import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Mod(
    var name: String,
    var filePath: String,
    var loadOrder: Int,
    var id: Int? = null,
    var url: String? = null,
    var enabled: Boolean = false,
) {
    fun getModFiles(): List<File> {
        return File(filePath).getFiles()
    }
}

fun File.getFiles(filter: (File) -> Boolean = {true}): List<File> {
    val fileList = listFiles() ?: arrayOf()
    val (folders, files) = fileList.partition { it.isDirectory }
    return files.filter { filter(it) } + folders.flatMap { it.getFiles() }
}