package commands

import Mod
import toolData
import java.io.File

fun deployDryRun() {
    getAnnotatedModFiles().forEach { entry ->
        println(entry.key)
        val winner = entry.value.first()
        println("\tDeploy: (${winner.loadOrder}) ${winner.idName()}")
        entry.value.drop(1).takeIf { it.isNotEmpty() }?.let { others ->
            println("\tNot used: ${others.joinToString { "(${it.loadOrder}) " + it.idName() }}")
        }
    }
    println("Deploy dryrun complete")
}

private fun getAnnotatedModFiles(): Map<String, List<Mod>> {
    val mappings = mutableMapOf<String, MutableList<Mod>>()
    toolData.mods.filter { it.enabled }.sortedByDescending { it.loadOrder }.forEach { mod ->
        val modRoot = File(mod.filePath).absolutePath + "/"
        mod.getModFiles().forEach { file ->
            val key = file.absolutePath.replace(modRoot, "")
            mappings.putIfAbsent(key, mutableListOf())
            mappings[key]?.add(mod)
        }
    }
    return mappings
}