package commands

import Mod
import confirmation
import save
import toolState
import java.io.File
import java.nio.file.Files.delete

//TODO - require confirmation
fun removeHelp(args: List<String>) = """
    remove <mod index>
    rm <mod index>
""".trimIndent()

fun remove(args: List<String>) {
    if (args.size == 1 && args.first().toIntOrNull() != null) {
        removeMod(args.first().toInt())
    } else {
        println(removeHelp(listOf()))
    }
}

private fun removeMod(index: Int) {
    toolState.byIndex(index)?.let { mod ->
        println("Remove ${mod.name}? (y/n)")
        confirmation = { args ->
            if (args.size != 1 || args.first() !in listOf("y","n")){
                println("Unable to understand response. Bailing out")
            } else if (args.first() == "y"){
                delete(mod)
            }
        }
    }
}

fun delete(mod: Mod) {
    val existing = File(mod.filePath)
    if (existing.exists()) existing.deleteRecursively()
    toolState.mods.remove(mod)
    toolState.mods.filter { it.loadOrder > mod.loadOrder }.map { it.loadOrder -= 1 }
    save()
    listMods()
}
