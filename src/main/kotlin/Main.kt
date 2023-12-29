import commands.CommandType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

lateinit var toolData: Data
lateinit var toolConfig: Config
lateinit var modFolder: File
val HOME = System.getProperty("user.home")!!
val CONFIG_PATH = configPath()
var confirmation: ((List<String>) -> Unit)? = null
var testingMode = false

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

private fun configPath(): String {
    return System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/starfield-mod-manager-config.json" } ?: "./config.json"
}

fun main(args: Array<String>) {
    println(cyan("\nStarfield Mod Manager"))
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
    toolConfig = File(CONFIG_PATH).takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Config()
    toolData = File("./data.json").takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Data()
    toolData.updateSorts()
}

fun save() {
    if (testingMode) return
    File(CONFIG_PATH).writeText(jsonMapper.encodeToString(toolConfig))
    File("./data.json").writeText(jsonMapper.encodeToString(toolData))
}


fun verbose(message: String) {
    if (toolConfig.verbose) println(message)
}