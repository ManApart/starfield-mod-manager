package commands

import runCommand
import java.io.File

fun startGameHelp() = """
    start - run the steam game
""".trimIndent()

fun startGame(args: List<String>) {
    File(".").runCommand("steam steam://rungameid/1716740")
}