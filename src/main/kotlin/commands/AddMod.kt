package commands

import HOME
import Mod
import modFolder
import runCommand
import save
import toolState
import java.io.File
import java.util.zip.ZipFile

fun addModHelp(args: List<String> = listOf()) = """
   add-mod file <path-to-mod-zip> <name-of-mod>*
""".trimIndent()

fun addMod(args: List<String>) {
    val subCommand = args.firstOrNull()
    when {
        args.size < 2 -> println(addModHelp())
//        subCommand == "id" -> addModById(args[1], args[2])
//        subCommand == "url" -> addModByUrl(args[1], args[2])
        subCommand == "file" -> addModByFile(args[1], args.getOrNull(2))

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

//private fun addModById(id: String, name: String){
//    println("Add by id $id $name")
//}
//private fun addModByUrl(url: String, name: String){
//    println("Add by url")
//}
private fun addModByFile(filePath: String, nameOverride: String?) {
    val name = nameOverride ?: File(filePath).nameWithoutExtension
    val sourceFile = File(filePath.replace("~", HOME))
    if (!sourceFile.exists()){
        println("Could not find ${sourceFile.absolutePath}")
        return
    }
    val stageFile = File(modFolder.path + "/$name")
    val stageExists = stageFile.exists()
    if (stageMod(sourceFile, stageFile)) {
        if (stageExists) {
            println("Updated $name")
        } else {
            val loadOrder = toolState.mods.maxOfOrNull { it.loadOrder } ?: 0
            toolState.mods.add(Mod(name, stageFile.path, loadOrder + 1))
            save()
            println("Added $name")
        }
    } else {
        println("Failed to add mod $name")
    }
}

private fun stageMod(sourceFile: File, stageFolder: File): Boolean {
    stageFolder.mkdirs()
    if (sourceFile.isDirectory) {
        sourceFile.copyRecursively(stageFolder, overwrite = true)
    } else {
        stageFolder.runCommand(listOf("unzip", "-q", "-o", sourceFile.absolutePath))
    }
    return true
}