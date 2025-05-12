enum class GameMode(
    val displayName: String,
    val configPath: String,
    val dataJsonPath: String,
    val dataModPath: String,
    val modFolder: String,
    val urlName: String,
) {
    STARFIELD(
        "Starfield",
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/starfield-mod-manager-config.json" } ?: "./starfield-config.json",
        "./starfield-data.json",
        "/Data",
        "starfield-mods",
        "starfield",
    ),
    OBLIVION_REMASTERED(
        "Oblivion Remastered",
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/oblivion-remastered-mod-manager-config.json" } ?: "./oblivion-remastered-config.json",
        "./oblivion-remastered-data.json",
        "/Content/Dev/ObvData/Data",
        "oblivion-remastered-mods",
        "oblivionremastered",
    )
}

fun mainConfigPath() = System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/mod-manager-config.json" } ?: "./config.json"

enum class GamePath {
    GAME, APP_DATA, INI
}

const val win64 = "/Binaries/Win64"
const val paks = "/Content/Paks/~mods"
