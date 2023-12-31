package commands

import runCommand
import java.io.File

val startGameDescription = """
    Run the steam game
""".trimIndent()

val startGameUsage = """
    start
""".trimIndent()

fun startGame(args: List<String>) {
    File(".").runCommand("steam steam://rungameid/1716740")
}