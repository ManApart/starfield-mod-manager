import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Mod(
    var name: String,
    var filePath: String,
    var loadOrder: Int,
    var id: Int? = null,
    var fileId: Int? = null,
    var latestFileId: Int? = null,
    var version: String? = null,
    var latestVersion: String? = null,
    var enabled: Boolean = false,
    var categoryId: Int? = null,
) {
    fun getModFiles(): List<File> {
        return File(filePath).getFiles()
    }

    fun url() = "https://www.nexusmods.com/starfield/mods/$id"

    fun updateAvailable() = latestVersion != null && latestVersion != version

    fun idName(): String {
        return if (id == null) name else "$id $name"
    }

    fun category(): String? {
        return categoryId?.let { toolConfig.categories[it] }
    }
}

fun File.getFiles(filterFunction: (File) -> Boolean = {true}): List<File> {
    val fileList = listFiles() ?: arrayOf()
    val (folders, files) = fileList.partition { it.isDirectory }
    return files.filter { filterFunction(it) } + folders.flatMap { it.getFiles(filterFunction) }
}