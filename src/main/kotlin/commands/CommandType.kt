package commands

enum class CommandType(
    val description: String,
    val help: () -> String,
    val apply: (List<String>) -> Unit,
    vararg val aliases: String = arrayOf(),
) {
    ADD("Add a new mod", ::addModHelp, ::addMod),
    CONFIG("Edit Configuration", ::configHelp, ::config),
    ENABLE("Enable Mod", ::enableHelp, ::enable),
    DISABLE("Enable Mod", ::enableHelp, ::disable),
    DEPLOY("Deploy enabled mods", ::deployHelp, ::deploy),
    HELP("Explain commands", ::helpHelp, ::help),
    FETCH("Fetch Mod Data", ::fetchHelp, ::fetchMod),
    LIST("List Mods", ::listHelp, ::listMods, "ls"),
    DETAIL("View all details of mod", ::detailHelp, ::detailMod),
    ORDER("Change Load Order", ::orderHelp, ::order),
    OPEN("Open mod on web", ::openHelp, ::open),
    LOCAL("Open local mod folder", ::openHelp, ::local),
    PURGE("Purge all sym links", ::purgeHelp, ::purge),
    MOD("Update a mod", ::changeHelp, ::changeMod),
    RENAME("Rename a mod", ::changeHelp, ::moveMod, "mv"),
    REFRESH("Refresh mods by id", ::refreshHelp, ::refresh),
    UPDATE("Check for newer versions", ::updateHelp, ::update),
    UPGRADE("Upgrade to newer versions", ::upgradeHelp, ::upgrade),
    REMOVE("Delete a mod", ::removeHelp, ::remove, "rm"),
    SEARCH("Search Mods", ::searchHelp, ::searchMods, "grep", "awk"),
    SORT("Sort Mods", ::sortHelp, ::sortMods),
    VALIDATE("List issues with mods", ::validateHelp, ::validateMods),
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