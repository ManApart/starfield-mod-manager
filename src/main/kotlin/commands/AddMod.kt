package commands

import HOME
import Mod
import modFolder
import nexus.downloadMod
import nexus.getDownloadUrl
import nexus.getModDetails
import nexus.parseDownloadRequest
import runCommand
import save
import toolConfig
import toolState
import java.io.File

fun addModHelp(args: List<String> = listOf()) = """
   add-mod file <path-to-mod-zip> <name-of-mod>*
""".trimIndent()

fun addMod(args: List<String>) {
    val subCommand = args.firstOrNull()
    when {
        args.size < 2 -> println(addModHelp())
//        subCommand == "id" -> addModById(args[1], args[2])
//        subCommand == "url" -> addModByUrl(args[1], args[2])
        subCommand == "file" -> addModByFile(args[1], args.getOrNull(2))

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

//private fun addModById(id: String, name: String){
//    println("Add by id $id $name")
//}
//private fun addModByUrl(url: String, name: String){
//    println("Add by url")
//}

fun addModByNexusProtocol(url: String) {
    val request = parseDownloadRequest(url)
    val modInfo = getModDetails(toolConfig.apiKey!!, request.modId)
    val modName = modInfo.name.lowercase()
    val filePath = modFolder.path + "/" + modName.replace(" ", "-")
    toolState.createOrUpdate(modInfo.mod_id, modName, filePath)
    toolState.update(modInfo, request.fileId)
    val mod = toolState.byId(modInfo.mod_id)!!
    save()
    val downloadUrl = getDownloadUrl(toolConfig.apiKey!!, request)
    val downloaded = downloadMod(downloadUrl)
    addModFile(mod, downloaded)
}

private fun addModByFile(filePath: String, nameOverride: String?) {
    val name = nameOverride ?: File(filePath).nameWithoutExtension
    val sourceFile = File(filePath.replace("~", HOME))

    val existing = toolState.byName(name)
    val mod = if (existing != null) existing else {
        val loadOrder = toolState.nextLoadOrder()
        val stagePath = modFolder.path + "/" + name.replace(" ", "-")
        Mod(name, stagePath, loadOrder + 1).also {
            toolState.mods.add(it)
            save()
        }
    }

    addModFile(mod, sourceFile)
}

private fun addModFile(mod: Mod, sourceFile: File) {
    if (!sourceFile.exists()) {
        println("Could not find ${sourceFile.absolutePath}")
        return
    }
    val stageFile = File(mod.filePath)
    val stageExists = stageFile.exists()
    if (stageMod(sourceFile, stageFile)) {
        if (stageExists) {
            println("Updated ${mod.name}")
        } else {
            println("Added ${mod.name}")
        }
    } else {
        println("Failed to add mod ${mod.name}")
    }
}

private fun stageMod(sourceFile: File, stageFolder: File): Boolean {
    stageFolder.mkdirs()
    if (sourceFile.isDirectory) {
        sourceFile.copyRecursively(stageFolder, overwrite = true)
    } else {
        stageFolder.runCommand(listOf("unzip", "-q", "-o", sourceFile.absolutePath))
    }
    return true
}