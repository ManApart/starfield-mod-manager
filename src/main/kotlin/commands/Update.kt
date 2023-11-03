package commands

import Mod
import addModById
import cyan
import fetchModInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import toolConfig
import toolData
import updateModInfo
import java.io.File

fun updateHelp() = """
    update - fetches latest metadata for mods, including new versions and endorsement data
    update <mod index>
    update 1 2 4
    update 1-3
    Useful for checking for updates existing mods. To check add new mods, see fetch or add.
    To download updates, see upgrade
""".trimIndent()

fun update(args: List<String>) {
    if (args.isEmpty()) {
        toolData.mods.updateMods()
    } else {
        args.getIndicesOrRange(toolData.mods.size)
            .mapNotNull { toolData.byIndex(it) }
            .updateMods()
    }
}

private fun List<Mod>.updateMods() {
    mapIndexed { i, mod -> i to mod }.filter { it.second.id != null }
        .also { println(cyan("Updating ${it.size} mods")) }
        .chunked(toolConfig.chunkSize)
        .forEach { chunk ->
            runBlocking {
                chunk.map { (i, mod) ->
                    async {
                        updateModInfo(mod.id!!)
                        if (mod.updateAvailable()) println("$i ${mod.name} can upgrade ${mod.version} -> ${mod.latestVersion}")
                    }
                }.awaitAll()
            }
            println("Updated ${chunk.joinToString{it.first.toString()}}")
        }
    println(cyan("Done Updating"))
}