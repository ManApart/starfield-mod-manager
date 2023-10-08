import commands.CommandType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

lateinit var toolState: State
lateinit var toolConfig: Config
lateinit var modFolder: File
val HOME = System.getProperty("user.home")

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun main(args: Array<String>) {
    println("Starfield Mod Manager")
    modFolder = File("mods")
    loadData()
    CommandType.LIST.apply(listOf())
    while (true) {
        readLine(readlnOrNull())
    }
}

private fun loadData() {
    toolConfig = File("./config.json").takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: Config()
    toolState = File("./data.json").takeIf { it.exists() }?.let {
        jsonMapper.decodeFromString(it.readText())
    } ?: State()
}

fun save() {
    File("./config.json").writeText(jsonMapper.encodeToString(toolConfig))
    File("./data.json").writeText(jsonMapper.encodeToString(toolState))
}


fun verbose(message: String){
    if (toolConfig.verbose) println(message)
}

private fun modTest() {
    val modsDir = File("./mods").also { if (!it.exists()) it.mkdir() }
    modsDir.listFiles()!!.forEach { Files.delete(it.toPath()) }

    val testSym = Files.createSymbolicLink(Path("mods/test"), Path("src"))

    modsDir.listFiles()!!.filter { Files.isSymbolicLink(it.toPath()) }.forEach {
        println(it.toPath().toFile().absolutePath + ", " + Files.readSymbolicLink(it.toPath()).toFile().absolutePath)
    }
}