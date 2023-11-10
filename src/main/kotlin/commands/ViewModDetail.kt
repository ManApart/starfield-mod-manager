package commands

import Mod
import fetchModInfo
import toolData
import urlToId

val detailHelp = """
   detail <mod id> - View all mod detail
""".trimIndent()
val detailUsage = """
   detail <mod id> - View all mod detail
""".trimIndent()

fun detailMod(args: List<String>) {
    if (args.isEmpty()) {
        println(detailHelp)
    } else {
        args.getIndicesOrRange(toolData.mods.size)
            .mapNotNull { toolData.byIndex(it) }
            .forEach { viewDetail(it) }
    }
}

private fun viewDetail(mod: Mod) {
    println(mod)
}