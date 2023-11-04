import io.ktor.client.utils.EmptyContent.headers

fun String?.truncate(length: Int = 6): String {
    return this?.substring(0, kotlin.math.min(this.length, length)) ?: ""
}

data class Table(val columns: List<Column>, val data: List<Map<String, Any>>) {
    fun print(highlightHeaders: Boolean = true) {
        val colFormat = columns.joinToString("") { "%-${it.size}s" }
        val rowFormat = columns.joinToString("") {
            val type = if (it.isNumber) "d" else "s"
            "%-${it.size}$type"
        }

        val headerValues = columns.map { it.header }.toTypedArray()
        val headers = if (highlightHeaders) cyan("$colFormat\n") else "$colFormat\n"
        System.out.printf(headers, *headerValues)
        data.forEach { row ->
            val dataValues = columns.map { row[it.header] ?: "" }.toTypedArray()
            System.out.printf("$rowFormat\n", *dataValues)
        }
    }
}

data class Column(val header: String, val size: Int, val isNumber: Boolean = false)