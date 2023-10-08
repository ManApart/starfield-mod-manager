package commands

enum class Command(val apply: (List<String>) -> Unit) {
    HELP(::help),
    EXIT({ kotlin.system.exitProcess(0)}),
}
