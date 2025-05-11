package commands

import GamePath
import GamePath.*
import Mod
import gameConfig
import runCommand
import toolConfig
import toolData
import java.awt.Desktop
import java.io.File
import java.net.URI

val openDescription = """
    Open various folders and filepaths
    You can open a mod locally or on the nexus, or open the various config paths
    If you pass 'cli' it will open in terminal instead of local folder
""".trimIndent()


fun open(args: List<String>) = openMod(true, args)
fun local(args: List<String>) = openMod(false, args)
fun cli(args: List<String>) = openMod(false, args.toMutableList().also { it.add("cli") })

fun openGamePath(args: List<String>) = open(GAME, "game path", args.contains("cli"))
fun openAppDataPath(args: List<String>) = open(APP_DATA, "appdata path", args.contains("cli"))
fun openIniPath(args: List<String>) = open(INI, "ini path", args.contains("cli"))
fun openPluginsTxt(args: List<String>) = open(gameConfig[APP_DATA]!! + "/Plugins.txt", "plugins file", args.contains("cli"))
fun openJarPath(args: List<String>) = open(".", "jar path", args.contains("cli"))
fun openManual(args: List<String>) = openInWeb("https://manapart.github.io/starfield-mod-manager-site/manual.html")
fun openSite(args: List<String>) = openInWeb("https://manapart.github.io/starfield-mod-manager-site/index.html")
fun openSource(args: List<String>) = openInWeb("https://github.com/ManApart/starfield-mod-manager")
fun openNexus(args: List<String>) = openInWeb("https://www.nexusmods.com/starfield/mods/6576")

private fun openMod(web: Boolean = true, args: List<String>) {
    val cli = args.contains("cli")
    val mods = args.filter { it != "cli" }.getIndicesOrRange(toolData.mods.size).mapNotNull { toolData.mods.getOrNull(it) }
    when {
        mods.isEmpty() -> println(openDescription)
        web -> mods.forEach { openInWeb(it) }
        else -> mods.forEach { openLocal(it, cli) }
    }
}

fun openInWeb(mod: Mod) {
    if (mod.id == null) {
        println("Can't open ${mod.name} because it doesn't have an id.")
    } else {
        openInWeb(mod.url(), mod.name)
    }
}

fun openInWeb(url: String, urlName: String = url) {
    try {
        Desktop.getDesktop().browse(URI(url))
    } catch (e: Exception) {
        println("Unable to open $urlName in web")
    }
}

fun openLocal(mod: Mod, cli: Boolean) = open(mod.filePath, mod.name, cli)

private fun open(path: GamePath, name: String, cli: Boolean) {
    open(gameConfig[path]!!, name, cli)
}

private fun open(path: String, name: String, cli: Boolean) {
    try {
        if (cli) {
            val command = toolConfig.openInTerminalCommand?.replace("{pwd}", path) ?: "gnome-terminal"
            File(path).runCommand(command)
        } else {
            Desktop.getDesktop().open(File(path))
        }
    } catch (e: Exception) {
        println("Unable to open $name on disk ($path)")
    }
}
