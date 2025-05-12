package commands

import GamePath
import Mod
import gameConfig
import gameMode
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

val openUsage = OpenType.entries.joinToString("\n") {
    val otherAliases = if (it.aliases.size > 1) " (" + it.aliases.drop(1).joinToString(",") + ")" else ""
    it.aliases.first() + otherAliases + " " + it.description
}

enum class OpenType(
    val aliases: List<String>,
    val description: String,
    val invoke: (List<String>) -> Unit
) {
    GAME(listOf("gamepath", "gg"), "Where the game is installed", { args -> open(GamePath.GAME, "game path", args.contains("cli")) }),
    APP_DATA(listOf("appdatapath", "app"), "Where the appdadata is", { args -> open(GamePath.APP_DATA, "appdata path", args.contains("cli")) }),
    INI(listOf("inipath", "ini"), "Game ini folder path", { args -> open(GamePath.INI, "ini path", args.contains("cli")) }),
    PLUGINS(listOf("plugins", "plugin"), "Location of plugins.txt", { args ->
        val folder = if (gameMode == GameMode.STARFIELD) gameConfig[GamePath.APP_DATA]!! else gameConfig[GamePath.GAME]!! + gameMode.dataModPath
        open("$folder/Plugins.txt", "plugins file", args.contains("cli"))
    }),
    JAR(listOf("jarpath", "jar"), "Location the mod manager is running from", { args -> open(".", "jar path", args.contains("cli")) }),
    MANUAL(listOf("manual", "man"), "Online Manual of commands", { openInWeb("https://manapart.github.io/starfield-mod-manager-site/manual.html") }),
    SITE(listOf("manual", "man"), "Open manager's main site", { openInWeb("https://manapart.github.io/starfield-mod-manager-site/index.html") }),
    SOURCE(listOf("manual", "man"), "Open source for mod manager", { openInWeb("https://github.com/ManApart/starfield-mod-manager") }),
    NEXUS(listOf("manual", "man"), "Open the mod on nexus mods", ::openNexus),
}

fun open(args: List<String>) = openMod(true, args)
fun local(args: List<String>) = openMod(false, args)
fun cli(args: List<String>) = openMod(false, args.toMutableList().also { it.add("cli") })

private fun openNexus(args: List<String>){
    if (args.size != 2 || args[1].toIntOrNull() != null) {
        println("Must pass a mod index to open")
        return
    }
    val modId = toolData.byIndex(args[1].toInt())?.id
    if (modId == null) {
        println("Unable to find mod")
        return
    }
    openInWeb("https://www.nexusmods.com/starfield/mods/${modId}")
}

private fun openMod(web: Boolean = true, args: List<String>) {
    if (args.isEmpty()) {
        println(CommandType.OPEN.description + "\n")
        println(CommandType.OPEN.usage)
        return
    }
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
