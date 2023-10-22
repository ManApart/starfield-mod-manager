package commands

import Column
import Mod
import Table
import toolConfig
import toolData
import truncate
import java.io.File
import kotlin.math.min

fun listHelp() = "List Mod details"

fun listMods(args: List<String> = listOf()) = display(toolData.mods. map { it to it.show })
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

