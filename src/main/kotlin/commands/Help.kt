package commands

import Column
import Table
import cyan

val helpHelp = ""
fun help(args: List<String> = listOf()) {
    val helpCommand = args.firstOrNull()?.let { getCommand(it) }
    when {
        args.isEmpty() -> printGeneralHelp()
        args.first() == "all" -> printDetailedHelp()
        helpCommand != null -> println(helpCommand.usage)
        else -> printGeneralHelp()
    }
}

private fun printGeneralHelp() {
    CommandType.entries
        .filterNot { it == CommandType.HELP }
        .groupBy { it.category }.entries
        .forEach { (category, commands) ->
            println(cyan(category.name.lowercase().capitalize()))
            val columns = listOf(
                Column("Command", 10),
                Column("Summary", 30),
            )
            val data = commands.map {
                mapOf(
                    "Command" to it.cleanName,
                    "Summary" to it.summary,
                )
            }
            Table(columns, data).print(false)
            println()
        }
}

private fun printDetailedHelp() {
    println(CommandType.entries.filterNot { it == CommandType.HELP }.joinToString("\n") {
        "${it.cleanName}: ${it.summary}\n\t${it.usage.replace("\n", "\n\t")}"
    })
}