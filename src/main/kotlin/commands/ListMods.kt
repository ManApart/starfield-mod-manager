package commands

import Mod
import toolData
import java.io.File

fun listHelp() = "List Mod details"

fun listMods(args: List<String> = listOf()) = display(toolData.mods. map { it to true })
fun display(mods: List<Pair<Mod, Boolean>>) {
    val columns = listOf(
        Column("Id", 10),
        Column("Version", 10),
        Column("Load Order", 12, true),
        Column("Staged", 9),
        Column("Enabled", 9),
        Column("Index", 7, true),
        Column("Name", 22),
    )
    val data = mods.mapIndexed { i, (mod, displayed) ->
        with(mod) {
            val enabledCheck = if (enabled) "X" else " "
            val idClean = id?.toString() ?: "?"
            val versionClean = when {
                version != null && latestVersion != null && version != latestVersion -> "$version -> $latestVersion"
                version != null -> version!!
                latestVersion != null -> "? -> $latestVersion"
                else -> "?"
            }
            val staged = if (File(filePath).exists()) "X" else " "
            mapOf(
                "Index" to i,
                "Staged" to staged,
                "Enabled" to enabledCheck,
                "Load Order" to loadOrder,
                "Id" to idClean,
                "Version" to versionClean,
                "Name" to name,
            )
        } to displayed
    }.filter { it.second }.map { it.first }
    Table(columns, data).print()
}

private data class Table(val columns: List<Column>, val data: List<Map<String, Any>>) {
    fun print() {
        val colFormat = columns.joinToString("") { "%-${it.size}s" }
        val rowFormat = columns.joinToString("") {
            val type = if (it.isNumber) "d" else "s"
            "%-${it.size}$type"
        }

        val headerValues = columns.map { it.header }.toTypedArray()
        System.out.printf("$colFormat\n", *headerValues)
        data.forEach { row ->
            val dataValues = columns.map { row[it.header] ?: "" }.toTypedArray()
            System.out.printf("$rowFormat\n", *dataValues)
        }
    }
}

private data class Column(val header: String, val size: Int, val isNumber: Boolean = false)