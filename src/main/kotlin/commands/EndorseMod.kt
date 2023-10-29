package commands

import Mod
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import save
import toolConfig
import toolData
import updateModInfo

fun endorseHelp() = """
    endorse <mod index> - endorse a mod on nexus
    abstain <mod index>
    endorse 1 2 4
    endorse 1-4
""".trimIndent()

fun endorse(args: List<String>) = endorseMod(true, args)
fun abstain(args: List<String>) = endorseMod(false, args)

private fun endorseMod(abstain: Boolean = true, args: List<String>) {
    when {
        args.isEmpty() -> println(endorseHelp())
        args.size == 1 && args.first().contains("-") -> endorseRange(abstain, args)
        args.size == 1 && args.first() == "all" -> endorseRange(abstain, listOf("0-${toolData.mods.size - 1}"))
        else -> endorseList(abstain, args)
    }
}

private fun endorseList(endorse: Boolean, args: List<String>) {
    val mods = args.getIndices(toolData.mods.size).map { i -> toolData.mods[i] }
    endorseMods(endorse, mods)
}

private fun endorseRange(endorse: Boolean, args: List<String>) {
    val range = args.getRange(toolData.mods.size)
    if (range.isNotEmpty()) {
        val mods = range.map { toolData.mods[it] }
        endorseMods(endorse, mods)
    }
}

private fun endorseMods(endorse: Boolean, mods: List<Mod>) {
    val endorseLine = if (endorse) "Endorsed" else "Abstained"
    mods.filter { it.id != null }
        .also { if (it.size > 1) println("Endorsing ${it.size} mods") }
        .chunked(toolConfig.chunkSize)
        .forEach { chunk ->
            runBlocking {
                chunk.map { mod ->
                    async {
                        if (endorse){
                            nexus.endorseMod(toolConfig.apiKey!!, mod.id!!)
                        } else {
                            nexus.abstainMod(toolConfig.apiKey!!, mod.id!!)
                        }
                    }
                }.awaitAll()
            }
            println("$endorseLine ${chunk.joinToString { it.toString() }}")
        }
    save()
    if (endorse) println("Done Endorsing") else println("Done Abstaining")
}