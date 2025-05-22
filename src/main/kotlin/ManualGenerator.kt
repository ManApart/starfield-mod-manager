import commands.Category
import commands.CommandType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
private data class CommandJson(val name: String, val category: Category, val summary: String, val description: String, val usage: String, val aliases: List<String> = listOf())

fun main() {
    val manualData = File("manual-data.json").also { if (!it.exists()) it.createNewFile() }
    val manualDataEntries = CommandType.entries.map {
        exceptions[it] ?: CommandJson(it.cleanName, it.category, it.summary, it.description, it.usage, it.aliases.toList())
    }
    manualData.writeText(jsonMapper.encodeToString(manualDataEntries))
}

private val exceptions = mapOf<CommandType, CommandJson>(
)
