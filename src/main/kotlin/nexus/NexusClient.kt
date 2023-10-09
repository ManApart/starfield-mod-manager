package nexus

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

private val client = HttpClient()

data class DownloadRequest(val modId: String, val fileId: String, val key: String, val expires: String)

data class DownloadLink(val name: String, val short_name: String, val URI: String)

fun parseDownloadRequest(url: String): DownloadRequest {
    val modId = url.between("mods/", "/")
    val fileId = url.between("files/", "?key")
    val key = url.between("?key=", "&")
    val expires = url.between("&expires=", "&")
    return DownloadRequest(modId, fileId, key, expires)
}

private fun String.between(start: String, end: String): String {
    val first = indexOf(start) + start.length
    val last = indexOf(end, first)
    return this.substring(first, last)
}

fun getDownloadUrl(apiKey: String, downloadRequest: DownloadRequest): String {
    val links: List<DownloadLink> = runBlocking {
        with(downloadRequest) {
            client.get("https://api.nexusmods.com/v1/games/starfield/mods/$modId/files/$fileId/download_link.json?key=$key&expires=$expires") {
                header("accept", "application/json")
                header("apikey", apiKey)
            }.body()
        }
    }

    return links.first().URI
}

fun downloadMod(url: String) {
    //todo - download blob
    //save to user downloads
}