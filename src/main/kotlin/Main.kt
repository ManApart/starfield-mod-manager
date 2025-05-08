import commands.CommandType
import kotlinx.serialization.encodeToString
import java.io.File

lateinit var toolData: Data
lateinit var toolConfig: MainConfig
lateinit var gameConfig: GameConfig
lateinit var modFolder: File
var gameMode = GameMode.STARFIELD
val HOME = System.getProperty("user.home")!!
private var confirmation: ((List<String>) -> Unit)? = null
var testingMode = false

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun main(args: Array<String>) {
    loadData()
    println(cyan("\n${gameMode.displayName} Mod Manager"))
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
    toolConfig = File(mainConfigPath()).takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: MainConfig()
    gameMode = toolConfig.mode
    modFolder = File(gameMode.modFolder)

    gameConfig = File(gameMode.configPath).takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: GameConfig()

    toolData = File(gameMode.dataPath).takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Data()
    toolData.updateSorts()
}

fun save() {
    if (testingMode) return
    File(mainConfigPath()).writeText(jsonMapper.encodeToString(toolConfig))
    File(gameMode.configPath).writeText(jsonMapper.encodeToString(gameConfig))
    File(gameMode.dataPath).writeText(jsonMapper.encodeToString(toolData))
}

fun saveMainConfigOnly() {
    if (testingMode) return
    File(mainConfigPath()).writeText(jsonMapper.encodeToString(toolConfig))
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
