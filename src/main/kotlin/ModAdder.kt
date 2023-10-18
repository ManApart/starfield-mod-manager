import nexus.*
import java.io.File
import kotlin.math.max

fun addModById(id: Int, fileId: Int? = null) {
    val mod = fetchModInfo(id, fileId) ?: return
    val cleanName = mod.name.replace(" ", "-")
    println("Downloading $id: ${mod.name}")
    val downloadUrl = getDownloadUrl(toolConfig.apiKey!!, mod.id!!, mod.fileId!!)
    if (downloadUrl == null) {
        println("Unable to get download url for ${mod.name}")
        return
    }
    val destination = "$HOME/Downloads/starfield-mods/$cleanName${parseFileExtension(downloadUrl)}"
    val downloaded = downloadMod(downloadUrl, destination)
    if (downloaded == null) {
        println("Failed to download ${mod.name}")
    } else {
        addModFile(mod, downloaded, mod.name)
    }
}

fun fetchModInfo(id: Int, fileId: Int? = null): Mod? {
    val modInfo = getModDetails(toolConfig.apiKey!!, id)
        ?: return null.also { println("Unable to get mod info for $id") }
    val modFileId = fileId ?: getModFiles(toolConfig.apiKey!!, id)?.getPrimaryFile()

    if (modFileId == null) {
        println("Could not find primary file for $id")
        return null
    }

    val modName = modInfo.name.lowercase()
    val cleanName = modName.replace(" ", "-")
    val filePath = modFolder.path + "/" + cleanName
    toolData.createOrUpdate(modInfo.mod_id, modName, filePath)
    toolData.update(modInfo, modFileId)
    return toolData.byId(modInfo.mod_id)!!.also { save() }
}

private val versionComparator = Comparator { fileA: ModFileInfoFile, fileB: ModFileInfoFile ->
    val versionA = fileA.version.split(".").mapNotNull { it.toIntOrNull() }
    val versionB = fileB.version.split(".").mapNotNull { it.toIntOrNull() }
    val max = max(versionA.size, versionB.size)
    (0..max).forEach { i ->
        val a = versionA.getOrNull(i) ?: 0
        val b = versionB.getOrNull(i) ?: 0
        when {
            a > b -> return@Comparator -1
            a < b -> return@Comparator 1
            else -> {}
        }
    }
    0
}

fun ModFileInfo.getPrimaryFile(): Int? {
    return files.let { files ->
        if (files.size == 1) files.first().file_id else {
            files.firstOrNull { it.is_primary }?.file_id
        } ?: files.sortedWith(versionComparator).firstOrNull()?.file_id
    }
}


fun addModByNexusProtocol(url: String) {
    val request = parseDownloadRequest(url)
    val modInfo = getModDetails(toolConfig.apiKey!!, request.modId)
    if (modInfo == null) {
        println("Unable to download $url")
        return
    }
    val modName = modInfo.name.lowercase()
    val cleanName = modName.replace(" ", "-")
    val filePath = modFolder.path + "/" + cleanName
    toolData.createOrUpdate(modInfo.mod_id, modName, filePath)
    toolData.update(modInfo, request.fileId)
    val mod = toolData.byId(modInfo.mod_id)!!
    save()
    println("Downloading $modName")
    val downloadUrl = getDownloadUrl(toolConfig.apiKey!!, request)
    if (downloadUrl == null) {
        println("Unable to get download url for $modName")
        return
    }
    val destination = "$HOME/Downloads/starfield-mods/$cleanName${parseFileExtension(downloadUrl)}"
    val downloaded = downloadMod(downloadUrl, destination)
    if (downloaded == null) {
        println("Failed to download ${mod.name}")
    } else {
        addModFile(mod, downloaded, modName)
    }
}

fun addModByFile(filePath: String, nameOverride: String?) {
    val name = nameOverride ?: File(filePath).nameWithoutExtension
    val sourceFile = File(filePath)

    val existing = toolData.byName(name)
    val mod = if (existing != null) existing else {
        val loadOrder = toolData.nextLoadOrder()
        val stagePath = modFolder.path + "/" + name.replace(" ", "-")
        Mod(name, stagePath, loadOrder + 1).also {
            toolData.mods.add(it)
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
