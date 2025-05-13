const val win64 = "/Binaries/Win64"
const val paks = "/Content/Paks/~mods"
const val obvIni = "/Content/Dev/ObvData/"
//Paks, ue4ss, unreal engine ini, etc


enum class GamePath {
    GAME, APP_DATA, INI, COMPAT_DATA
}

class GeneratedPath(
    val name: String,
    val aliases: List<String>,
    val description: String,
    val path: () -> String,
)

private val gamePath = GeneratedPath("Game Path", listOf("gamepath", "gg"), "Where the game is installed") { gameConfig[GamePath.GAME]!! }

fun starfieldPaths(): List<GeneratedPath> {
    return listOf(gamePath)
}

fun oblivionRemasteredPaths(): List<GeneratedPath> {
    return listOf(
        gamePath,
        GeneratedPath("Win64", listOf("win64"), "Where the unreal engine binary lives") { gameConfig[GamePath.GAME] + "/Binaries/Win64" }
    )
}
