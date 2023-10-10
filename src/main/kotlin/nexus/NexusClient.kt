package nexus

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import jsonMapper
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

private val client = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

fun parseDownloadRequest(url: String): DownloadRequest {
    val modId = url.between("mods/", "/").toInt()
    val fileId = url.between("files/", "?key").toInt()
    val key = url.between("?key=", "&")
    val expires = url.between("&expires=", "&")
    return DownloadRequest(modId, fileId, key, expires)
}

private fun String.between(start: String, end: String): String {
    val first = indexOf(start) + start.length
    val last = indexOf(end, first)
    return this.substring(first, last)
}

fun getModDetails(apiKey: String, id: Int): ModInfo {
    return runBlocking {
        client.get("https://api.nexusmods.com/v1/games/starfield/mods/$id.json") {
            header("accept", "application/json")
            header("apikey", apiKey)
        }.body()
    }
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

fun downloadMod(url: String, destination: String): File {
    val result = File(destination)
    runBlocking {
        client.get(url) {
        }.bodyAsChannel().copyAndClose(result.writeChannel())
    }
    return result
}