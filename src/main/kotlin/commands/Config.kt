package commands

import jsonMapper
import kotlinx.serialization.encodeToString
import save
import toolConfig
import java.io.File

fun configHelp(args: List<String> = listOf()) = """
    config game-path <path-to-folder> - Sets the path to the folder under steam containing the starfield Data folder and exe
    config api-key <key-from-nexus>
""".trimIndent()

fun config(args: List<String>) {
    when {
        args.isEmpty() -> {
            println("Running in ${File(".").absolutePath}")
            println("Config:\n" + jsonMapper.encodeToString(toolConfig))
        }

        args.size == 2 && args.first() == "game-path" -> {
            toolConfig.gamePath = args.last()
            println("Updated game path to ${toolConfig.gamePath}")
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