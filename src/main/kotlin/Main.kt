import commands.CommandType
import kotlinx.serialization.encodeToString
import java.io.File

lateinit var toolData: Data
lateinit var toolConfig: Config
lateinit var modFolder: File
val HOME = System.getProperty("user.home")!!
private var confirmation: ((List<String>) -> Unit)? = null
var testingMode = false

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun main(args: Array<String>) {
    println(cyan("\nStarfield Mod Manager"))
    modFolder = File(gameConfig.modFolder)
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

fun loadData() {
    toolConfig = File(gameConfig.configPath).takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Config()
    toolData = File(gameConfig.dataPath).takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Data()
    toolData.updateSorts()
}

fun save() {
    if (testingMode) return
    File(gameConfig.configPath).writeText(jsonMapper.encodeToString(toolConfig))
    File(gameConfig.dataPath).writeText(jsonMapper.encodeToString(toolData))
}


fun verbose(message: String) {
    if (toolConfig.verbose) println(message)
}

fun confirm(skip: Boolean = false, warnMessage: String? = null, block: () -> Unit) {
    if (skip) block() else {
        warnMessage?.let { println("$it (y/n)") }
        confirmation = { c ->
            if (c.firstOrNull() == "y" || c.firstOrNull() == "yes") {
                block()
            } else if (c.size != 1 || c.first() !in listOf("no", "n")) {
                println("Unable to understand response. Bailing out")
            }
        }
    }
}
