package commands

enum class Category { ADD, DEPLOY, VIEW, EDIT, OPEN, UPDATE, CONFIG }

enum class CommandType(
    val summary: String,
    val category: Category,
    val description: String,
    val usage: String,
    val apply: (List<String>) -> Unit,
    vararg val aliases: String = arrayOf(),
) {
    ADD("Add a new mod", Category.ADD, addModDescription, addModUsage, ::addMod),
    CONFIG("Edit Configuration", Category.CONFIG, configDescription, configUsage, ::config),
    ENABLE("Enable Mod", Category.DEPLOY, enableDescription, enableUsage, ::enable),
    DISABLE("Disable Mod", Category.DEPLOY, enableDescription, disableUsage, ::disable),
    ENDORSE("Endorse Mod", Category.EDIT, endorseDescription, endorseUsage, ::endorse),
    ABSTAIN("Abstain from endorsing Mod", Category.EDIT, endorseDescription, "abstain <index>", ::abstain),
    DEPLOY("Deploy enabled mods", Category.DEPLOY, deployDescription, deployUsage, ::deploy),
    HELP("Explain commands", Category.CONFIG, helpDescription, helpUsage, ::help),
    FETCH("Fetch Mod Data", Category.ADD, fetchDescription, fetchUsage, ::fetchMod),
    LIST("List Mods", Category.VIEW, listDescription, listUsage, ::listMods, "ls"),
    DETAIL("View all details of mod", Category.VIEW, detailDescription, detailUsage, ::detailMod),
    ORDER("Change Load Order", Category.DEPLOY, orderDescription, orderUsage, ::order),
    ESP("Change Load Order for plugins", Category.DEPLOY, espDescription, espUsage, ::esp, "esps", "plugin", "plugins"),
    OPEN("Open mod on web", Category.OPEN, openDescription, "open <index>", ::open),
    LOCAL("Open local mod folder", Category.OPEN, openDescription, "local <index>", ::local),
    GAME_PATH("Open game folder", Category.OPEN, openDescription, "", ::openGamePath, "gamepath"),
    APPDATA_PATH("Open ini path folder", Category.OPEN, openDescription, "", ::openAppDataPath, "appdatapath"),
    INI_PATH("Open ini path folder", Category.OPEN, openDescription, "", ::openIniPath, "inipath"),
    JAR_PATH("Open jar path folder", Category.OPEN, openDescription, "", ::openJarPath, "jarpath"),
    PURGE("Purge all sym links", Category.DEPLOY, purgeDescription, purgeUsage, ::purge),
    MOD("Update a mod", Category.EDIT, changeHelp, changeDescription, ::changeMod),
    PROFILE("Create and use local mod lists", Category.DEPLOY, profileDescription, profileUsage, ::profile),
    RENAME("Rename a mod", Category.EDIT, renameHelp, "rename <index> <new name>", ::moveMod, "mv"),
    REFRESH("Refresh mods by id", Category.UPDATE, refreshDescription, refreshUsage, ::refresh),
    UPDATE("Check for newer versions", Category.UPDATE, updateDescription, updateUsage, ::update),
    VERSION("See current and new version for mod", Category.UPDATE, versionDescription, versionUsage, ::version),
    UPGRADE("Upgrade to newer versions", Category.UPDATE, upgradeDescription, upgradeUsage, ::upgrade),
    REMOVE("Delete a mod", Category.ADD, removeHelp, removeDescription, ::remove, "rm"),
    SEARCH("Search Mods", Category.VIEW, searchDescription, searchModsUsage, ::searchMods, "grep", "awk"),
    FIND("Find file", Category.VIEW, findDescription, findUsage, ::find),
    FILTER("Apply a filter to Mods", Category.VIEW, filterDescription, filterUsage, ::filterMods),
    SORT("Sort Mods", Category.VIEW, sortDescription, sortUsage, ::sortMods),
    VALIDATE("List issues with mods", Category.DEPLOY, validateDescription, validateUsage, ::validateMods),
    START("Launch Starfield", Category.CONFIG, startGameDescription, startGameUsage, ::startGame, "game"),
    EXIT(
        "Exit Program", Category.CONFIG,
        "Exit the process",
        "Exit the process",
        { kotlin.system.exitProcess(0) }
    ),
    ;

    val cleanName = name.lowercase().replace("_", "-")
}

fun getCommand(commandString: String) =
    CommandType.entries.firstOrNull {
        commandString == it.cleanName || it.aliases.contains(commandString)
    }