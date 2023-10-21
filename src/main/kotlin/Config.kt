import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var chunkSize: Int = 5,
    var gamePath: String? = null,
    var apiKey: String? = null,
    var verbose: Boolean = false,
    var categories: Map<Int, String> = mapOf()
)