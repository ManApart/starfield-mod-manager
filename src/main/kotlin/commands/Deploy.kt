package commands

import toolConfig
import toolState
import java.io.File
import java.nio.file.Files

fun deployHelp(args: List<String>) = """
""".trimIndent()

fun deploy(args: List<String>) {
    val files = getAllModFiles()
    if (files.isEmpty()) {
        println("No mod files found")
    } else {
//        println("Found Mod Files:\n${files.entries.joinToString("\n") { (key, file) -> "$key: ${file.path}" }}")
        files.entries.forEach { (gamePath, modFile) -> makeLink(gamePath, modFile) }
        println("Deployed ${files.size} files")
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

fun makeLink(gamePath: String, modFile: File) {
    val gameFile = File(toolConfig.gamePath + "/$gamePath")
    gameFile.parentFile.mkdirs()
    if (Files.isSymbolicLink(gameFile.toPath())) {
        val existingLink = Files.readSymbolicLink(gameFile.toPath())
        if (existingLink != modFile.canonicalFile.toPath()) {
            println("Update link ${modFile.path}")
            gameFile.delete()
            Files.createSymbolicLink(gameFile.toPath(), modFile.canonicalFile.toPath())
        }
    } else if (gameFile.exists()) {
        //TODO - eventually backup real file and create link
        println("Skipping replacing real file ${modFile.path}")
    } else {
        Files.createSymbolicLink(gameFile.toPath(), modFile.canonicalFile.toPath())
    }
}