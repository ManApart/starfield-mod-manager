import commands.Category
import commands.CommandType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.File

fun main(){
    val manualData = File("manual-data.json").also { if (!it.exists()) it.createNewFile() }
    val manualDataEntries = CommandType.entries.map { CommandJson(it.cleanName, it.category, it.description, it.aliases.toList(), it.help()) }
    manualData.writeText(jsonMapper.encodeToString(manualDataEntries))
}

@Serializable
private data class CommandJson(val name: String, val category: Category, val summary: String, val aliases: List<String> = listOf(), val usage: String)