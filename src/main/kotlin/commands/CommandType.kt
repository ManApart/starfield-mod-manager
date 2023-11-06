package commands

enum class Category { ADD, DEPLOY, VIEW, EDIT, OPEN, UPDATE, CONFIG }

enum class CommandType(
    val description: String,
    val category: Category,
    val help: () -> String,
    val apply: (List<String>) -> Unit,
    vararg val aliases: String = arrayOf(),
) {
    ADD("Add a new mod", Category.ADD, ::addModHelp, ::addMod),
    CONFIG("Edit Configuration", Category.CONFIG, ::configHelp, ::config),
    ENABLE("Enable Mod", Category.DEPLOY, ::enableHelp, ::enable),
    DISABLE("Disable Mod", Category.DEPLOY, ::enableHelp, ::disable),
    ENDORSE("Endorse Mod", Category.EDIT, ::endorseHelp, ::endorse),
    ABSTAIN("Abstain from endorsing Mod", Category.EDIT, ::endorseHelp, ::abstain),
    DEPLOY("Deploy enabled mods", Category.DEPLOY, ::deployHelp, ::deploy),
    HELP("Explain commands", Category.CONFIG, ::helpHelp, ::help),
    FETCH("Fetch Mod Data", Category.ADD, ::fetchHelp, ::fetchMod),
    LIST("List Mods", Category.VIEW, ::listHelp, ::listMods, "ls"),
    DETAIL("View all details of mod", Category.VIEW, ::detailHelp, ::detailMod),
    ORDER("Change Load Order", Category.DEPLOY, ::orderHelp, ::order),
    OPEN("Open mod on web", Category.OPEN, ::openHelp, ::open),
    LOCAL("Open local mod folder", Category.OPEN, ::openHelp, ::local),
    GAME_PATH("Open game folder", Category.OPEN, ::openHelp, ::openGamePath, "gamepath"),
    APPDATA_PATH("Open ini path folder", Category.OPEN, ::openHelp, ::openAppDataPath, "appdatapath"),
    INI_PATH("Open ini path folder", Category.OPEN, ::openHelp, ::openIniPath, "inipath"),
    JAR_PATH("Open jar path folder", Category.OPEN, ::openHelp, ::openJarPath, "jarpath"),
    PURGE("Purge all sym links", Category.DEPLOY, ::purgeHelp, ::purge),
    MOD("Update a mod",  Category.EDIT, ::changeHelp, ::changeMod),
    PROFILE("Create and use local mod lists", Category.DEPLOY, ::profileHelp, ::profile),
    RENAME("Rename a mod", Category.EDIT, ::changeHelp, ::moveMod, "mv"),
    REFRESH("Refresh mods by id", Category.UPDATE, ::refreshHelp, ::refresh),
    UPDATE("Check for newer versions", Category.UPDATE, ::updateHelp, ::update),
    VERSION("See current and new version for mod", Category.UPDATE, ::versionHelp, ::version),
    UPGRADE("Upgrade to newer versions", Category.UPDATE, ::upgradeHelp, ::upgrade),
    REMOVE("Delete a mod", Category.ADD, ::removeHelp, ::remove, "rm"),
    SEARCH("Search Mods", Category.VIEW, ::searchHelp, ::searchMods, "grep", "awk"),
    FILTER("Apply a filter to Mods", Category.VIEW, ::filterHelp, ::filterMods),
    SORT("Sort Mods", Category.VIEW, ::sortHelp, ::sortMods),
    VALIDATE("List issues with mods", Category.DEPLOY, ::validateHelp, ::validateMods),
    START("Launch Starfield", Category.CONFIG, ::startGameHelp, ::startGame, "game"),
    EXIT(
        "Exit Program", Category.CONFIG,
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