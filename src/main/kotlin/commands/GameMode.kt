package commands

import GameMode
import gameConfig
import loadData

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

fun gameMode(args: List<String>) {
    val firstArg = args.firstOrNull() ?: ""
    when {
        args.isEmpty() -> println(modeDescription)
        listOf("starfield", "sf").contains(firstArg) -> setGameToStarfield()
        listOf("oblivionremastered", "or").contains(firstArg) -> setGameToOblivionRemastered()
        else -> println(modeDescription)
    }
}

private fun setGameToStarfield() {
    if (gameConfig != GameMode.STARFIELD) {
        gameConfig = GameMode.STARFIELD
        loadData()
        CommandType.LIST.apply(listOf())
    }
}

private fun setGameToOblivionRemastered() {
    if (gameConfig != GameMode.OBLIVION_REMASTERED) {
        gameConfig = GameMode.OBLIVION_REMASTERED
        loadData()
        CommandType.LIST.apply(listOf())
    }
}
