import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var gamePath: String? = null,
    var verbose: Boolean = false,
)