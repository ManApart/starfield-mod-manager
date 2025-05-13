package commands

import Mod
import toolData

val detailDescription = """
   View all mod detail
""".trimIndent()
val detailUsage = """
   detail <mod id>
""".trimIndent()

fun detailMod(command: String, args: List<String>) {
    if (args.isEmpty()) {
        println(detailDescription)
    } else {
        args.getIndicesOrRange(toolData.mods.size)
            .mapNotNull { toolData.byIndex(it) }
            .forEach { viewDetail(it) }
    }
}

private fun viewDetail(mod: Mod) {
    println(mod)
}
