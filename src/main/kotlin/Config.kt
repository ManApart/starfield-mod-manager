import kotlinx.serialization.Serializable

@Serializable
data class MainConfig(
    var mode: GameMode = GameMode.STARFIELD,
    var chunkSize: Int = 5,
    var apiKey: String? = null,
    var useMyDocs: Boolean = false,
    var verbose: Boolean = false,
    var autoDeploy: Boolean = true,
    var openInTerminalCommand: String? = null,
)

@Serializable
data class GameConfig(
    var gamePath: String? = null,
    var appDataPath: String? = null,
    var iniPath: String? = null,
    var useMyDocs: Boolean = false,
    var categories: Map<Int, String> = mapOf(),
) {
    fun usedGamePath(modGamePath: String) = if (useMyDocs && modGamePath.startsWith("Data", true)) iniPath else gamePath
}
