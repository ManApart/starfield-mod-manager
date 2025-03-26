import kotlinx.serialization.Serializable

@Serializable
data class Changes(
    val adds: List<Int> = listOf(),
    val deletes: List<String> = listOf(),
    val tagsAdded: Map<String, List<String>> = mapOf(),
    val tagsRemoved: Map<String, List<String>> = mapOf(),
)
