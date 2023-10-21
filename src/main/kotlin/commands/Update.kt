package commands

import Mod
import addModById
import fetchModInfo
import toolData
import updateModInfo
import java.io.File

fun updateHelp() = """
    update <mod index>
    update 1 2 4
    update 1-3
    update all
    Useful for checking for updates existing mods. To check add new mods, see fetch or add.
    To download updates, see upgrade
""".trimIndent()

fun update(args: List<String>) {
    if (args.isEmpty()) {
        println(updateHelp())
    } else {
        args.getIndicesOrRange(toolData.mods.size)
            .mapNotNull { toolData.byIndex(it) }
            .updateMods()
    }
}

private fun List<Mod>.updateMods(){
    mapIndexed{i, mod -> i to mod }.filter { it.second.id != null }
        .also { println("Updating ${it.size} mods") }
        .forEach { (i, mod) ->
            updateModInfo(mod.id!!)
            if (mod.updateAvailable()) println("(i: i) ${mod.name} can upgrade from ${mod.version} -> ${mod.latestVersion}")
        }
        println("Done Updating")
}