package commands

import Mod
import confirmation
import modFolder
import red
import save
import toolConfig
import toolData
import yellow
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.StandardCopyOption

val creationDescription = """
   Tools for managing creations just like other mods
   creation ls - lists unmanaged creations by examining your game data folder
   creation add - adds a single creation by its id (like SFBGS021). Also see add mod
   creation add all - Attempts to add _all_ unmanaged creations found in the data folder
""".trimIndent()

val creationUsage = """
   creation ls
   creation add <id> *<name>
   creation add all
""".trimIndent()

val idMap by lazy {
    ClassLoader.getSystemClassLoader().getResourceAsStream("creationIds.txt")
        .let { BufferedReader(InputStreamReader(it!!)).lines().toList() }
        .associate { line ->
            val (key, value) = line.split("=")
            key.lowercase() to value.lowercase().replace(" ", "-")
        }
}

fun creation(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(creationDescription)
        listOf("ls", "list").contains(firstArg) -> listCreations()
        args.getOrNull(1) == "all" && firstArg == "add" -> addAllCreations()
        firstArg == "add" -> addCreation(args.getOrNull(1)!!, args.getOrNull(2))

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

private fun listCreations() = println(getUnmanagedCreations().sorted().creationFriendlyNames())

private fun addAllCreations() {
    val creations = getUnmanagedCreations()
    println(yellow("Add unmanaged creations? ") + creations.toList().creationFriendlyNames() + " (y/n)")
    confirmation = { c ->
        if (c.firstOrNull() == "y") {
            creations.forEach { addCreation(it) }
        }
    }
}

private fun List<String>.creationFriendlyNames(): String {
    return joinToString(", ") { it + (idMap[it.lowercase()]?.let { name -> " ($name)" } ?: "") }
}

private fun getUnmanagedCreations(): Set<String> {
    return File(toolConfig.gamePath!! + "/Data").listFiles()!!
        .asSequence()
        .filter { it.name.lowercase().startsWith("sfbgs") }
        .map { it.nameWithoutExtension.split(" ")[0] }.toSet()
}

fun addCreation(creationId: String, nameOverride: String? = null) {
    val files = File(toolConfig.gamePath!! + "/Data").listFiles()!!.filter { it.path.lowercase().contains(creationId.lowercase()) }
    if (files.isEmpty()) {
        println(red("No files found for $creationId"))
    }
    val name = nameOverride ?: idMap[creationId] ?: creationId

    val existing = toolData.byName(name, true)
    val mod = if (existing != null) existing else {
        val loadOrder = toolData.nextLoadOrder()
        val stagePath = modFolder.path + "/" + name.replace(" ", "-")
        Mod(name, stagePath, loadOrder + 1).also {
            it.index = toolData.mods.size
            it.tags.add("Creation")
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
