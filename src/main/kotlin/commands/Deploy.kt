package commands

import toolConfig
import toolState
import verbose
import java.io.File
import java.nio.file.Files

fun deployHelp(args: List<String>) = """
""".trimIndent()

fun deploy(args: List<String>) {
    val files = getAllModFiles()
    getDisabledModPaths().forEach { deleteLink(it, files) }
    if (files.isEmpty()) {
        println("No mod files found")
    } else {
//        println("Found Mod Files:\n${files.entries.joinToString("\n") { (key, file) -> "$key: ${file.path}" }}")
        files.entries.forEach { (gamePath, modFile) -> makeLink(gamePath, modFile) }
        println("Deployed ${files.size} files")
    }
}

private fun getDisabledModPaths(): List<String> {
    return toolState.mods.filter { !it.enabled }.flatMap { mod ->
        val modRoot = File(mod.filePath).absolutePath + "/"
        mod.getModFiles().map { file ->
            file.absolutePath.replace(modRoot, "")
        }
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
    val gameFile = File(toolConfig.dataPath + "/$gamePath")
    gameFile.parentFile.mkdirs()
    if (Files.isSymbolicLink(gameFile.toPath())) {
        val existingLink = Files.readSymbolicLink(gameFile.toPath())
        if (existingLink != modFile.canonicalFile.toPath()) {
            println("Update: ${modFile.path}")
            gameFile.delete()
            Files.createSymbolicLink(gameFile.toPath(), modFile.canonicalFile.toPath())
        } else verbose("Skip: ${modFile.path}")
    } else if (gameFile.exists()) {
        //TODO - eventually backup real file and create link
        println("Skipping replacing real file ${modFile.path}")
    } else {
        verbose("Add: ${modFile.path}")
        Files.createSymbolicLink(gameFile.toPath(), modFile.canonicalFile.toPath())
    }
}

fun deleteLink(gamePath: String, modFiles: Map<String, File>) {
    val gameFile = File(toolConfig.dataPath + "/$gamePath")
    if (!modFiles.contains(gamePath) && Files.isSymbolicLink(gameFile.toPath())) {
        verbose("Delete: $gamePath")
        gameFile.delete()
    }

}