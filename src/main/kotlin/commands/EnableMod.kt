package commands

import save
import toolState

fun enableHelp(args: List<String>) = """
    enable <mod index>
    disable <mod index>
    enable 1 2 4
    enable 1-4
    disable all
""".trimIndent()

fun enable(args: List<String>) = enableMod(true, args)

fun disable(args: List<String>) = enableMod(false, args)

fun enableMod(enable: Boolean = true, args: List<String>) {
    when {
        args.isEmpty() -> println(enableHelp(listOf()))
        args.size == 1 && args.first().contains("-") -> enableRange(enable, args)
        args.size == 1 && args.first() == "all" -> enableRange(enable, listOf("0-${toolState.mods.size - 1}"))
        else -> enableList(enable, args)
    }
}

private fun enableList(enable: Boolean, args: List<String>) {
    args.getIndices(toolState.mods.size).forEach { i ->
        toolState.mods[i].enabled = enable
    }
    save()
    CommandType.LIST.apply(listOf())
}

private fun enableRange(enable: Boolean, args: List<String>) {
    val range = args.getRange(toolState.mods.size)
    if (range.isNotEmpty()) {
        range.forEach { i ->
            toolState.mods[i].enabled = enable
        }
        save()
        CommandType.LIST.apply(listOf())
    }
}