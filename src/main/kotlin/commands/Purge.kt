package commands

import confirmation
import getFiles
import toolConfig
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path

fun purgeHelp() = """
    purge - delete all symlinks and rename override files
    purge dryrun - view what a purge would do without doing it
""".trimIndent()

fun purge(args: List<String>) {
    val dryRun = args.lastOrNull() == "dryrun"

    if (!dryRun) {
        println("Are you sure you want to purge? (y/n)")
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
}

private fun purgeFiles(dryRun: Boolean, gamePath: String) {
    File(gamePath).getFiles {
        Files.isSymbolicLink(it.toPath())
    }.forEach { link ->
        println("Deleting ${link.path}")
        if (!dryRun) link.delete()
    }

    File(gamePath).getFiles {
        it.nameWithoutExtension.endsWith("_override")
    }.forEach { link ->
        println("Unbacking up ${link.path}")
        if (!dryRun) {
            val ogPath = Path(
                "${link.parentFile.absolutePath}/${
                    link.nameWithoutExtension.replace(
                        "_override",
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
    if (dryRun) {
        println("Purge dryrun compete")
    } else {
        println("Purge compete")
    }
}