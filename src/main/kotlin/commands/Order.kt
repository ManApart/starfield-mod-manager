package commands

import Mod
import toolData

fun orderHelp(args: List<String> = listOf()) = """
    order 1 first
    order 1 last
    order 1 sooner 5
    order 1 later
    order 1 set 4 - sets to exactly this order in the load, bumping later mods down
""".trimIndent()

private data class Args(val index: Int, val subCommand: String, val amount: Int?)

fun order(args: List<String>) {
    val arguments = parseArgs(args)
    if (arguments == null) {
        println(orderHelp())
        return
    }
    with(arguments) {
        when {
            subCommand == "first" -> setModOrder(toolData.mods, index, 0)
            subCommand == "last" -> setModOrder(toolData.mods, index, toolData.nextLoadOrder())
            subCommand == "set" && amount != null -> setModOrder(toolData.mods, index, amount)
            subCommand == "sooner" && amount != null -> setModOrder(toolData.mods, index, index - amount)
            subCommand == "later" && amount != null -> setModOrder(toolData.mods, index, index + amount)
            subCommand == "sooner" -> setModOrder(toolData.mods, index, index - 1)
            subCommand == "later" -> setModOrder(toolData.mods, index, index + 1)
            else -> println("Unknown subCommand: ")
        }
    }
}

private fun parseArgs(args: List<String>): Args? {
    val index = args.getOrNull(0)?.toIntOrNull()
    val subCommand = args.getOrNull(1)
    val amount = args.getOrNull(2)?.toIntOrNull()
    return if (index != null && subCommand != null) {
        Args(index, subCommand, amount)
    } else null
}


fun setModOrder(mods: List<Mod>, modIndex: Int, position: Int) {
    if (position < 0) return
    val mod = mods.getOrNull(modIndex) ?: return
    mods.filterIndexed { i, _ -> i > modIndex }.forEach { it.loadOrder -= 1 }
    mods.filterIndexed { i, _ -> i >= position && i != modIndex }.forEach { it.loadOrder += 1 }
    mod.loadOrder = position
}