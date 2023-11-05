package commands

import FOLDER
import Column
import ENABLED
import Mod
import THUMBS_DOWN
import THUMBS_UP
import Table
import UPDATE
import clearConsole
import toolData
import truncate
import java.io.File

fun listHelp() = """
    List Mod details
    list 10 30 - List 30 mods, starting with the 10th mod
""".trimIndent()

fun listMods(args: List<String> = listOf()) {
    val ranges = args.mapNotNull { it.toIntOrNull() }
    when {
        ranges.isNotEmpty() -> displayAmount(ranges)
        else -> display(toolData.mods.map { it to it.show })
    }
}

private fun displayAmount(ranges: List<Int>) {
    val start = ranges.first()
    val amount = ranges.getOrNull(1) ?: 20
    var shownCount = 0
    val shownMods = toolData.mods.mapIndexed { i, mod ->
        val shown = if (mod.show && i >= start && shownCount < amount) {
            shownCount++
            true
        } else false
        mod to shown
    }
    display(shownMods)
}

fun display(mods: List<Pair<Mod, Boolean>>) {
    clearConsole()
    val columns = listOf(
        Column("Id", 7),
        Column("Version", 12),
        Column("Load", 7, true),
        Column("Staged", 8),
        Column("Enabled", 9),
        Column("Endorsed", 10),
        Column("Category", 20),
        Column("Index", 7, true),
        Column("Name", 22),
    )
    val data = mods.mapIndexed { i, (mod, displayed) ->
        with(mod) {
            val enabledCheck = if (enabled) ENABLED else " "
            val endorsedCheck = when(endorsed){
                true -> THUMBS_UP
                false -> THUMBS_DOWN
                else -> " "
            }
            val idClean = id?.toString() ?: "?"
            val versionClean = when {
                version != null && latestVersion != null && version != latestVersion -> "$UPDATE${version.truncate()}"
                version != null ->"  "+ version.truncate(8)
                latestVersion != null -> "$UPDATE?"
                else -> "  ?"
            }
            val staged = if (File(filePath).exists()) FOLDER else " "
            val category = category() ?: "?"
            mapOf(
                "Index" to i,
                "Staged" to staged,
                "Enabled" to enabledCheck,
                "Endorsed" to endorsedCheck,
                "Load" to loadOrder,
                "Id" to idClean,
                "Version" to versionClean,
                "Category" to category,
                "Name" to name,
            )
        } to displayed
    }.filter { it.second }.map { it.first }
    Table(columns, data).print()
}

