import kotlinx.serialization.Serializable

@Serializable
data class State(
    val mods: MutableList<Mod> = mutableListOf()
)


@Serializable
data class Mod(
    var name: String,
    var filePath: String,
    var id: Int? =null,
    var url: String? = null,
    var enabled: Boolean = false,
)