import commands.CommandType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

lateinit var toolData: Data
lateinit var toolConfig: Config
lateinit var modFolder: File
val HOME = System.getProperty("user.home")!!
var confirmation: ((List<String>) -> Unit)? = null

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun main(args: Array<String>) {
    println("Starfield Mod Manager")
    modFolder = File("mods")
    loadData()
    if (args.isEmpty()) {
        CommandType.LIST.apply(listOf())
        while (true) {
            readLine(readlnOrNull())
            while (confirmation != null) {
                confirmation?.let { confirm ->
                    readlnOrNull()?.parseArgs()?.let {
                        confirmation = null
                        confirm(it)
                    }
                }
            }
        }
    } else {
        readLine(args.joinToString(" "))
    }
}

private fun loadData() {
    toolConfig = File("./config.json").takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Config()
    toolData = File("./data.json").takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Data()
}

fun save() {
    File("./config.json").writeText(jsonMapper.encodeToString(toolConfig))
    File("./data.json").writeText(jsonMapper.encodeToString(toolData))
}


fun verbose(message: String) {
    if (toolConfig.verbose) println(message)
}