import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

fun main(args: Array<String>) {
    println("Hello World!")
    val modsDir = File("./mods").also { if (!it.exists()) it.mkdir() }
    modsDir.listFiles()!!.forEach { Files.delete(it.toPath()) }

    val testSym = Files.createSymbolicLink(Path("mods/test"), Path("src"))

    modsDir.listFiles()!!.filter { Files.isSymbolicLink(it.toPath()) }.forEach {
        println(it.toPath().toFile().absolutePath + ", " + Files.readSymbolicLink(it.toPath()).toFile().absolutePath)
    }
}