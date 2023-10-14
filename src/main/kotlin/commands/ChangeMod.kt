package commands

import HOME
import save
import toolState
import java.io.File

fun changeHelp(args: List<String>) = """
    mod <mod index> id 123 - Update mod's id
    mod <mod index> file ~/Downloads/sleepy-time.zip - Delete mod's stage folder and restage from zip
    mod <mod index> name <new name> - renames a mod without changing file paths
    rename <mod index> <new name>
""".trimIndent()

fun moveMod(args: List<String>){
    changeMod(listOf(args.first(), "name") + args.drop(1))
}

fun changeMod(args: List<String>) {
    val i = args.firstOrNull()?.toIntOrNull()
    when {
        args.isEmpty() -> println(changeHelp(listOf()))
        args.size == 3 && args[1] == "id" -> updateId(i!!, args.last().toInt())
        args.size == 3 && args[1] == "file" -> updateFile(i!!, args.last())
        args.size == 3 && args[1] == "name" -> updateName(i!!, args.last())
        else -> println(changeHelp(listOf()))
    }
}

private fun updateId(i: Int, id: Int) {
    toolState.byIndex(i)?.let { mod ->
        println("${mod.name} id updated ${mod.id ?: "?"} -> $id")
        mod.id = id
        save()
    }
}

private fun updateName(i: Int, newName:String) {
    toolState.byIndex(i)?.let { mod ->
        println("${mod.name} renamed to $newName")
        mod.name = newName
        save()
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