package commands

import Mod
import PathType
import gameMode
import nestInPrefix
import toolData
import java.io.File

val nestDescription = """
    Nests all mod files under the given path and then opens the folder locally
    Used to make fixing mod installs to unrecognized paths
""".trimIndent()

val nestUsage = """
    nest <index> <PathType>
    nest 1 DATA
    nest 2 paks
""".trimIndent()

fun nest(command: String, args: List<String>) {
    val pathType = args.getOrNull(1)?.let { arg -> PathType.entries.firstOrNull { it.name.lowercase() == arg } }
    val generatedPath = pathType?.let { gameMode.generatedPaths[it] }
    val mod = args.firstOrNull()?.toIntOrNull()?.let { toolData.byIndex(it) }
    when {
        args.isEmpty() -> println(nestDescription + "\n" + nestUsage)
        args.size != 2 -> println("Please provide a mod index and a path to nest in")
        mod == null -> println("Unable to find mod with index ${args.firstOrNull()}")
        pathType == null -> println("Unable to find path for ${args.getOrNull(1)}. Try one of ${PathType.entries.filter { gameMode.generatedPaths.containsKey(it) }.joinToString()}")
        generatedPath == null -> println("${gameMode.displayName} does not have a path for $pathType")
        pathType == PathType.DATA -> nestMod(mod, gameMode.deployedModPath)
        generatedPath.suffix.isBlank() -> println("$pathType can't be used because it has no suffix")
        else -> nestMod(mod, generatedPath.suffix)
    }
}

private fun nestMod(mod: Mod, path: String) {
    val stageFolder = File(mod.filePath)
    val stagedFiles = stageFolder.listFiles() ?: arrayOf()
    nestInPrefix(mod.name, path, stageFolder, stagedFiles)
    open(mod.filePath + path, mod.name, false)
}
