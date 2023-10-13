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
        args.size == 1 && args.first() == "all" -> enableRange(enable, listOf("0-${toolState.mods.size-1}"))
        else -> enableList(enable, args)
    }
}

private fun enableList(enable: Boolean, args: List<String>) {
    args.map { it.toInt() }.forEach { i ->
        if (i >= toolState.mods.size || i < 0) {
            println("Invalid Mod index $i")
        } else {
            toolState.mods[i].enabled = enable
        }
    }
    save()
    CommandType.LIST.apply(listOf())
}

private fun enableRange(enable: Boolean, args: List<String>) {
    val parts = args.first().split("-")
    val start = parts.first().toInt()
    val end = parts.last().toInt()
    if (start < 0 || start >= end || end >= toolState.mods.size) {
        println("Invalid Range $start-$end")
    } else {
        (start..end).forEach { i ->
            toolState.mods[i].enabled = enable
        }
        save()
        CommandType.LIST.apply(listOf())
    }
}