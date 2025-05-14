import kotlinx.serialization.Serializable
import GamePath.*

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
    var useMyDocs: Boolean = false,
    var categories: Map<Int, String> = mapOf(),
    val paths: MutableMap<String, String> = mutableMapOf(),
) {
    fun usedGamePath(modGamePath: String) = (if (useMyDocs && modGamePath.startsWith("Data", true)) PathType.INI else PathType.APP_DATA).let { gameMode.path(it)!! }

    operator fun get(type: GamePath) = paths[type.name]
    operator fun set(type: GamePath, value: String) {
        paths[type.name] = value
    }
}
