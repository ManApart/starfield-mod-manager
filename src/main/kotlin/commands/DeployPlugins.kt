package commands

import toolConfig
import yellow
import java.io.File

fun deployPlugins(files: Map<String, File>) {
    if (toolConfig.iniPath == null){
        println("Config must have ini path to update plugins.txt")
        return
    }
    val iniFile = File(toolConfig.iniPath!! + "/Plugins.txt")
    iniFile.writeText(createPluginsContent(files))

}

fun deployPluginsDryRun(files: Map<String, File>) {
    println("Plugins.txt would look like:")
    println(createPluginsContent(files))
}

private fun createPluginsContent(files: Map<String, File>): String {
    val pluginList = files.entries.filter { it.value.extension.lowercase() in listOf("esp", "esm", "esl") }.map { it.value.name }
    val pluginLines = pluginList.joinToString("\n") { "|*$it"  }

    if (pluginList.toSet().size != pluginList.size){
        println(yellow("WARNING: There are duplicate plugin files"))
    }

    return """
        |# This file is used by Starfield to keep track of your downloaded content.
        $pluginLines
    """.trimMargin()
}