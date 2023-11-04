import commands.Category
import commands.CommandType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.File

fun main(){
    val manual = File("manual.md").also { if (!it.exists()) it.createNewFile() }
    val content = createContent()
    manual.writeText(content)

    val manualData = File("manual-data.json").also { if (!it.exists()) it.createNewFile() }
    val manualDataEntries = CommandType.entries.map { CommandJson(it.cleanName, it.category, it.description, it.aliases.toList(), it.help()) }
    manualData.writeText(jsonMapper.encodeToString(manualDataEntries))
}

fun createContent(): String {
    val commands = CommandType.entries.joinToString("\n") {
        "${it.cleanName} |${it.description} |${it.aliases.joinToString()} |${it.help().replace("\n", " <br/>")}"
    }
    return """
# Manual

Command | Description | Aliases | Usage
--- | --- | --- | ---
$commands
    """
}

@Serializable
private data class CommandJson(val name: String, val category: Category, val summary: String, val aliases: List<String> = listOf(), val usage: String)