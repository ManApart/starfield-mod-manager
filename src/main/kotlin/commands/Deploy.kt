package commands

import toolState
import java.io.File

fun deployHelp(args: List<String>) = """
""".trimIndent()

fun deploy(args: List<String>) {
    val files = getAllModFiles()
    if (files.isEmpty()) {
        println("No mod files found")
    } else {
        println("Found Mod Files: ${files.joinToString("\n") { it.absolutePath }}")
    }
}

private fun getAllModFiles(): List<File> {
    val paths = mutableSetOf<File>()
    //TODO -test priority / load order
    toolState.mods.filter { it.enabled }.sortedByDescending { it.loadOrder }.forEach { mod ->
        paths.addAll(mod.getModFiles())
    }
    return paths.toList()
}