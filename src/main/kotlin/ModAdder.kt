import nexus.*
import java.io.File

fun addModById(id: Int, fileId: Int? = null) {
    val mod = fetchModInfo(id, fileId) ?: return
    val cleanName = mod.name.replace(" ", "-")
    println("Downloading $id: ${mod.name}")
    val downloadUrl = getDownloadUrl(toolConfig.apiKey!!, mod.id!!, mod.fileId!!)
    val destination = "$HOME/Downloads/$cleanName${parseFileExtension(downloadUrl)}"
    val downloaded = downloadMod(downloadUrl, destination)
    addModFile(mod, downloaded, mod.name)
}

fun fetchModInfo(id: Int, fileId: Int? = null): Mod? {
    val modInfo = getModDetails(toolConfig.apiKey!!, id)
    val modFileId = fileId ?: getModFiles(toolConfig.apiKey!!, id).files.firstOrNull { it.is_primary }?.file_id

    if (modFileId == null) {
        println("Could not find primary file for $id")
        return null
    }

    val modName = modInfo.name.lowercase()
    val cleanName = modName.replace(" ", "-")
    val filePath = modFolder.path + "/" + cleanName
    toolState.createOrUpdate(modInfo.mod_id, modName, filePath)
    toolState.update(modInfo, modFileId)
    return toolState.byId(modInfo.mod_id)!!.also { save() }
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
    addModFile(mod, downloaded, modName)
}

fun addModByFile(filePath: String, nameOverride: String?) {
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

    addModFile(mod, sourceFile, name)
}

fun addModFile(mod: Mod, sourceFile: File, modName: String) {
    if (!sourceFile.exists()) {
        println("Could not find ${sourceFile.absolutePath}")
        return
    }
    val stageFile = File(mod.filePath)
    val stageExists = stageFile.exists()
    if (stageMod(sourceFile, stageFile, modName)) {
        if (stageExists) {
            println("Updated ${mod.name}")
        } else {
            println("Added ${mod.name}")
        }
    } else {
        println("Failed to add mod ${mod.name}")
    }
}
