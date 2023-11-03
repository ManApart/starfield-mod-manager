import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.File

@Serializable
data class Mod(
    var name: String,
    var filePath: String,
    var loadOrder: Int,
    var id: Int? = null,
    var downloadPath: String? = null,
    var fileId: Int? = null,
    var latestFileId: Int? = null,
    var version: String? = null,
    var latestVersion: String? = null,
    var enabled: Boolean = false,
    var categoryId: Int? = null,
    var endorsed: Boolean? = null,
) {
    @Transient
    var show: Boolean = true

    fun getModFiles(): List<File> {
        return File(filePath).getFiles {
            !it.path.contains("${filePath}/fomod", ignoreCase = true)
                    && !it.path.contains("${filePath}/optional", ignoreCase = true)
        }
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

fun File.getFiles(filterFunction: (File) -> Boolean = { true }): List<File> {
    val fileList = listFiles() ?: arrayOf()
    val (folders, files) = fileList.partition { it.isDirectory }
    return files.filter { filterFunction(it) } + folders.flatMap { it.getFiles(filterFunction) }
}

fun File.getFolders(filterFunction: (File) -> Boolean = { true }): List<File> {
    val fileList = listFiles() ?: arrayOf()
    val folders = fileList.filter { it.isDirectory }
    return folders.filter { filterFunction(it) } + folders.flatMap { it.getFolders(filterFunction) }
}