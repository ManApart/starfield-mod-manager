package commands

import Column
import Mod
import Table
import confirmation
import modFolder
import save
import toolConfig
import toolData
import yellow
import java.io.File
import java.nio.file.Files

enum class Tag(val tag: String) {
    CREATION("Creation"),
    EXTERNAL("External"),
}

val tagDescription = """
Tools for managing external plugins
external ls - lists unmanaged plugins by examining your game data folder
external add - adds a single plugin by its id (like SFBGS003). Also see add mod
external add all - Attempts to add _all_ unmanaged plugins found in the data folder

""".trimIndent()

val tagUsage = """
   external ls
   external add <id>
   external add all
""".trimIndent()

fun tag(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
//    when {
//        args.isEmpty() -> println(creationDescription)
//        listOf("ls", "list").contains(firstArg) -> listExternal()
//        args.getOrNull(1) == "all" && firstArg == "add" -> addAllExternal()
//        firstArg == "add" -> addExternal(args.getOrNull(1)!!, args.getOrNull(2))
//
//        else -> println("Unknown args: ${args.joinToString(" ")}")
//    }
}

private fun listExternal() {
//    val columns = listOf(
//        Column("Esp", 42),
//        Column("Index", 15),
//        Column("Name", 22),
//    )
//    val data = getExternalMods().map { (esp, mod) ->
//        mapOf(
//            "Esp" to esp,
//            "Index" to (mod?.index ?: ""),
//            "Name" to (mod?.name ?: ""),
//        )
//    }
//
//    Table(columns, data).print()
}