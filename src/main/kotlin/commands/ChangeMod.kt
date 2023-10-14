package commands

import HOME
import toolState
import java.io.File

fun changeHelp(args: List<String>) = """
    mod <mod index> id 123 - Update mod's id
    mod <mod index> file ~/Downloads/sleepy-time.zip - Delete mod's stage folder and restage from zip
""".trimIndent()

fun changeMod(args: List<String>) {
    val i = args.firstOrNull()?.toIntOrNull()
    when {
        args.isEmpty() -> println(changeHelp(listOf()))
        args.size == 3 && args[1] == "id" -> updateId(i!!, args.last().toInt())
        args.size == 3 && args[1] == "file" -> updateFile(i!!, args.last())
        else -> println(changeHelp(listOf()))
    }
}

private fun updateId(i: Int, id: Int) {
    toolState.byIndex(i)?.let { mod ->
        println("${mod.name} id updated ${mod.id ?: "?"} -> $id")
        mod.id = id
    }
}

private fun updateFile(i: Int, sourceFilePath: String) {
    toolState.byIndex(i)?.let { mod ->
        val sourceFile = File(sourceFilePath.replace("~", HOME))
        if (sourceFile.exists()) {
            val existing = File(mod.filePath)
            if (existing.exists()) existing.deleteRecursively()
            addModFile(mod, sourceFile)
        } else {
            println("No file found at $sourceFilePath")
        }
    }
}