package commands

import Column
import Mod
import Table
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
    val columns = listOf(
        Column("Id", 10),
        Column("Version", 20),
        Column("Load Order", 12, true),
        Column("Staged", 9),
        Column("Enabled", 9),
        Column("Category", 20),
        Column("Index", 7, true),
        Column("Name", 22),
    )
    val data = mods.mapIndexed { i, (mod, displayed) ->
        with(mod) {
            val enabledCheck = if (enabled) "X" else " "
            val idClean = id?.toString() ?: "?"
            val versionClean = when {
                version != null && latestVersion != null && version != latestVersion -> "${version.truncate()} -> ${latestVersion.truncate()}"
                version != null -> version.truncate(10)
                latestVersion != null -> "? -> ${latestVersion.truncate(10)}"
                else -> "?"
            }
            val staged = if (File(filePath).exists()) "X" else " "
            val category = category() ?: "?"
            mapOf(
                "Index" to i,
                "Staged" to staged,
                "Enabled" to enabledCheck,
                "Load Order" to loadOrder,
                "Id" to idClean,
                "Version" to versionClean,
                "Category" to category,
                "Name" to name,
            )
        } to displayed
    }.filter { it.second }.map { it.first }
    Table(columns, data).print()
}

