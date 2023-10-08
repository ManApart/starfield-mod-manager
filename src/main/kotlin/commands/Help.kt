package commands

fun helpHelp(args: List<String>) = ""
fun help(args: List<String> = listOf()) {
    if (args.isEmpty()) {
        printGeneralHelp()
    } else {
        val helpCommand = getCommand(args.first())
        if (helpCommand != null) {
            println(helpCommand.help(args.subList(1, args.size)))
        } else {
            printGeneralHelp()
        }
    }
}

private fun printGeneralHelp() {
    println(CommandType.entries.filterNot { it == CommandType.HELP }.joinToString("\n") {
        "${it.cleanName}: ${it.description}"
    })
}