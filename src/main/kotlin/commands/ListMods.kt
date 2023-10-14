package commands

import toolState

fun listHelp(args: List<String>) = "List Mod details"

fun listMods(args: List<String> = listOf()) {
    val columns = listOf(
        Column("Id", 10 ),
        Column("Version", 10),
        Column("Enabled", 9),
        Column("Load Order", 12, true),
        Column("Index", 7, true),
        Column("Name", 22),
    )
    val data = toolState.mods.mapIndexed { i, mod ->
        with(mod) {
            val enabledCheck = if (enabled) "X" else " "
            val idClean = id?.toString() ?: "?"
            val versionClean = version ?: "?"
            listOf(i, enabledCheck, loadOrder, idClean, versionClean, name)
            mapOf(
                "Index" to i,
                "Enabled" to enabledCheck,
                "Load Order" to loadOrder,
                "Id" to idClean,
                "Version" to versionClean,
                "Name" to name,
            )
        }
    }
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