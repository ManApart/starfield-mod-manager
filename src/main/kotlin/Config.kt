import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var chunkSize: Int = 5,
    var gamePath: String? = null,
    var appDataPath: String? = null,
    var iniPath: String? = null,
    var apiKey: String? = null,
    var useMyDocs: Boolean = false,
    var verbose: Boolean = false,
    var categories: Map<Int, String> = mapOf(),
) {
    fun usedGamePath(modGamePath: String) = if (useMyDocs && modGamePath.startsWith("Data", true)) iniPath else gamePath
}