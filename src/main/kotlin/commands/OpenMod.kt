package commands

import GameMode
import Mod
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
""".trimIndent() +
        OpenType.entries.sortedBy { it.aliases.first() }.joinToString("\n") { it.aliases.first() + " - " + it.description } + "\n" +
        GameMode.entries.asSequence().flatMap { mode -> mode.generatedPaths.values.map { mode to it } }.groupBy { it.second.aliases.first() + it.second.type.description }.map { (_, paths) ->
            val modes = paths.map { it.first }.joinToString{it.abbreviation}
            val first = paths.first().second
            first.aliases.first() +" ($modes) - " + first.type.description
        }.sorted().joinToString("\n")

val openUsage = """
    open <index>, 
""".trimIndent() +
        OpenType.entries.sortedBy { it.aliases.first() }.joinToString(", ") { it.aliases.first() } + ", " +
        GameMode.entries.asSequence().flatMap { it.generatedPaths.values }.map { it.aliases.first() }.toSet().sorted().joinToString(", ")

val openAliases = (listOf("paths", "path", "o") + OpenType.entries.flatMap { it.aliases } + GameMode.entries.flatMap { mode -> mode.generatedPaths.values.flatMap { it.aliases } }).toSet().toTypedArray()

enum class OpenType(
    val aliases: List<String>,
    val description: String,
    val invoke: (List<String>) -> Unit
) {
    MANUAL(listOf("manual", "man"), "Online Manual of commands", { openInWeb("https://manapart.github.io/starfield-mod-manager-site/manual.html") }),
    SITE(listOf("site", "man"), "Open manager's main site", { openInWeb("https://manapart.github.io/starfield-mod-manager-site/index.html") }),
    SOURCE(listOf("git", "github", "source"), "Open source for mod manager", { openInWeb("https://github.com/ManApart/starfield-mod-manager") }),
    NEXUS(listOf("nexus"), "Open the mod on nexus mods", ::openNexus),
}

fun open(command: String, args: List<String>) {
    val openType = OpenType.entries.firstOrNull { it.aliases.contains(command) }
    val gamePath = gameMode.generatedPaths.values.firstOrNull { it.aliases.contains(command) }
    if (openType != null) {
        openType.invoke(args)
    } else if (gamePath != null) {
        open(gamePath.path(), gamePath.type.name, args.contains("cli"))
    } else {
        openMod(true, args)
    }
}

fun local(command: String, args: List<String>) = openMod(false, args)
fun cli(command: String, args: List<String>) = openMod(false, args.toMutableList().also { it.add("cli") })

private fun openNexus(args: List<String>) {
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

fun open(path: String, name: String, cli: Boolean) {
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
