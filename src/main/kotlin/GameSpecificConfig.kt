import GamePath.*

enum class GameMode(
    val displayName: String,
    val configPath: String,
    val dataPath: String,
    val modFolder: String,
    val urlName: String,
    val paths: List<GamePath>,
) {
    STARFIELD(
        "Starfield",
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/starfield-mod-manager-config.json" } ?: "./starfield-config.json",
        "./starfield-data.json",
        "starfield-mods",
        "starfield",
        listOf(GAME, APP_DATA, INI)
    ),
    OBLIVION_REMASTERED(
        "Oblivion Remastered",
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/oblivion-remastered-mod-manager-config.json" } ?: "./oblivion-remastered-config.json",
        "./oblivion-remastered-data.json",
        "oblivion-remastered-mods",
        "oblivionremastered",
        listOf(GAME, APP_DATA, INI, UNREAL_INI, UE4SS, PAKS)
    )
}

fun mainConfigPath() = System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/mod-manager-config.json" } ?: "./config.json"

enum class GamePath {
    GAME, APP_DATA, INI, UNREAL_INI, UE4SS, PAKS
}
