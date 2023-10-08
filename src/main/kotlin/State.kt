import kotlinx.serialization.Serializable

@Serializable
data class State(
    val mods: List<String> = listOf()
)