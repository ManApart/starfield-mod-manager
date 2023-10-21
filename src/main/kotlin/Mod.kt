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
) {
    fun getModFiles(): List<File> {
        return File(filePath).getFiles()
    }

    fun url() = "https://www.nexusmods.com/starfield/mods/$id"

    fun updateAvailable() = latestVersion != null && latestVersion != version
}

fun File.getFiles(filter: (File) -> Boolean = {true}): List<File> {
    val fileList = listFiles() ?: arrayOf()
    val (folders, files) = fileList.partition { it.isDirectory }
    return files.filter { filter(it) } + folders.flatMap { it.getFiles() }
}