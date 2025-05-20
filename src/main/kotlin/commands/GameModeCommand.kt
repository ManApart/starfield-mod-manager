package commands

import GameMode
import gameMode
import initialCommand
import loadData
import saveMainConfigOnly
import toolConfig

val modeDescription = """
   Switch which game you're managing
   Used so you can mod multiple games
""".trimIndent()

val modeUsage = """
   mode starfield
   mode sf
   mode oblivionremastered
   mode or
""".trimIndent()

fun selectGameMode(command: String, args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(modeDescription)
        listOf("starfield", "sf").contains(firstArg) -> setGameToStarfield()
        listOf("oblivionremastered", "or").contains(firstArg) -> setGameToOblivionRemastered()
        else -> println(modeDescription)
    }
}

private fun setGameToStarfield() {
    if (gameMode != GameMode.STARFIELD) {
        gameMode = GameMode.STARFIELD
        toolConfig.mode = gameMode
        saveMainConfigOnly()
        loadData()
        initialCommand()
    }
}

private fun setGameToOblivionRemastered() {
    if (gameMode != GameMode.OBLIVION_REMASTERED) {
        gameMode = GameMode.OBLIVION_REMASTERED
        toolConfig.mode = gameMode
        saveMainConfigOnly()
        loadData()
        initialCommand()
    }
}
