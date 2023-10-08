package commands

enum class Command(
    val help: () -> String,
    val apply: (List<String>) -> Unit,
    vararg val aliases: String = arrayOf(),
) {
    HELP(::helpHelp, ::help),
    LIST(::listHelp, ::list, "ls"),
    EXIT(
        { "Exit the process" },
        { kotlin.system.exitProcess(0) }
    ),
}

