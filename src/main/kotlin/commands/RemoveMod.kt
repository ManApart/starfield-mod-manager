package commands

import Mod
import confirm
import red
import save
import toolData
import yellow
import java.io.File

val removeHelp = """
    Remove a mod from being managed
""".trimIndent()

val removeDescription = """
    remove <index>
    rm <index>
""".trimIndent()

fun remove(args: List<String>) {
    if (args.size == 1 && args.first().toIntOrNull() != null) {
        removeMod(args.first().toInt())
    } else {
        println(removeHelp)
    }
}

private fun removeMod(index: Int) {
    toolData.byIndex(index)?.let { mod ->
        confirm(false, yellow("Remove ${mod.name}?")) {
            delete(mod)
        }
    }
}

fun delete(mod: Mod) {
    val desc = mod.name
    val existing = File(mod.filePath)
    if (existing.exists()) existing.deleteRecursively()
    toolData.mods.remove(mod)
    toolData.mods.filter { it.loadOrder > mod.loadOrder }.map { it.loadOrder -= 1 }
    toolData.mods.filter { it.index > mod.index }.map { it.index -= 1 }
    save()
    println(red("Mod '$desc' deleted"))
}
