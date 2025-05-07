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
    ADD("Add a new mod", Category.ADD, addModDescription, addModUsage, ::addMod, "a"),
    CONFIG("Edit Configuration", Category.CONFIG, configDescription, configUsage, ::config, "c"),
    CREATION("Manage Creation", Category.ADD, creationDescription, creationUsage, ::creation, "creations", "cr"),
    EXTERNAL("Manage External Plugins", Category.ADD, externalModDescription, externalModUsage, ::externalMod, "ex"),
    ENABLE("Enable Mod", Category.DEPLOY, enableDescription, enableUsage, ::enable, "e"),
    DISABLE("Disable Mod", Category.DEPLOY, enableDescription, disableUsage, ::disable, "x"),
    ENDORSE("Endorse Mod", Category.EDIT, endorseDescription, endorseUsage, ::endorse, "en"),
    ABSTAIN("Abstain from endorsing Mod", Category.EDIT, endorseDescription, "abstain <index>", ::abstain),
    DEPLOY("Deploy enabled mods", Category.DEPLOY, deployDescription, deployUsage, ::deploy, "d"),
    HELP("Explain commands", Category.CONFIG, helpDescription, helpUsage, ::help, "h"),
    IMPORT("Import from viewer", Category.ADD, importDescription, importUsage, ::importData, "i"),
    FETCH("Fetch Mod Data", Category.ADD, fetchDescription, fetchUsage, ::fetchMod, "f"),
    LIST("List Mods", Category.VIEW, listDescription, listUsage, ::listMods, "ls"),
    DETAIL("View all details of mod", Category.VIEW, detailDescription, detailUsage, ::detailMod, "dd"),
    ORDER("Change Load Order", Category.DEPLOY, orderDescription, orderUsage, ::order, "or"),
    ESP("Change Load Order for plugins", Category.DEPLOY, espDescription, espUsage, ::esp, "esps"),
    OPEN("Open mod on web", Category.OPEN, openDescription, "open <index>", ::open, "o"),
    LOCAL("Open local mod folder", Category.OPEN, openDescription, "local <index> *<cli>", ::local, "l"),
    CLI("Open local mod folder in terminal", Category.OPEN, openDescription, "cli <index>", ::cli, "terminal", "term"),
    GAME_PATH("Open game folder", Category.OPEN, openDescription, ::openGamePath, "gamepath", "gg"),
    APPDATA_PATH("Open appdata path folder", Category.OPEN, openDescription, ::openAppDataPath, "appdatapath", "app"),
    MODE("Switch Games", Category.CONFIG, modeDescription, modeUsage, ::gameMode),
    INI_PATH("Open ini path folder", Category.OPEN, openDescription, ::openIniPath, "inipath", "ini"),
    PLUGINS("Open plugins file", Category.OPEN, openDescription, ::openPluginsTxt, "plugin"),
    JAR_PATH("Open jar path folder", Category.OPEN, openDescription, ::openJarPath, "jarpath", "jar"),
    MANUAL("Open website manual", Category.OPEN, openDescription, ::openManual, "man"),
    SITE("Open website", Category.OPEN, openDescription, ::openSite),
    SOURCE("Open app source", Category.OPEN, openDescription, ::openSource, "git", "github"),
    NEXUS("Open app nexus page", Category.OPEN, openDescription, ::openNexus),
    PURGE("Purge all sym links", Category.DEPLOY, purgeDescription, purgeUsage, ::purge),
    MOD("Update a mod", Category.EDIT, changeDescription, changeUsage, ::changeMod, "m"),
    TAG("Edit tags on a mod", Category.EDIT, tagDescription, tagUsage, ::tag, "t"),
    PROFILE("Create and use local mod lists", Category.DEPLOY, profileDescription, profileUsage, ::profile, "p", "prof"),
    RENAME("Rename a mod", Category.EDIT, renameHelp, "rename <index> <new name>", ::moveMod, "mv"),
    REFRESH("Refresh mods by id", Category.UPDATE, refreshDescription, refreshUsage, ::refresh, "rr"),
    UPDATE("Check for newer versions", Category.UPDATE, updateDescription, updateUsage, ::update, "up"),
    VERSION("See current and new version for mod", Category.UPDATE, versionDescription, versionUsage, ::version, "vv"),
    UPGRADE("Upgrade to newer versions", Category.UPDATE, upgradeDescription, upgradeUsage, ::upgrade, "ug"),
    REMOVE("Delete a mod", Category.ADD, removeHelp, removeDescription, ::remove, "rm"),
    SEARCH("Search Mods", Category.VIEW, searchDescription, searchModsUsage, ::searchMods, "grep", "awk"),
    FIND("Find file", Category.VIEW, findDescription, findUsage, ::find, "ff"),
    FILTER("Apply a filter to Mods", Category.VIEW, filterDescription, filterUsage, ::filterMods, "fl"),
    SORT("Sort Mods", Category.VIEW, sortDescription, sortUsage, ::sortMods, "s"),
    VALIDATE("List issues with mods", Category.DEPLOY, validateDescription, validateUsage, ::validateMods, "v"),
    START("Launch Starfield", Category.CONFIG, startGameDescription, startGameUsage, ::startGame, "game"),
    EXIT(
        "Exit Program", Category.CONFIG,
        "Exit the process",
        "Exit the process",
        { kotlin.system.exitProcess(0) }
    ),
    ;

    constructor(
        summary: String,
        category: Category,
        description: String,
        apply: (List<String>) -> Unit,
        vararg aliases: String = arrayOf()
    ) : this(summary, category, description, summary, apply, aliases = aliases)

    val cleanName = name.lowercase().replace("_", "-")
}

fun getCommand(commandString: String) =
    CommandType.entries.firstOrNull {
        commandString == it.cleanName || it.aliases.contains(commandString)
    }
