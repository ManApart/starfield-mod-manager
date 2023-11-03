import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path

fun stageMod(sourceFile: File, stageFolder: File, modName: String): Boolean {
    stageFolder.mkdirs()
    stageFolder.listFiles()?.forEach { it.deleteRecursively() }
    return when {
        sourceFile.isDirectory -> {
            sourceFile.copyRecursively(stageFolder, overwrite = true)
            true
        }

        sourceFile.extension == "zip" -> {
            stageFolder.runCommand(listOf("unzip", "-q", "-o", sourceFile.absolutePath), !toolConfig.verbose)
            true
        }

        sourceFile.extension == "7z" -> {
            stageFolder.runCommand(listOf("7z", "x", "-y", sourceFile.absolutePath), !toolConfig.verbose)
            true
        }

        sourceFile.extension == "rar" -> {
            stageFolder.runCommand(listOf("bsdtar", "-xf", sourceFile.absolutePath), !toolConfig.verbose)
            true
        }

        else -> {
            println("Unknown Filetype: ${sourceFile.extension}")
            false
        }
    }.also { success ->
        if (success) {
            fixFolderPath(modName, stageFolder)
        }
    }
}

private fun fixFolderPath(modName: String, stageFolder: File) {
    val stagedFiles = stageFolder.listFiles() ?: arrayOf()
    val action = detectStagingChanges(stageFolder)
    when (action) {
        StageChange.NONE -> {}
        StageChange.NO_FILES -> println("No staged files found for $modName")
        StageChange.CAPITALIZE -> capitalizeData(stageFolder)
        StageChange.NEST -> nestInData(modName, stageFolder, stagedFiles)
        StageChange.UNNEST -> unNestFiles(modName, stageFolder, stagedFiles)
        StageChange.FOMOD -> println("FOMOD detected for $modName. You should open the staging folder and pick options yourself.")
        else -> println("Unable to guess folder path for $modName. You should open the staging folder and make sure it was installed correctly.")
    }
    properlyCasePaths(stageFolder)
}

enum class StageChange { NONE, NEST, UNNEST, FOMOD, CAPITALIZE, NO_FILES, UNKNOWN }

fun detectStagingChanges(stageFolder: File): StageChange {
    val stagedFiles = stageFolder.listFiles() ?: arrayOf()
    val stagedNames = stagedFiles.map { it.nameWithoutExtension.lowercase() }
    val stagedExtensions = stagedFiles.map { it.extension }
    val dataTopLevelNames = listOf("textures", "music", "sound", "meshes", "video", "sfse_readme", "sfse")
    val dataTopLevelExtensions = listOf("esp", "esm", "ba2")
    val firstFile = stagedFiles.firstOrNull()
    return when {
        stagedFiles.isEmpty() -> StageChange.NO_FILES
        stagedFiles.any { it.nameWithoutExtension == "data" } -> StageChange.CAPITALIZE
        stagedNames.contains("data") -> StageChange.NONE
        stagedNames.any { dataTopLevelNames.contains(it) } -> StageChange.NEST
        stagedExtensions.any { dataTopLevelExtensions.contains(it) } -> StageChange.NEST
        firstFile?.isDirectory ?: false && firstFile?.nameWithoutExtension?.startsWith("sfse_") ?: false -> StageChange.UNNEST
        stagedFiles.size == 1 && firstFile?.isDirectory ?: false && stagedFiles.first().listFiles()
            ?.map { it.nameWithoutExtension.lowercase() }?.contains("data") ?: false -> StageChange.UNNEST

        stagedNames.contains("fomod") -> StageChange.FOMOD
        else -> StageChange.UNKNOWN
    }
}

fun unNestFiles(modName: String, stageFolder: File, stagedFiles: Array<File>) {
    println("Unnesting files in data for $modName")
    val topFolder = stagedFiles.first()
    try {
        topFolder.listFiles()?.forEach { nested ->
            unNest(stageFolder.path, nested, topFolder.path)
        }
        topFolder.deleteRecursively()
    } catch (e: Exception) {
        println("Failed to unnest files. Please fix manually")
        verbose(e.message ?: "")
        verbose(e.stackTraceToString())
    }
}

private fun unNest(stageFolderPath: String, nested: File, topPath: String) {
    val newPath = stageFolderPath + nested.path.replace(topPath, "")
    Files.move(nested.toPath(), Path(newPath), StandardCopyOption.REPLACE_EXISTING)
    if (nested.isDirectory) {
        nested.listFiles()?.forEach { moreNested ->
            unNest(stageFolderPath, moreNested, topPath)
        }
    }
}

private fun nestInData(modName: String, stageFolder: File, stagedFiles: Array<File>) {
    println("Nesting files in data for $modName")
    try {
        val dataFolder = File(stageFolder.path + "/Data").also { it.mkdirs() }
        stagedFiles.forEach { file ->
            nest(stageFolder.path, file, dataFolder.path)
        }
    } catch (e: Exception) {
        println("Failed to nest files. Please fix manually")
        verbose(e.message ?: "")
        verbose(e.stackTraceToString())
    }
}

private fun nest(stageFolderPath: String, file: File, dataPath: String) {
    val newPath = Path(file.path.replace(stageFolderPath, dataPath))
    Files.move(file.toPath(), newPath, StandardCopyOption.REPLACE_EXISTING)
    if (file.isDirectory) {
        file.listFiles()?.forEach { moreNested ->
            nest(stageFolderPath, moreNested, dataPath)
        }
    }
}

private fun capitalizeData(stageFolder: File) {
    Files.move(Path(stageFolder.path + "/data"), Path(stageFolder.path + "/Data"))
}

private fun properlyCasePaths(stageFolder: File) {
    File(stageFolder.absolutePath).listFiles()?.filter { it.isDirectory }?.forEach { case(it) }
}

private fun case(folder: File) {
    val newPath = folder.parent + "/" + folder.name.lowercase()
    val next = if (folder.name != "Data") {
        Files.move(folder.toPath(), Path(newPath), StandardCopyOption.REPLACE_EXISTING)
        File(newPath)
    } else folder
    next.listFiles()?.filter { it.isDirectory }?.forEach { case(it) }
}
