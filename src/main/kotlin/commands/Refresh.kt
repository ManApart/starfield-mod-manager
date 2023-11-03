package commands

import Mod
import cyan
import refreshMod
import toolData
import java.io.File

fun refreshHelp() = """
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
    when {
        args.isEmpty() -> println(refreshHelp())
        args.first() == "empty" -> refresh { !File(it.filePath).exists() }
        args.first() == "staged" -> refresh { File(it.filePath).exists() }
        args.first() == "enabled" -> refresh { it.enabled }
        args.first() == "disabled" -> refresh { !it.enabled }
        else -> {
            args.getIndicesOrRange(toolData.mods.size)
                .mapNotNull { toolData.byIndex(it) }
                .refreshMods()
        }
    }
}

private fun refresh(filter: (Mod) -> Boolean) {
    toolData.mods.filter(filter).refreshMods()
}

private fun List<Mod>.refreshMods() {
    also { println(cyan("Refreshing ${it.size} mods")) }
        .forEach { refreshMod(it) }
    println(cyan("Done Refreshing"))
}