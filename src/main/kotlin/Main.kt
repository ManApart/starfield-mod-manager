import commands.CommandType
import commands.getCommand
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

lateinit var state: State
lateinit var config: Config

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun main(args: Array<String>) {
    println("Starfield Mod Manager")
    loadData()
    while (true){
        readLine(readlnOrNull())
    }
}

private fun loadData(){
    File("./config.json").takeIf { it.exists() }?.let {
        config = jsonMapper.decodeFromString(it.readText())
    }
    File("./data.json").takeIf { it.exists() }?.let {
        state = jsonMapper.decodeFromString(it.readText())
    }
}

fun save(){
    File("./config.json").writeText(jsonMapper.encodeToString(config))
    File("./data.json").writeText(jsonMapper.encodeToString(state))
}

private fun readLine(line: String?) {
    val parts = line?.split(" ") ?: return
    if (parts.isEmpty()) {
        CommandType.HELP.help(listOf())
    } else {
        val commandString = parts.first().lowercase()
        val args = parts.subList(1, parts.size)

        val command = getCommand(commandString)
        if (command != null){
            command.apply(args)
        } else {
            println("Unknown Command $commandString")
        }

    }
}

private fun modTest() {
    val modsDir = File("./mods").also { if (!it.exists()) it.mkdir() }
    modsDir.listFiles()!!.forEach { Files.delete(it.toPath()) }

    val testSym = Files.createSymbolicLink(Path("mods/test"), Path("src"))

    modsDir.listFiles()!!.filter { Files.isSymbolicLink(it.toPath()) }.forEach {
        println(it.toPath().toFile().absolutePath + ", " + Files.readSymbolicLink(it.toPath()).toFile().absolutePath)
    }
}