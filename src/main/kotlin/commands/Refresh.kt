package commands

import Mod
import cyan
import doCommand
import refreshMod
import toolData
import java.io.File

val refreshHelp = """
    refresh <mod index>
    refresh 1 2 4
    refresh 1-3
    refresh all - For all mods with ids, attempt to redownload (or grab the file from the downloads folder if it exists) and restage.
    refresh empty - Refresh any files without staged data
    refresh staged - Refresh only files that are staged
    refresh enabled - Refresh only files that are enabled
    refresh disabled - Refresh only files that are NOT enabled
    If you're looking to upgrade to a new version, see update and upgrade
""".trimIndent()

val refreshUsage = """
    refresh <mod index>
    refresh 1 2 4
    refresh 1-3
    refresh all - For all mods with ids, attempt to redownload (or grab the file from the downloads folder if it exists) and restage.
    refresh empty - Refresh any files without staged data
    refresh staged - Refresh only files that are staged
    refresh enabled - Refresh only files that are enabled
    refresh disabled - Refresh only files that are NOT enabled
    If you're looking to upgrade to a new version, see update and upgrade
""".trimIndent()

fun refresh(args: List<String>) {
    doCommand(args, List<Mod>::refreshMods)
}

private fun List<Mod>.refreshMods() {
    also { println(cyan("Refreshing ${it.size} mods")) }
        .forEach { refreshMod(it) }
    println(cyan("Done Refreshing"))
}