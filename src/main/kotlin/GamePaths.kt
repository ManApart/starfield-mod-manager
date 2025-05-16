import PathType.*

const val win64 = "/Binaries/Win64"
const val paks = "/Content/Paks/~mods"

enum class GamePath(vararg val examples: String) {
    GAME("/mnt/c/SteamLibrary/steamapps/common/Starfield", "/mnt/c/SteamLibrary/steamapps/common/Oblivion Remastered/OblivionRemastered/"),
    COMPAT_DATA("/mnt/c/SteamLibrary/steamapps/compatdata/2623190")
}

enum class PathType(val description: String) {
    GAME("Where the game is installed"),
    JAR("Location the mod manager is running from"),
    DATA(""),
    APP_DATA(""),
    INI(""),
    DATA_INI(""),
    UNREAL_INI(""),
    PLUGINS(""),
    WIN64("Where the unreal engine binary lives"),
    UE4SS_Mods(""),
    PAKS("Where the unreal engine mods live"),
    SAVES(""),
}

class GeneratedPath(
    val type: PathType,
    val aliases: List<String>,
    private val prefix: GamePath? = null,
    val suffix: String = "",
    private val genPath: (() -> String)? = null,
) {
    fun path() = genPath?.invoke() ?: (prefix?.let { gameConfig[it] } + suffix)
}

private val gamePath = GeneratedPath(GAME, listOf("gamepath", "gg")) { gameConfig[GamePath.GAME]!! }
private val jarPath = GeneratedPath(JAR, listOf("jarpath", "jar")) { "." }

fun starfieldPaths(): Map<PathType, GeneratedPath> {
    return listOf(
        gamePath,
        jarPath,
        GeneratedPath(DATA, listOf("data", "data")) { gameConfig[GamePath.GAME]!! + GameMode.STARFIELD.dataModPath },
        GeneratedPath(APP_DATA, listOf("appdatapath", "app"), GamePath.COMPAT_DATA, "/pfx/drive_c/users/steamuser/AppData/Local/Starfield"),
        GeneratedPath(INI, listOf("inipath", "ini"), GamePath.COMPAT_DATA, "/pfx/drive_c/users/steamuser/Documents/My Games/Starfield"),
        GeneratedPath(PLUGINS, listOf("plugins", "plugin"), GamePath.COMPAT_DATA, "/pfx/drive_c/users/steamuser/AppData/Local/Starfield/Plugins.txt"),
        GeneratedPath(SAVES, listOf("saves"), GamePath.COMPAT_DATA, "pfx/drive_c/users/steamuser/Documents/My Games/Starfield/Saves"),
    ).associateBy { it.type }
}

fun oblivionRemasteredPaths(): Map<PathType, GeneratedPath> {
    return listOf(
        gamePath,
        jarPath,
        GeneratedPath(DATA, listOf("data", "data")) { gameConfig[GamePath.GAME]!! + GameMode.OBLIVION_REMASTERED.dataModPath },
        GeneratedPath(DATA_INI, listOf("inipath", "ini"), GamePath.GAME, "/Content/Dev/ObvData/"),
        GeneratedPath(UNREAL_INI, listOf("unrealini", "engineini"), GamePath.COMPAT_DATA, "/pfx/drive_c/users/steamuser/Documents/My Games/Oblivion Remastered/Saved/Config/Windows"),
        GeneratedPath(PLUGINS, listOf("plugins", "plugin"), GamePath.GAME, "/Plugins.txt"),
        GeneratedPath(WIN64, listOf("win64"), GamePath.GAME, "/Binaries/Win64"),
        GeneratedPath(UE4SS_Mods, listOf("ue4ss"), GamePath.GAME, "/Binaries/Win64/ue4ss/Mods"),
        GeneratedPath(PAKS, listOf("paks", "unreal-mods"), GamePath.GAME, paks),
        GeneratedPath(SAVES, listOf("saves"), GamePath.COMPAT_DATA, "/pfx/drive_c/users/steamuser/Documents/My Games/Oblivion Remastered/Saved/SaveGames"),
    ).associateBy { it.type }
}
