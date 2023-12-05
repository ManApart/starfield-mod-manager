import commands.CommandType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

lateinit var toolData: Data
lateinit var toolConfig: Config
lateinit var modFolder: File
val HOME = System.getProperty("user.home")!!
var GAME = "starfield"
var CONFIG_PATH = configPath()
var confirmation: ((List<String>) -> Unit)? = null

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

private fun configPath(): String {
    return System.getenv("XDG_CONFIG_HOME")?.replace("~", HOME)?.let { "$it/$GAME-mod-manager-config.json" } ?: "./$GAME-mod-manager-config.json"
}

fun main(args: Array<String>) {
    GAME = args.firstOrNull() ?: "Starfield"
    CONFIG_PATH = configPath()

    println(cyan("\nCLI Mod Manager"))
    modFolder = File("$GAME-mods")
    loadData()
    if (args.size < 2) {
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
        readLine(args.drop(1).joinToString(" "))
    }
}

private fun loadData() {
    toolConfig = File(CONFIG_PATH).takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Config()
    toolData = File("./$GAME-data.json").takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Data()
    toolData.updateSorts()
}

fun save() {
    File(CONFIG_PATH).writeText(jsonMapper.encodeToString(toolConfig))
    File("./$GAME-data.json").writeText(jsonMapper.encodeToString(toolData))
}


fun verbose(message: String) {
    if (toolConfig.verbose) println(message)
}