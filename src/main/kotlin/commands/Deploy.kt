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
        println("Found Mod Files:\n${files.entries.joinToString("\n") { (key, file) -> "$key: ${file.path}" }}")
    }
}

private fun getAllModFiles(): Map<String, File> {
    val mappings = mutableMapOf<String, File>()
    toolState.mods.filter { it.enabled }.sortedBy { it.loadOrder }.forEach { mod ->
        val modRoot = File(mod.filePath).absolutePath + "/"
        mod.getModFiles().forEach { file ->
            mappings[file.absolutePath.replace(modRoot, "")] = file
        }
    }
    return mappings
}