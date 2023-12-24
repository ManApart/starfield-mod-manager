package commands

import Mod
import toolData

val orderDescription = """
    Mods with a higher load order are loaded later, and override mods loaded earlier. Given mod A has an order 5 and mod B has an order of 1, then A will load AFTER B, and A's files will be used instead of B's in any file conflicts. 
    In these examples the first number is the mod index and the second is the sort order you want
    order 1 set 4 - sets mod with index 1 to load order 4. Any mods with a higher number for load order have their number increased
    order 1 - view any conflicts mod index 1 has with any other mods
""".trimIndent()

val orderUsage = """
    order 1
    order 1 first
    order 1 last
    order 1 sooner 5
    order 1 later
    order 1 set 4
""".trimIndent()

private data class Args(val index: Int, val subCommand: String, val amount: Int?)

fun order(args: List<String>) {
    val arguments = parseArgs(args)
        ?: if (args.size == 1 && args.last().toIntOrNull() != null) {
            toolData.mods.getOrNull(args.last().toInt())?.let { showOverrides(it) }
            return
        } else {
            println(orderDescription)
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
    val oldOrder = mod.loadOrder
    mods.filter { it.loadOrder > oldOrder }.forEach { it.loadOrder -= 1 }
    mods.filter { it.loadOrder >= position }.forEach { it.loadOrder += 1 }
    mod.loadOrder = position
}