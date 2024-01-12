package commands

import Mod
import toolConfig
import toolData
import java.awt.Desktop
import java.io.File
import java.lang.Exception
import java.net.URI

val openDescription = """
    Open various folders and filepaths
    You can open a mod locally or on the nexus, or open the various config paths
""".trimIndent()


fun open(args: List<String>) = openMod(true, args)

fun local(args: List<String>) = openMod(false, args)

fun openGamePath(args: List<String>) = open(toolConfig.gamePath!!, "game path")
fun openAppDataPath(args: List<String>) = open(toolConfig.appDataPath!!, "appdata path")
fun openIniPath(args: List<String>) = open(toolConfig.iniPath!!, "ini path")
fun openPluginsTxt(args: List<String>) = open(toolConfig.appDataPath!! +"/Plugins.txt", "plugins file")
fun openJarPath(args: List<String>) = open(".", "jar path")

private fun openMod(web: Boolean = true, args: List<String>) {
    val mods = args.getIndicesOrRange(toolData.mods.size).mapNotNull { toolData.mods.getOrNull(it) }
    when {
        mods.isEmpty() -> println(openDescription)
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

fun openLocal(mod: Mod) = open(mod.filePath, mod.name)

private fun open(path: String, name: String) {
    try {
        Desktop.getDesktop().open(File(path))
    } catch (e: Exception) {
        println("Unable to open $name on disk ($path)")
    }
}
