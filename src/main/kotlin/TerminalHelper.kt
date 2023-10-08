import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun File.runCommand(command: String): String? {
    val parts = command.split("\\s".toRegex())
    return runCommand(parts)
}
fun File.runCommand(parts: List<String>): String? {
    return try {
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(this)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        proc.inputStream.bufferedReader().readText()
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}