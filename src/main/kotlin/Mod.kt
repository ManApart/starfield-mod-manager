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

    private fun File.getFiles(): List<File> {
        val fileList = listFiles() ?: arrayOf()
        val (folders, files) = fileList.partition { it.isDirectory }
        return files + folders.flatMap { it.getFiles() }
    }
}