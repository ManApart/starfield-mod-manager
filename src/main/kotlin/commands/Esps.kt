package commands

import Column
import Mod
import Table
import clearConsole
import toolData
import java.io.File

val espTypes = listOf("esp", "esm", "esl")

val espDescription = """
    Plugins with a higher load order are loaded later, and override plugins loaded earlier. Given plugin A has an order 5 and plugin B has an order of 1, then A will load AFTER B, and A's records will be used instead of B's in any conflicts. 
    In these examples the first number is the mod index and the second is the sort order you want
    esp ls - view load order of plugins
    esp 1 set 4 - sets mod with index 1 to load order 4. Any plugins with a higher number for load order have their number increased
    esp 1 later - sets the plugin to load after the next plugin (may shift the order number more than one as it shifts until it's after the next plugin).
    esp 1 later 2 - same as above but shifts down by two plugins
    For general mod order, see order command. The same order number is used for both commands.
""".trimIndent()

val espUsage = """
    esp ls
    esp refresh
    esp 1 first
    esp 1 last
    esp 1 sooner 5
    esp 1 later
    esp 1 set 4
""".trimIndent()

fun esp(args: List<String>) {
    val arguments = parseArgs(args)
        ?: when {
            args.size == 1 && args.last() == "ls" -> {
                val mods = getModsWithPlugins()
                if (mods.isEmpty()){
                    println("Found no plugins. Consider running esp refresh")
                } else {
                display(getModsWithPlugins())
                }
                return
            }
            args.size == 1 && args.last() == "refresh" -> {
                refreshPlugins()
                println("Refreshed Plugins")
                return
            }
            else -> {
                println(orderDescription)
                return
            }
        }
    with(arguments) {
        val mods = getModsWithPlugins()
        when {
            subCommand == "first" -> setModOrder(toolData.mods, index, 0)
            subCommand == "last" -> setModOrder(toolData.mods, index, toolData.nextLoadOrder())
            subCommand == "set" && amount != null -> setModOrder(toolData.mods, index, amount)
            subCommand == "sooner" && amount != null -> setModOrder(
                toolData.mods,
                index,
                nextPluginLoadOrder(mods, index, -amount)
            )

            subCommand == "later" && amount != null -> setModOrder(
                toolData.mods,
                index,
                nextPluginLoadOrder(mods, index, amount)
            )

            subCommand == "sooner" -> setModOrder(toolData.mods, index, nextPluginLoadOrder(mods, index, -1))
            subCommand == "later" -> setModOrder(toolData.mods, index, nextPluginLoadOrder(mods, index, 1))
            else -> println("Unknown subCommand: ")
        }
        display(getModsWithPlugins())
    }
}

private fun refreshPlugins() {
    toolData.mods.forEach { it.refreshPlugins() }
}

fun Mod.refreshPlugins() {
    plugins = getModFiles().filter { it.extension.lowercase() in espTypes }.map { it.name }
}

private fun getModsWithPlugins(): Map<Mod, List<String>> {
    return toolData.mods
        .filter { it.enabled }.sortedBy { it.loadOrder }
        .associateWith { mod -> mod.plugins }
        .filter { (_, files) -> files.isNotEmpty() }
}

private fun nextPluginLoadOrder(mods: Map<Mod, List<String>>, modIndexToShift: Int, pluginShiftAmount: Int): Int {
    return nextPlugin(mods, modIndexToShift, pluginShiftAmount)?.loadOrder ?: 0
}

private fun nextPluginIndex(mods: Map<Mod, List<String>>, modIndexToShift: Int, pluginShiftAmount: Int): Int {
    return nextPlugin(mods, modIndexToShift, pluginShiftAmount)?.index ?: -1
}

private fun nextPlugin(mods: Map<Mod, List<String>>, modIndexToShift: Int, pluginShiftAmount: Int): Mod? {
    val sortedMods = mods.keys.toList().sortedBy { it.loadOrder }
    val i = sortedMods.firstOrNull { it.index == modIndexToShift }?.let { sortedMods.indexOf(it) } ?: -1
    return sortedMods.getOrNull(i + pluginShiftAmount)
}

private fun display(mods: Map<Mod, List<String>>) {
    val modFiles = mods.flatMap { (mod, files) -> files.map { mod to it } }
    val espWidth = modFiles.maxOf { it.second.length } + 5

    clearConsole()
    val columns = listOf(
        Column("Id", 7),
        Column("Load", 7, true),
        Column("Index", 7, true),
        Column("Esp", espWidth),
        Column("Mod", 22),
    )
    val data = modFiles.map { (mod, plugin) ->
        with(mod) {
            val idClean = id?.toString() ?: "?"
            mapOf(
                "Index" to mod.index,
                "Load" to loadOrder,
                "Id" to idClean,
                "Esp" to plugin,
                "Mod" to name,
            )
        }
    }
    Table(columns, data).print()
}