package commands

import Mod
import blue
import cyan
import detectStagingChanges
import doCommand
import toolData
import yellow
import java.io.File

fun validateHelp() = """
    validate
    validate <mod index>
    validate 1 2 4
    validate 1-3
    validate staged
    validate enabled
    validate disabled
""".trimIndent()

fun validateMods(args: List<String>) {
   doCommand(args, List<Mod>::validate)
}

private fun List<Mod>.validate() {
    val errorMap = mutableMapOf<Int, Pair<Mod, MutableList<String>>>()

    addDupeIds(errorMap)
    addDupeFilenames(errorMap)
    detectStagingIssues(errorMap)
    detectDupePlugins()
    detectIncorrectCasing(errorMap)

    printErrors(errorMap.filter { it.value.first in this }.toMap())
    println(cyan("Validated $size mods"))
}

private fun List<Mod>.addDupeIds(
    errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>
) {
    groupBy { it.id }.filter { it.key != null && it.value.size > 1 }.map { it.value }.forEach { dupes ->
        val indexes = dupes.map { it.index }
        dupes.forEach { dupe ->
            errorMap.putIfAbsent(dupe.index, dupe to mutableListOf())
            errorMap[dupe.index]?.second?.add("Duplicate Id ($indexes)")
        }
    }
}

private fun List<Mod>.addDupeFilenames(
    errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>
) {
    groupBy { it.filePath }.filter { it.value.size > 1 }.map { it.value }.forEach { dupes ->
        val indexes = dupes.map { it.index }
        dupes.forEach { dupe ->
            errorMap.putIfAbsent(dupe.index, dupe to mutableListOf())
            errorMap[dupe.index]?.second?.add("Duplicate Filepath ($indexes)")
        }
    }
}


private fun List<Mod>.detectStagingIssues(
    errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>
) {
    forEach { mod ->
        val stageFolder = File(mod.filePath)
        if (stageFolder.exists()) {
            when (detectStagingChanges(stageFolder)) {
                StageChange.UNKNOWN -> {
                    errorMap.putIfAbsent(mod.index, mod to mutableListOf())
                    errorMap[mod.index]?.second?.add("Unable to guess folder path. You should open the staging folder and make sure it was installed correctly.")
                }

                StageChange.FOMOD -> {
                    errorMap.putIfAbsent(mod.index, mod to mutableListOf())
                    errorMap[mod.index]?.second?.add("FOMOD detected. You should open the staging folder and pick options yourself.")
                }

                StageChange.NO_FILES -> {
                    errorMap.putIfAbsent(mod.index, mod to mutableListOf())
                    errorMap[mod.index]?.second?.add("No files found in stage folder.")
                }

                else -> {}
            }
        }
    }
}

private fun List<Mod>.detectDupePlugins() {
    flatMap { mod ->
        mod.getModFiles().filter { it.extension.lowercase() in listOf("esp", "esm", "esl") }.map { it to mod.index }
    }
        .groupBy { it.first.name }
        .filter { it.value.size > 1 }
        .map { (fileName, indexList) ->
            fileName to indexList.groupBy { it.second }.keys
        }
        .forEach { (name, indexList) ->
            val modNames = indexList.joinToString(", ") { "$it ${toolData.byIndex(it)?.name}" }
            println("$name is duplicated in $modNames")
        }
}

private fun List<Mod>.detectIncorrectCasing(
    errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>
) {
    forEach { mod ->
        val modsPaths = mod.getModFiles()
            .map { it.parent }
            .mapNotNull { file ->
                val start = file.indexOf("Data") + 4
                val end = file.lastIndexOf("/")
                if (start < end) file.substring(start, end) else null
            }.toSet()
            .filter { it != it.lowercase() }
        if (modsPaths.isNotEmpty()) {
            errorMap.putIfAbsent(mod.index, mod to mutableListOf())
            errorMap[mod.index]?.second?.add("Filepaths should be lowercase between data and filename:")
            modsPaths.forEach {
                errorMap[mod.index]?.second?.add("\t${it}")
            }
        }
    }
}

private fun printErrors(errorMap: Map<Int, Pair<Mod, MutableList<String>>>) {
    errorMap.entries.forEach { (i, errorList) ->
        val (mod, errors) = errorList
        println("$i ${yellow(mod.name)} has issues:")
        errors.forEach { error ->
            println("\t$error")
        }
    }
}