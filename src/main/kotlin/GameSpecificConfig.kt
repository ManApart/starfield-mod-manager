enum class GameMode(
    val displayName: String,
    val configPath: String,
    val dataPath: String,
    val modFolder: String,
    val urlName: String,
) {
    STARFIELD(
        "Starfield",
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/starfield-mod-manager-config.json" } ?: "./starfield-config.json",
        "./starfield-data.json",
        "starfield-mods",
        "starfield",
    ),
    OBLIVION_REMASTERED(
        "Oblivion Remastered",
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/oblivion-remastered-mod-manager-config.json" } ?: "./oblivion-remastered-config.json",
        "./oblivion-remastered-data.json",
        "oblivion-remastered-mods",
        "oblivionremastered",
    )
}

fun mainConfigPath() = System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/mod-manager-config.json" } ?: "./config.json"
