package commands

import jsonMapper
import kotlinx.serialization.encodeToString
import nexus.getGameInfo
import save
import toolConfig
import java.io.File

val configDescription = """
    Used to configure the mod manager itself. Saved in the config.json file located next to the jar
    game-path should be the path to the folder under steam containing the starfield Data folder and exe
    appdata-path should be the path to the folder under your appdata that will contain Plugins.txt. Needed for updating mod load order
    ini-path should be the path to the folder under your documents that contains StarfieldCustom.ini. It's optionally used to deploy to your my docs folder instead of the game path
    verbose gives additional output (for debugging) if set to true
    use-my-docs optionally allows deploying mod files under Data to my documents instead of the game folder. (Defaults to false) 
    categories is used to download category names from nexus
    If your paths have spaces, make sure to quote them
""".trimIndent()
val configUsage = """
    config game-path <path-to-folder>
    config appdata-path <path-to-folder>
    config ini-path <path-to-folder> 
    config api-key <key-from-nexus>
    config verbose <true/false>
    config use-my-docs <true/false> 
    config categories
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

        args.size == 2 && args.first() == "appdata-path" -> {
            toolConfig.appDataPath = args.last()
            println("Updated appdata path to ${toolConfig.appDataPath}")
            save()
        }

        args.size == 2 && args.first() == "ini-path" -> {
            toolConfig.iniPath = args.last()
            println("Updated ini path to ${toolConfig.iniPath}")
            save()
        }

        args.size == 2 && args.first() == "api-key" -> {
            toolConfig.apiKey = args.last()
            println("Updated api key to ${toolConfig.apiKey}")
            save()
        }

        args.size == 1 && args.first() == "categories" -> {
            getGameInfo(toolConfig.apiKey!!)?.let { info ->
                if (info.categories.isNotEmpty()) {
                    toolConfig.categories = info.categories.associate { it.category_id to it.name }
                    println("Saved ${toolConfig.categories.size} categories")
                    save()
                }
            }
        }

        args.first() == "verbose" -> {
            val verbose = args.getOrNull(1) == "true"
            toolConfig.verbose = verbose
            println("Updated verbose to ${toolConfig.verbose}")
            save()
        }

        args.first() == "use-my-docs" -> {
            val useMyDocs = args.getOrNull(1) == "true"
            toolConfig.useMyDocs = useMyDocs
            println("Updated use-my-docs to ${toolConfig.useMyDocs}")
            save()
        }

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}