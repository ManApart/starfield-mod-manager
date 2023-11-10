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

val versionHelp = """
    version <mod index> - view the current and latest version of a mod
    version 1 2 4
    version 1-3
""".trimIndent()
val versionUsage = """
    version <mod index> - view the current and latest version of a mod
    version 1 2 4
    version 1-3
""".trimIndent()

fun version(args: List<String>) {
    if (args.isEmpty()) {
        toolData.mods.viewVersion()
    } else {
        args.getIndicesOrRange(toolData.mods.size)
            .mapNotNull { toolData.byIndex(it) }
            .viewVersion()
    }
}

private fun List<Mod>.viewVersion() {
    forEach { mod ->
        println("${mod.index} ${mod.version} -> ${mod.latestVersion} ${mod.name}")
    }

}