const val win64 = "/Binaries/Win64"
const val paks = "/Content/Paks/~mods"
const val obvIni = "/Content/Dev/ObvData/"
//Paks, ue4ss, unreal engine ini, etc


enum class GamePath(vararg val examples: String) {
    GAME("/mnt/c/SteamLibrary/steamapps/common/Starfield", "/mnt/c/SteamLibrary/steamapps/common/Oblivion Remastered/OblivionRemastered/"),
    APP_DATA("/mnt/c/SteamLibrary/steamapps/compatdata/1716740/pfx/drive_c/users/steamuser/AppData/Local/Starfield"),
    INI("/mnt/c/SteamLibrary/steamapps/compatdata/1716740/pfx/drive_c/users/steamuser/Documents/My Games/Starfield"),
    COMPAT_DATA("/mnt/c/SteamLibrary/steamapps/compatdata/2623190/pfx/drive_c/users/steamuser/Documents/My Games/Oblivion Remastered")
}

class GeneratedPath(
    val name: String,
    val aliases: List<String>,
    val description: String,
    val path: () -> String,
)

private val gamePath = GeneratedPath("Game Path", listOf("gamepath", "gg"), "Where the game is installed") { gameConfig[GamePath.GAME]!! }
private val jarPath = GeneratedPath("Jar Path", listOf("jarpath", "jar"), "Location the mod manager is running from") { "." }

fun starfieldPaths(): Map<String, GeneratedPath> {
    return listOf(
        gamePath,
        jarPath,
        GeneratedPath("AppData Path", listOf("appdatapath", "app"), "") { gameConfig[GamePath.APP_DATA]!! },
        GeneratedPath("Ini Path", listOf("inipath", "ini"), "") { gameConfig[GamePath.INI]!! },
        GeneratedPath("Plugins", listOf("plugins", "plugin"), "") { gameConfig[GamePath.APP_DATA]!! + "/Plugins.txt" },
    ).associateBy { it.name }
}

fun oblivionRemasteredPaths(): Map<String, GeneratedPath> {
    return listOf(
        gamePath,
        jarPath,
        GeneratedPath("Plugins", listOf("plugins", "plugin"), "") { gameConfig[GamePath.GAME]!! + "/Plugins.txt" },
        GeneratedPath("Win64", listOf("win64"), "Where the unreal engine binary lives") { gameConfig[GamePath.GAME]!! + "/Binaries/Win64" },
    ).associateBy { it.name }
}
