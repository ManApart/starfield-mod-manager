package commands

import Mod
import addModById
import cyan
import doCommand
import fetchModInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import toolConfig
import toolData
import updateModInfo
import java.io.File

val updateHelp = """
    update - fetches latest metadata for mods, including new versions and endorsement data
    update <mod index>
    update 1 2 4
    update 1-3
    update staged
    update enabled
    Useful for checking for updates existing mods. To check add new mods, see fetch or add.
    To download updates, see upgrade
""".trimIndent()

val updateUsage = """
    update - fetches latest metadata for mods, including new versions and endorsement data
    update <mod index>
    update 1 2 4
    update 1-3
    update staged
    update enabled
    Useful for checking for updates existing mods. To check add new mods, see fetch or add.
    To download updates, see upgrade
""".trimIndent()

fun update(args: List<String>) {
    doCommand(args, List<Mod>::updateMods)
}

private fun List<Mod>.updateMods() {
    filter { it.id != null }
        .also { println(cyan("Updating ${it.size} mods")) }
        .chunked(toolConfig.chunkSize)
        .forEach { chunk ->
            runBlocking {
                chunk.map { mod ->
                    async {
                        updateModInfo(mod.id!!)
                        if (mod.updateAvailable()) println("${mod.index} ${mod.name} can upgrade ${mod.version} -> ${mod.latestVersion}")
                    }
                }.awaitAll()
            }
            println("Updated ${chunk.joinToString { it.index.toString() }}")
        }
    println(cyan("Done Updating"))
}