package commands

import toolConfig
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
    val pluginLines = files.entries.filter { it.value.extension.lowercase() in listOf("esp", "esm", "esl") }.joinToString("\n") { "|*${it.value.name}"  }

    return """
        |# This file is used by Starfield to keep track of your downloaded content.
        $pluginLines
    """.trimMargin()
}