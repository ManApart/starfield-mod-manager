package commands

import Column
import Mod
import Table
import confirmation
import jsonMapper
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import modFolder
import red
import save
import toolConfig
import toolData
import yellow
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Creation(
    @JsonNames("AchievementSafe") val achievementSafe: Boolean,
    @JsonNames("Files") val files: List<String>,
    @JsonNames("Title") val title: String,
    @JsonNames("Version") val version: String,
) {
    var creationId: String? = null
    val modFileId = files.firstOrNull { it.contains("esm") }?.replace(".esm", "")
}

val creationDescription = """
   Tools for managing creations just like other mods
   creation ls - lists unmanaged creations by examining your game content catalog
   creation add - adds a single creation by its mod file id (like SFBGS021) or content id (like TM_31ccf130-4852-417b-842a-9d82672028e4). Also see add mod
   creation add all - Attempts to add _all_ unmanaged creations found in the content catalog
""".trimIndent()

val creationUsage = """
   creation ls
   creation add <id>
   creation add all
""".trimIndent()

fun creation(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(creationDescription)
        listOf("ls", "list").contains(firstArg) -> listCreations()
        args.getOrNull(1) == "all" && firstArg == "add" -> addAllCreations()
        firstArg == "add" -> addCreation(args.getOrNull(1)!!)

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

private fun listCreations() {
    val columns = listOf(
        Column("Creation Id", 42),
        Column("Mod File Id", 15),
        Column("Title", 22),
    )
    val data = parseCreationCatalog().values.map { creation ->
        mapOf(
            "Creation Id" to (creation.creationId ?: ""),
            "Mod File Id" to (creation.modFileId ?: ""),
            "Title" to creation.title,
        )
    }

    Table(columns, data).print()
}

private fun addAllCreations() {
    val creations = parseCreationCatalog().values.filter { it.creationId == null || toolData.byCreationId(it.creationId!!) != null }
    println(yellow("Add unmanaged creations? ") + creations.joinToString(", ") { it.title } + " (y/n)")
    confirmation = { c ->
        if (c.firstOrNull() == "y") {
            creations.forEach { addCreation(it) }
        }
    }
}

fun addCreation(creationId: String) {
    val creations = parseCreationCatalog()
    val creation: Creation? = creations[creationId] ?: creations.values.firstOrNull { it.modFileId?.lowercase() == creationId.lowercase() }
    creation?.let { addCreation(it) }
}

fun addCreation(creation: Creation) {
    val files = creation.files.map { File(toolConfig.gamePath!! + "/Data/$it") }
    if (files.isEmpty()) {
        println(red("No files found for ${creation.title}"))
    }

    val existing = toolData.byName(creation.title, true)
    val mod = if (existing != null) existing else {
        val loadOrder = toolData.nextLoadOrder()
        val stagePath = modFolder.path + "/" + creation.title.replace(" ", "-")
        Mod(creation.title, stagePath, loadOrder + 1).also {
            it.index = toolData.mods.size
            it.creationId = creation.creationId
            it.add(Tag.CREATION)
            it.refreshPlugins()
            toolData.mods.add(it)
            save()
        }
    }

    val dest = File(mod.filePath + "/Data").also { it.mkdirs() }
    files.forEach { file ->
        Files.move(file.toPath(), File(dest.path + "/" + file.name).toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    if (existing != null) {
        println("Updated (${mod.index}) ${mod.name}")
    } else {
        println("Added (${mod.index}) ${mod.name}")
    }
}

fun parseCreationCatalog(): Map<String, Creation> {
    val rawLines = File(toolConfig.appDataPath + "/ContentCatalog.txt").readLines()
    val parsable = "{" + rawLines.drop(6).joinToString("\n")
    return jsonMapper.decodeFromString<Map<String, Creation>>(parsable).also { it.entries.forEach { (id, creation) -> creation.creationId = id } }
}

fun parseCreationPlugins(): List<String> {
    return parseCreationCatalog().values.flatMap { creation -> creation.files.filter { file -> espTypes.any { file.endsWith(it) } } }
}
