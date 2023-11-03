package commands

import cyan
import toolConfig
import toolData
import verbose
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path

fun deployHelp() = """
    deploy - Applies all mods to the game folder by creating the appropriate symlinks
    deploy dryrun - Per your load order view how files will be deployed
""".trimIndent()

fun deploy(args: List<String>) {
    val files = getAllModFiles()
    when {
        args.firstOrNull() == "dryrun" -> deployDryRun(files)
        toolConfig.gamePath == null -> println("No game path configured")
        files.isEmpty() -> println("No mod files found")
        else -> {
            getDisabledModPaths().forEach { deleteLink(it, files) }
            files.entries.forEach { (gamePath, modFile) -> makeLink(gamePath, modFile) }
            deployPlugins(files)
            println(cyan("Deployed ${files.size} files"))
        }
    }
}

private fun getDisabledModPaths(): List<String> {
    return toolData.mods.filter { !it.enabled }.flatMap { mod ->
        val modRoot = File(mod.filePath).absolutePath + "/"
        mod.getModFiles().map { file ->
            file.absolutePath.replace(modRoot, "")
        }
    }
}

private fun getAllModFiles(): Map<String, File> {
    val mappings = mutableMapOf<String, File>()
    toolData.mods.filter { it.enabled }.sortedBy { it.loadOrder }.forEach { mod ->
        val modRoot = File(mod.filePath).absolutePath + "/"
        mod.getModFiles().forEach { file ->
            mappings[file.absolutePath.replace(modRoot, "")] = file
        }
    }
    return mappings
}

fun makeLink(gamePath: String, modFile: File) {
    val gameFile = File(toolConfig.usedGamePath(gamePath) + "/$gamePath")
    gameFile.parentFile.mkdirs()
    if (Files.isSymbolicLink(gameFile.toPath())) {
        val existingLink = Files.readSymbolicLink(gameFile.toPath())
        if (existingLink != modFile.canonicalFile.toPath()) {
            println("Update: ${modFile.path}")
            gameFile.delete()
            Files.createSymbolicLink(gameFile.toPath(), modFile.canonicalFile.toPath())
        } else verbose("Skip: ${modFile.path}")
    } else if (gameFile.exists()) {
        verbose("Backup: ${gameFile.path}")
        verbose("Add: ${modFile.path}")
        Files.move(
            gameFile.toPath(),
            Path("${gameFile.parentFile.absolutePath}/${gameFile.nameWithoutExtension}_overridden.${gameFile.extension}"),
            StandardCopyOption.REPLACE_EXISTING
        )
        Files.createSymbolicLink(gameFile.toPath(), modFile.canonicalFile.toPath())
    } else {
        verbose("Add: ${modFile.path}")
        Files.createSymbolicLink(gameFile.toPath(), modFile.canonicalFile.toPath())
    }
}

fun deleteLink(gamePath: String, modFiles: Map<String, File>) {
    val gameFile = File(toolConfig.usedGamePath(gamePath) + "/$gamePath")
    if (!modFiles.contains(gamePath) && Files.isSymbolicLink(gameFile.toPath())) {
        verbose("Delete: $gamePath")
        gameFile.delete()
        val backedUpFile =
            File("${gameFile.parentFile.absolutePath}/${gameFile.nameWithoutExtension}_overridden.${gameFile.extension}")
        if (backedUpFile.exists()) {
            verbose("Restore: ${gameFile.path}")
            Files.move(backedUpFile.toPath(), gameFile.toPath())
        }
    }
}

