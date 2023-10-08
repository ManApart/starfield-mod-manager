package commands

import save
import toolState

fun enableHelp(args: List<String>) = """
    enable <mod index>
    disable <mod index>
""".trimIndent()

fun enable(args: List<String>) = enableMod(true, args)

fun disable(args: List<String>) = enableMod(false, args)

fun enableMod(enable: Boolean = true, args: List<String>) {
    if (args.size != 1 || args.first().toIntOrNull() == null) {
        println(enableHelp(listOf()))
    } else {
        val i = args.first().toInt()
        if (i >= toolState.mods.size) {
            println("Invalid Mod index $i")
        } else {
            toolState.mods[i].enabled = enable
            save()
            CommandType.LIST.apply(listOf())
        }
    }
}