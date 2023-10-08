import commands.CommandType
import commands.getCommand

fun readLine(line: String?) {
    val parts = line?.parseArgs() ?: return
    if (parts.isEmpty()) {
        CommandType.HELP.help(listOf())
    } else {
        val commandString = parts.first().lowercase()
        val args = parts.subList(1, parts.size)

        val command = getCommand(commandString)
        if (command != null) {
            command.apply(args)
        } else {
            println("Unknown Command $commandString")
        }
    }
}

fun String.parseArgs(): List<String>{
    return split("\"").chunked(2).flatMap { parts ->
        val second = if(parts.size == 2) listOf(parts.last()) else listOf()
        parts.first().trim().split(" ") + second
    }.filter { it.isNotBlank() }
}