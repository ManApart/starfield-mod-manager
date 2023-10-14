import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var dataPath: String? = null,
    var apiKey: String? = null,
    var verbose: Boolean = false,
)