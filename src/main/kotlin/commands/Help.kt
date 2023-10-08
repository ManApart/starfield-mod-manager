package commands

fun helpHelp() = ""
fun help(args: List<String> = listOf()) {
    //TODO - help per specific command
    println(Command.entries.filterNot { it == Command.HELP }.joinToString("\n") {
        "${it.name.lowercase()}: ${it.help()}"
    })
}