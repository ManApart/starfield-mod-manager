package commands

import getFiles
import toolConfig
import java.io.File
import java.nio.file.Files
//TODO - too aggressive if on mounted hard drive
fun purgeHelp(args: List<String>) = """
""".trimIndent()

fun purge(args: List<String>) {
    toolConfig.gamePath?.let { gamePath ->
        File(gamePath).getFiles { Files.isSymbolicLink(it.toPath()) }.forEach { link ->
            println("Deleting ${link.path}")
        }
    }
}