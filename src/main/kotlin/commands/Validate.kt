package commands

import Mod
import detectStagingChanges
import toolData
import java.io.File

fun validateHelp() = """
    validate
    validate <mod index>
    validate 1 2 4
    validate 1-3
""".trimIndent()

fun validateMods(args: List<String>) {
    if (args.isEmpty()) {
        toolData.mods.validate()
    } else {
        args.getIndicesOrRange(toolData.mods.size)
            .mapNotNull { toolData.byIndex(it) }
            .validate()
    }
}

private fun List<Mod>.validate() {
    val errorMap = mutableMapOf<Int, Pair<Mod, MutableList<String>>>()
    val indexed = mapIndexed { i, mod -> mod to i }.toMap()

    addDupeIds(indexed, errorMap)
    addDupeFilenames(indexed, errorMap)
    detectStagingIssues(errorMap)

    printErrors(errorMap)
    println("Validation Complete")
}

private fun List<Mod>.addDupeIds(
    indexed: Map<Mod, Int>,
    errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>
) {
    groupBy { it.id }.filter { it.key != null && it.value.size > 1 }.map { it.value }.forEach { dupes ->
        val indexes = dupes.map { indexed[it]!! }
        dupes.forEach { dupe ->
            val i = indexed[dupe]!!
            errorMap.putIfAbsent(i, dupe to mutableListOf())
            errorMap[i]?.second?.add("Duplicate Id ($indexes)")
        }
    }
}

private fun List<Mod>.addDupeFilenames(
    indexed: Map<Mod, Int>,
    errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>
) {
    groupBy { it.filePath }.filter { it.value.size > 1 }.map { it.value }.forEach { dupes ->
        val indexes = dupes.map { indexed[it]!! }
        dupes.forEach { dupe ->
            val i = indexed[dupe]!!
            errorMap.putIfAbsent(i, dupe to mutableListOf())
            errorMap[i]?.second?.add("Duplicate Filepath ($indexes)")
        }
    }
}


private fun List<Mod>.detectStagingIssues(errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>) {
    forEachIndexed { i, mod ->
        val stageFolder = File(mod.filePath)
        if (stageFolder.exists()) {
            val change = detectStagingChanges(stageFolder)
            if (change == StageChange.UNKNOWN) {
                errorMap.putIfAbsent(i, mod to mutableListOf())
                errorMap[i]?.second?.add("Unable to guess folder path. You should open the staging folder and make sure it was installed correctly.")
            } else if (change == StageChange.FOMOD) {
                errorMap.putIfAbsent(i, mod to mutableListOf())
                errorMap[i]?.second?.add("FOMOD detected. You should open the staging folder and pick options yourself.")
            }
        }
    }
}

private fun printErrors(errorMap: MutableMap<Int, Pair<Mod, MutableList<String>>>) {
    errorMap.entries.forEach { (i, errorList) ->
        val (mod, errors) = errorList
        println("(i: $i) ${mod.name} has issues:")
        errors.forEach { error ->
            println("\t$error")
        }
    }
}