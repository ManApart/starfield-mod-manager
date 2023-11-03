package commands

import Mod
import addModById
import cyan
import toolData
import java.io.File

fun upgradeHelp() = """
    upgrade <mod index>
    upgrade 1 2 4
    upgrade 1-3
    upgrade all - For all mods with newer versions, attempt to stage the latest version.
    If you want to check for new versions, see update
    If you're looking to just redownload or restage a file at the current version, see refresh
""".trimIndent()

fun upgrade(args: List<String>) {
    if (args.isEmpty()) {
        println(upgradeHelp() + "\n")
        val updatable = toolData.mods.map { it to it.updateAvailable() }
        display(updatable)
    } else {
        args.getIndicesOrRange(toolData.mods.size)
            .mapNotNull { toolData.byIndex(it) }
            .upgradeMods()
    }
}

private fun List<Mod>.upgradeMods(){
    filter { it.id != null && it.updateAvailable()}
        .also { println(cyan("Upgrading ${it.size} mods")) }
        .forEach { addModById(it.id!!, it.latestFileId, true) }
        println(cyan("Done Upgrading"))
}