import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

fun stageMod(sourceFile: File, stageFolder: File, modName: String): Boolean {
    stageFolder.mkdirs()
    return when {
        sourceFile.isDirectory -> {
            sourceFile.copyRecursively(stageFolder, overwrite = true)
            true
        }

        sourceFile.extension == "zip" -> {
            stageFolder.runCommand(listOf("unzip", "-q", "-o", sourceFile.absolutePath), !toolConfig.verbose)
            true
        }

        sourceFile.extension in listOf("7z", "rar") -> {
            stageFolder.runCommand(listOf("7z", "x", "-y", sourceFile.absolutePath), !toolConfig.verbose)
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
    val stagedNames = stagedFiles.map { it.nameWithoutExtension.lowercase() }
    val dataTopLevelNames = listOf("textures", "music", "sound", "meshes", "video")
    when {
        stagedNames.contains("data") -> {}
        stagedNames.any { dataTopLevelNames.contains(it) } -> nestInData(modName, stageFolder, stagedFiles)
        stagedFiles.size == 1 && stagedFiles.firstOrNull()?.isDirectory ?: false -> unNestFiles(
            modName,
            stageFolder,
            stagedFiles
        )
        //TODO FMOD
        else -> println("Unable to guess folder path for $modName. You should open the staging folder and make sure it was installed correctly.")
    }
}

fun unNestFiles(modName: String, stageFolder: File, stagedFiles: Array<File>) {
    println("Unnesting files in data for $modName")
    val topFolder = stagedFiles.first()
    topFolder.listFiles()?.forEach { nested ->
        unNest(stageFolder, nested, topFolder.path)
    }
    topFolder.deleteRecursively()
}

private fun unNest(stageFolder: File, nested: File, topPath: String) {
    val newPath = stageFolder.path + nested.path.replace(topPath, "")
    Files.copy(nested.toPath(), Path(newPath))
    if (nested.isDirectory){
        nested.listFiles()?.forEach { moreNested ->
            unNest(stageFolder, moreNested, topPath)
        }
    }
}

private fun nestInData(modName: String, stageFolder: File, stagedFiles: Array<File>) {
    println("Nesting files in data for $modName")
}