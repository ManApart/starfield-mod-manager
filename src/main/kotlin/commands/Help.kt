package commands

fun helpHelp() = ""
fun help(args: List<String> = listOf()) {
    val helpCommand = args.firstOrNull()?.let { getCommand(it) }
    when {
        args.isEmpty() -> printGeneralHelp()
        args.first() == "all" -> printDetailedHelp()
        helpCommand != null -> println(helpCommand.help())
        else -> printGeneralHelp()
    }
}

private fun printGeneralHelp() {
    println(CommandType.entries.filterNot { it == CommandType.HELP }.joinToString("\n") {
        "${it.cleanName}\n\t${it.description}"
    })
}

private fun printDetailedHelp() {
    println(CommandType.entries.filterNot { it == CommandType.HELP }.joinToString("\n") {
        "${it.cleanName}: ${it.description}\n\t${it.help().replace("\n", "\n\t")}"
    })
}