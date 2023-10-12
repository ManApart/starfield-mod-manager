package commands

import HOME
import Mod
import modFolder
import nexus.*
import runCommand
import save
import toolConfig
import toolState
import java.io.File

fun addModHelp(args: List<String> = listOf()) = """
   add nexus nxm://starfield/mods/4183/files/12955?key=abc&expires=1697023374&user_id=111
   add url https://www.nexusmods.com/starfield/mods/4183?tab=files
   add id 4183
   add id 4183 4182 4181 - Add multiple by id
   add-mod file <path-to-mod-zip> <name-of-mod>*
""".trimIndent()

fun addMod(args: List<String>) {
    val subCommand = args.firstOrNull()
    when {
        args.size < 2 -> println(addModHelp())
        subCommand == "nexus" -> addModByNexusProtocol(args[1])
        subCommand == "id" && args.size > 2 -> addModByIds(args.drop(1))
        subCommand == "id" -> addModById(args[1])
        subCommand == "url" -> addModByUrl(args[1])
        subCommand == "file" -> addModByFile(args[1], args.getOrNull(2))

        else -> println("Unknown args: ${args.joinToString(" ")}")
    }
}

private fun addModByUrl(url: String) {
    val id = url.replace("https://www.nexusmods.com/starfield/mods/", "").let {
        it.substring(0, it.indexOf("?"))
    }
    addModById(id)
}

private fun addModByIds(ids: List<String>) = ids.forEach { addModById(it) }
private fun addModById(id: String) {
    println("Adding $id")
    //TODO 
}


fun addModByNexusProtocol(url: String) {
    val request = parseDownloadRequest(url)
    val modInfo = getModDetails(toolConfig.apiKey!!, request.modId)
    val modName = modInfo.name.lowercase()
    val cleanName = modName.replace(" ", "-")
    val filePath = modFolder.path + "/" + cleanName
    toolState.createOrUpdate(modInfo.mod_id, modName, filePath)
    toolState.update(modInfo, request.fileId)
    val mod = toolState.byId(modInfo.mod_id)!!
    save()
    println("Downloading $modName")
    val downloadUrl = getDownloadUrl(toolConfig.apiKey!!, request)
    val destination = "$HOME/Downloads/$cleanName${parseFileExtension(downloadUrl)}"
    val downloaded = downloadMod(downloadUrl, destination)
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