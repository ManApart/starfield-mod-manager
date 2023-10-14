package commands

interface Command {
    val aliases: List<String>
    val description: String
    fun help(args: List<String>)
    fun apply(args: List<String>)
}

enum class CommandType(
    val description: String,
    val help: (List<String>) -> String,
    val apply: (List<String>) -> Unit,
    vararg val aliases: String = arrayOf(),
) {
    ADD("Add a new mod", ::addModHelp, ::addMod),
    CONFIG("Edit Configuration", ::configHelp, ::config),
    ENABLE("Enable Mod", ::enableHelp, ::enable),
    DISABLE("Enable Mod", ::enableHelp, ::disable),
    DEPLOY("Deploy enabled mods", ::deployHelp, ::deploy),
    ORDER("Change Load Order", ::orderHelp, ::order),
    OPEN("Open mod on web", ::openHelp, ::open),
    LOCAL("Open local mod folder", ::openHelp, ::local),
    PURGE("Purge all sym links", ::purgeHelp, ::purge),
    HELP("Explain commands", ::helpHelp, ::help),
    LIST("List Mods", ::listHelp, ::list, "ls"),
    EXIT(
        "Exit Program",
        { "Exit the process" },
        { kotlin.system.exitProcess(0) }
    ),
    ;

    val cleanName = name.lowercase().replace("_", "-")
}

fun getCommand(commandString: String) =
    CommandType.entries.firstOrNull {
        commandString == it.cleanName || it.aliases.contains(commandString)
    }