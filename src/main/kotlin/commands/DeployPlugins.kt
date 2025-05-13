package commands

import cyan
import gameConfig
import yellow
import java.io.File
import GamePath.*
import gameMode

fun deployPlugins(files: Map<String, File>) {
    if (gameConfig[APP_DATA]== null){
        println("Config must have appdata path to update plugins.txt")
        return
    }
    val pluginsFile = File(gameMode.generatedPaths["plugins"]!!.path())
    pluginsFile.writeText(createPluginsContent(files))

}

fun deployPluginsDryRun(files: Map<String, File>) {
    println(cyan("Plugins.txt would look like:"))
    println(createPluginsContent(files))
}

private fun createPluginsContent(files: Map<String, File>): String {
    val pluginList = files.entries.filter { it.value.extension.lowercase() in listOf("esp", "esm", "esl") }.map { it.value.name }
    val pluginLines = pluginList.joinToString("\n") { "|*$it"  }

    if (pluginList.toSet().size != pluginList.size){
        println(yellow("WARNING: There are duplicate plugin files"))
    }

    val header = if (gameMode == GameMode.STARFIELD) starfieldHeader else oblivionRemasteredHeader
    return """
        |$header
        $pluginLines
    """.trimMargin()
}

private const val starfieldHeader = """
|# This file is used by Starfield to keep track of your downloaded content.
|# Please do not modify this file.
"""

private const val oblivionRemasteredHeader = """
|Oblivion.esm
|DLCBattlehornCastle.esp
|DLCFrostcrag.esp
|DLCHorseArmor.esp
|DLCMehrunesRazor.esp
|DLCOrrery.esp
|DLCShiveringIsles.esp
|DLCSpellTomes.esp
|DLCThievesDen.esp
|DLCVileLair.esp
|Knights.esp
|AltarESPMain.esp
|AltarDeluxe.esp
|AltarESPLocal.esp
"""
