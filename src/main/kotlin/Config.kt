import kotlinx.serialization.Serializable

@Serializable
data class Config(
    var chunkSize: Int = 5,
    var gamePath: String? = null,
    var apiKey: String? = null,
    var verbose: Boolean = false,
    var categories: Map<Int, String> = mapOf(),
    val collections: MutableList<Collection> = mutableListOf(),
)

//Use ids. If ids are blank, use filepaths
//display like mod and operate off indexes
@Serializable
data class Collection(val name: String, val ids: List<Int>, val filePaths: List<String>)