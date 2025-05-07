var gameConfig = GameMode.STARFIELD

enum class GameMode(
    val configPath: String,
    val dataPath: String,
    val modFolder: String,
) {
    STARFIELD(
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/starfield-mod-manager-config.json" } ?: "./starfield-config.json",
        "./starfield-data.json",
        "starfield-mods",
    ),
    OBLIVION_REMASTERED(
        System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/oblivion-remastered-mod-manager-config.json" } ?: "./oblivion-remastered-config.json",
        "./oblivion-remastered-data.json",
        "oblivion-remastered-mods",
    )
}
