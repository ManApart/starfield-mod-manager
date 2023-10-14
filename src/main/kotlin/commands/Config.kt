package commands

import jsonMapper
import kotlinx.serialization.encodeToString
import save
import toolConfig

fun configHelp(args: List<String> = listOf()) = """
    config data-path <path-to-folder> - Sets the path to the folder in my documents containing the starfield Data folder
    config api-key <key-from-nexus>
""".trimIndent()

fun config(args: List<String>) {
    when {
        args.isEmpty() -> println("Config:\n" + jsonMapper.encodeToString(toolConfig))
        args.size == 2 && args.first() == "game-path" -> {
            toolConfig.dataPath = args.last()
            println("Updated data path to ${toolConfig.dataPath}")
            save()
        }

        args.size == 2 && args.first() == "api-key" -> {
            toolConfig.apiKey = args.last()
            println("Updated api key to ${toolConfig.apiKey}")
            save()
        }

        args.first() == "verbose" -> {
            val verbose = args.getOrNull(1) == "true"
            toolConfig.verbose = verbose
            println("Updated verbose to ${toolConfig.verbose}")
            save()
        }

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}