package commands

import Mod
import toolData

val versionDescription = """
    View the current and latest version of a mod
""".trimIndent()
val versionUsage = """
    version <index>
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