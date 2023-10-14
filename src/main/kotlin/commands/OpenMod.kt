package commands

import Mod
import toolState
import java.awt.Desktop
import java.io.File
import java.lang.Exception
import java.net.URI

fun openHelp(args: List<String>) = """
    open <mod index> - open on nexus
    local <mod index> - open local folder
    open 1 2 4
    open 1-4
""".trimIndent()

fun open(args: List<String>) = openMod(true, args)

fun local(args: List<String>) = openMod(false, args)

private fun openMod(web: Boolean = true, args: List<String>) {
    val mods = args.getIndicesOrRange(toolState.mods.size).mapNotNull { toolState.mods.getOrNull(it) }
    when {
        mods.isEmpty() -> println(enableHelp(listOf()))
        web -> mods.forEach { openInWeb(it) }
        else -> mods.forEach { openLocal(it) }
    }
}

fun openInWeb(mod: Mod) {
    if (mod.id == null) {
        println("Can't open ${mod.name} because it doesn't have an id.")
    } else {
        try {
            Desktop.getDesktop().browse(URI(mod.url()))
        } catch (e: Exception) {
            println("Unable to open ${mod.name} in web")
        }
    }
}

fun openLocal(mod: Mod) {
    try {
        Desktop.getDesktop().open(File(mod.filePath))
    } catch (e: Exception) {
        println("Unable to open ${mod.name} on disk")
    }
}
