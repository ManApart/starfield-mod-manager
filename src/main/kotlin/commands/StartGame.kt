package commands

import runCommand
import java.io.File

val startGameHelp = """
    start - run the steam game
""".trimIndent()

val startGameUsage = """
    start - run the steam game
""".trimIndent()

fun startGame(args: List<String>) {
    File(".").runCommand("steam steam://rungameid/1716740")
}