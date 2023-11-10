package commands

import confirmation
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

val purgeHelp = """
    purge - delete all symlinks and rename override files
    purge dryrun - view what a purge would do without doing it
""".trimIndent()

val purgeUsage = """
    purge - delete all symlinks and rename override files
    purge dryrun - view what a purge would do without doing it
""".trimIndent()

fun purge(args: List<String>) {
    val dryRun = args.lastOrNull() == "dryrun"

    if (!dryRun) {
        println(yellow("Are you sure you want to purge?") + " (y/n)")
        confirmation = { c ->
            if (c.firstOrNull() == "y") {
                purgeFiles(false)
            }
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