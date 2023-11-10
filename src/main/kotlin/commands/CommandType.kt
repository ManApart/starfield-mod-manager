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
    ADD("Add a new mod", Category.ADD, addModHelp, addModUsage, ::addMod),
    CONFIG("Edit Configuration", Category.CONFIG, configHelp, configUsage, ::config),
    ENABLE("Enable Mod", Category.DEPLOY, enableHelp, enableUsage, ::enable),
    DISABLE("Disable Mod", Category.DEPLOY, enableHelp, enableUsage, ::disable),
    ENDORSE("Endorse Mod", Category.EDIT, endorseHelp, endorseUsage, ::endorse),
    ABSTAIN("Abstain from endorsing Mod", Category.EDIT, endorseHelp, endorseUsage, ::abstain),
    DEPLOY("Deploy enabled mods", Category.DEPLOY, deployHelp, deployUsage, ::deploy),
    HELP("Explain commands", Category.CONFIG, "", "", ::help),
    FETCH("Fetch Mod Data", Category.ADD, fetchHelp, fetchUsage, ::fetchMod),
    LIST("List Mods", Category.VIEW, listHelp, listUsage, ::listMods, "ls"),
    DETAIL("View all details of mod", Category.VIEW, detailHelp, detailUsage, ::detailMod),
    ORDER("Change Load Order", Category.DEPLOY, orderHelp, orderUsage, ::order),
    OPEN("Open mod on web", Category.OPEN, openHelp, openUsage, ::open),
    LOCAL("Open local mod folder", Category.OPEN, openHelp, openUsage, ::local),
    GAME_PATH("Open game folder", Category.OPEN, openHelp, openUsage, ::openGamePath, "gamepath"),
    APPDATA_PATH("Open ini path folder", Category.OPEN, openHelp, openUsage, ::openAppDataPath, "appdatapath"),
    INI_PATH("Open ini path folder", Category.OPEN, openHelp, openUsage, ::openIniPath, "inipath"),
    JAR_PATH("Open jar path folder", Category.OPEN, openHelp, openUsage, ::openJarPath, "jarpath"),
    PURGE("Purge all sym links", Category.DEPLOY, purgeHelp, purgeUsage, ::purge),
    MOD("Update a mod", Category.EDIT, changeHelp, changeUsage, ::changeMod),
    PROFILE("Create and use local mod lists", Category.DEPLOY, profileHelp, profileUsage, ::profile),
    RENAME("Rename a mod", Category.EDIT, changeHelp, changeUsage, ::moveMod, "mv"),
    REFRESH("Refresh mods by id", Category.UPDATE, refreshHelp, refreshUsage, ::refresh),
    UPDATE("Check for newer versions", Category.UPDATE, updateHelp, updateUsage, ::update),
    VERSION("See current and new version for mod", Category.UPDATE, versionHelp, versionUsage, ::version),
    UPGRADE("Upgrade to newer versions", Category.UPDATE, upgradeHelp, upgradeUsage, ::upgrade),
    REMOVE("Delete a mod", Category.ADD, removeHelp, removeUsage, ::remove, "rm"),
    SEARCH("Search Mods", Category.VIEW, searchHelp, searchModsUsage, ::searchMods, "grep", "awk"),
    FIND("Find file", Category.VIEW, findHelp, findUsage, ::find),
    FILTER("Apply a filter to Mods", Category.VIEW, filterHelp, filterUsage, ::filterMods),
    SORT("Sort Mods", Category.VIEW, sortHelp, sortUsage, ::sortMods),
    VALIDATE("List issues with mods", Category.DEPLOY, validateHelp, validateUsage, ::validateMods),
    START("Launch Starfield", Category.CONFIG, startGameHelp, startGameUsage, ::startGame, "game"),
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