package nexus

import kotlinx.serialization.Serializable

data class DownloadRequest(val modId: Int, val fileId: Int, val key: String, val expires: String)

@Serializable
data class DownloadLink(val name: String, val short_name: String, val URI: String)

@Serializable
data class ModInfo(val name: String, val mod_id: Int, val category_id: Int, val version: String, val summary:String, val description: String, val endorsement: Endorsement)

enum class EndorseStatus(val stringValue: String){
    UNDECIDED("Undecided"),
    ENDORSED("Endorsed"),
    ABSTAINED("Abstained");
    fun isEndorsed()= when (this){
        ENDORSED -> true
        ABSTAINED -> false
        else -> null
    }
}
private fun String.endorseStatus() = EndorseStatus.entries.firstOrNull{it.stringValue == this} ?: EndorseStatus.UNDECIDED

@Serializable
data class Endorsement(val endorse_status: String) {
    val endorseStatus = endorse_status.endorseStatus()
}

@Serializable
data class ModFileInfo(val files: List<ModFileInfoFile>)

@Serializable
data class ModFileInfoFile(val file_id: Int, val name: String, val version: String, val is_primary: Boolean)


@Serializable
data class GameInfo(val id: Int, val categories: List<CategoryInfo>)

@Serializable
data class CategoryInfo(val category_id: Int, val name: String)
