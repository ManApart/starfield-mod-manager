import kotlinx.serialization.Serializable

@Serializable
data class State(
    val mods: MutableList<Mod> = mutableListOf()
)


