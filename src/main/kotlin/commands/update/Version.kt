package commands.update

import Mod
import commands.getIndicesOrRange
import save
import toolData
import java.io.BufferedReader
import java.io.InputStreamReader

val versionDescription = """
    View the current and latest version of a mod
    Use version set to manually tell the manager that mode is at a given version
""".trimIndent()
val versionUsage = """
    version <index>
    version 1 2 4
    version 1-3
    version <index> set <new-version>
""".trimIndent()

fun version(command: String, args: List<String>) {
    when {
        args.isEmpty() -> {
            viewAppVersion()
            toolData.mods.viewVersion()
        }

        args.size == 3 && args[1] == "set" -> setVersion(args[0].toInt(), args.drop(2).joinToString(" "))
        else -> {
            args.getIndicesOrRange(toolData.mods.size)
                .mapNotNull { toolData.byIndex(it) }
                .viewVersion()
        }
    }
}

private fun List<Mod>.viewVersion() = forEach { it.viewVersion() }

private fun Mod.viewVersion() = println("$index $version -> $latestVersion $name")

private fun setVersion(i: Int, version: String) {
    toolData.byIndex(i)?.let { mod ->
        mod.version = version
        save()
        mod.viewVersion()
    }
}

fun viewAppVersion() {
    val version = ClassLoader.getSystemClassLoader().getResourceAsStream("commit.txt")
        .let { BufferedReader(InputStreamReader(it!!)).lines().toList().joinToString("\n") }
    println("Running Commit: $version")
}
