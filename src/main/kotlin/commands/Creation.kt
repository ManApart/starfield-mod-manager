package commands

import Mod
import modFolder
import red
import save
import toolConfig
import toolData
import java.io.File
import java.nio.file.Files

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

private fun listCreations(){
    File(toolConfig.gamePath!! + "/Data").listFiles()!!
        .asSequence()
        .filter { it.name.lowercase().startsWith("sfbgs") }
        .map { it.nameWithoutExtension.split(" ")[0] }
        .toSet().sorted().joinToString(", ")
        .let { println(it) }

}

private fun addAllCreations(){

}

fun addCreation(creationId: String, nameOverride: String?) {
    val files = File(toolConfig.gamePath!! + "/Data").listFiles()!!.filter { it.path.lowercase().contains(creationId.lowercase()) }
    if (files.isEmpty()) {
        println(red("No files found for $creationId"))
    }
    val name = nameOverride ?: creationId

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
        Files.move(file.toPath(), File(dest.path + "/" + file.name).toPath())
    }

    if (existing != null) {
        println("Updated (${mod.index}) ${mod.name}")
    } else {
        println("Added (${mod.index}) ${mod.name}")
    }
}
