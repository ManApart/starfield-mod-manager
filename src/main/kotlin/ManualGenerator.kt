import commands.CommandType
import java.io.File

fun main(){
    val manual = File("manual.md").also { if (!it.exists()) it.createNewFile() }
    val content = createContent()
    manual.writeText(content)
}

fun createContent(): String {
    val commands = CommandType.entries.joinToString("\n") {
        "${it.cleanName}|${it.description}|${it.help().replace("\n", " <br/>")}"
    }
    return """
# Manual

Command | Description | Usage
--- | --- | ---
$commands
    """
}
