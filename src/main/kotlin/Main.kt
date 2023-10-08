import commands.Command
import commands.help
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

fun main(args: Array<String>) {
    println("Hello World!")
    do {

    } while (readLine(readlnOrNull()))
}

private fun readLine(line: String?): Boolean {
    val parts = line?.split(" ") ?: return false
    if (parts.isEmpty()) {
        help()
    } else {
        val commandString = parts.first().lowercase()
        val args = parts.subList(1, parts.size)

        val command = Command.entries.firstOrNull { commandString == it.name.lowercase() }
        if (command != null){
            command.apply(args)
        } else {
            println("Unknown Command $commandString")
        }

    }

    return true
}

private fun modTest() {
    val modsDir = File("./mods").also { if (!it.exists()) it.mkdir() }
    modsDir.listFiles()!!.forEach { Files.delete(it.toPath()) }

    val testSym = Files.createSymbolicLink(Path("mods/test"), Path("src"))

    modsDir.listFiles()!!.filter { Files.isSymbolicLink(it.toPath()) }.forEach {
        println(it.toPath().toFile().absolutePath + ", " + Files.readSymbolicLink(it.toPath()).toFile().absolutePath)
    }
}