package commands

import confirm
import cyan
import getFiles
import getFolders
import red
import toolConfig
import yellow
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path

val purgeDescription = """
    Purge your game data folder and delete all symlinks and rename override files.
    This should set you back to vanilla, and can catch files missed by disabling mods and deploying.
    Specifically, if you manually delete a mod outside the manager, a deploy may not delete its deployed files, but a purge should clean them up.
    Use dryrun to view what a purge would do without actually doing it.
""".trimIndent()

val purgeUsage = """
    purge
    purge dryrun
""".trimIndent()

fun purge(args: List<String>) {
    val dryRun = args.lastOrNull() == "dryrun"

    if (!dryRun) {
        confirm(false, yellow("Are you sure you want to purge?")) {
            purgeFiles(false)
        }
    } else {
        purgeFiles(true)
    }

}

private fun purgeFiles(dryRun: Boolean) {
    toolConfig.gamePath?.let { purgeFiles(dryRun, it) }
    toolConfig.iniPath?.let { purgeFiles(dryRun, it) }
    if (dryRun) {
        println(cyan("Purge dryrun compete"))
    } else {
        println(cyan("Purge compete"))
    }
}

private fun purgeFiles(dryRun: Boolean, gamePath: String) {
    deleteSymlinks(gamePath, dryRun)
    undoOverrides(gamePath, dryRun)
    deleteEmptyFolders(gamePath, dryRun)
}

private fun deleteSymlinks(gamePath: String, dryRun: Boolean) {
    File(gamePath).getFiles {
        Files.isSymbolicLink(it.toPath())
    }.forEach { link ->
        println(red("Deleting") + " ${link.path}")
        if (!dryRun) link.delete()
    }
}


private fun undoOverrides(gamePath: String, dryRun: Boolean) {
    File(gamePath).getFiles {
        it.nameWithoutExtension.endsWith("_overridden")
    }.forEach { link ->
        println("Unbacking up ${link.path}")
        if (!dryRun) {
            val ogPath = Path(
                "${link.parentFile.absolutePath}/${
                    link.nameWithoutExtension.replace(
                        "_overridden",
                        ""
                    )
                }.${link.extension}"
            )
            if (link.exists()) {
                Files.move(
                    link.toPath(),
                    ogPath,
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }
    }
}

private fun deleteEmptyFolders(gamePath: String, dryRun: Boolean) {
    File(gamePath)
        .getFolders()
        .sortedByDescending { it.absolutePath.length }
        .forEach { folder ->
            if (folder.listFiles()?.isEmpty() == true) {
                println(red("Deleting") + " ${folder.path}")
                if (!dryRun) {
                    folder.delete()
                }
            }
        }
}
