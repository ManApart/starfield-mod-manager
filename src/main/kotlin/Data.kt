import kotlinx.serialization.Serializable
import nexus.ModInfo

@Serializable
data class Profile(val name: String, val ids: List<Int>, val filePaths: List<String>)

@Serializable
data class Data(
    val mods: MutableList<Mod> = mutableListOf(),
    val profiles: MutableList<Profile> = mutableListOf(),
) {
    fun byId(id: Int) = mods.firstOrNull { it.id == id }
    fun byIndex(i: Int) = mods.getOrNull(i).also { if (it == null) println("No Mod found for $i") }
    fun byName(name: String) = mods.firstOrNull { it.name == name }
    fun nextLoadOrder() = (mods.maxOfOrNull { it.loadOrder } ?: -1) + 1

    fun createOrUpdate(id: Int, name: String, filePath: String) {
        byId(id)?.also {
            it.name = name
            it.filePath = filePath
        } ?: Mod(name, filePath, nextLoadOrder(), id).also { mods.add(it) }
    }

    fun update(info: ModInfo, updateCurrentVersion: Boolean = true, updatedFileId: Int? = null) {
        byId(info.mod_id)?.apply {
            if (updateCurrentVersion) version = info.version
            latestVersion = info.version
            categoryId = info.category_id
            updatedFileId?.let {
                if (updateCurrentVersion) fileId = it
                latestFileId = it
            }
        }
    }

    fun profileByIndex(i: Int) = profiles.getOrNull(i).also { if (it == null) println("No Profile found for $i") }
}


