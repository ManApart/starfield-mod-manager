package commands

import save
import toolData

fun enableHelp() = """
    enable <mod index>
    disable <mod index>
    enable 1 2 4
    enable 1-4
    disable all
""".trimIndent()

fun enable(args: List<String>) = enableMod(true, args)

fun disable(args: List<String>) = enableMod(false, args)

private fun enableMod(enable: Boolean = true, args: List<String>) {
    when {
        args.isEmpty() -> println(enableHelp())
        args.size == 1 && args.first().contains("-") -> enableRange(enable, args)
        args.size == 1 && args.first() == "all" -> enableRange(enable, listOf("0-${toolData.mods.size - 1}"))
        else -> enableList(enable, args)
    }
}

private fun enableList(enable: Boolean, args: List<String>) {
    val names = args.getIndices(toolData.mods.size).map { i ->
        toolData.mods[i].enabled = enable
        toolData.mods[i].name
    }.joinToString(", ")
    save()
    if (enable) println("Enabled $names") else println("Disabled $names")
}

private fun enableRange(enable: Boolean, args: List<String>) {
    val range = args.getRange(toolData.mods.size)
    if (range.isNotEmpty()) {
        val names = range.joinToString(", ") { i ->
            toolData.mods[i].enabled = enable
            toolData.mods[i].name
        }
        save()
        if (enable) println("Enabled $names") else println("Disabled $names")
    }
}