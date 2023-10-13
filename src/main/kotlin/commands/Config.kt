package commands

import jsonMapper
import kotlinx.serialization.encodeToString
import save
import toolConfig

fun configHelp(args: List<String> = listOf()) = """
    config game-path <path-to-game-folder> - Sets the path to the folder containing the starfield exe and Data folder
    config api-key <key-from-nexus>
""".trimIndent()

fun config(args: List<String>) {
    when {
        args.isEmpty() -> println("Config:\n" +jsonMapper.encodeToString(toolConfig))
        args.size == 2 && args.first() == "game-path" -> {
            toolConfig.gamePath = args.last()
            println("Updated data path to ${toolConfig.gamePath}")
            save()
        }
        args.size == 2 && args.first() == "api-key" -> {
            toolConfig.apiKey = args.last()
            println("Updated api key to ${toolConfig.gamePath}")
            save()
        }

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}