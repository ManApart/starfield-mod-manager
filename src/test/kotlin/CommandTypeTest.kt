import commands.CommandType
import kotlin.test.Test
import kotlin.test.fail

class CommandTypeTest {

    @Test
    fun uniqueAliases() {
        val commands = (CommandType.entries.flatMap { it.aliases.toList() } + CommandType.entries.map { it.cleanName }).groupBy { it }.filter { it.value.size > 1 }
        if (commands.isNotEmpty()) {
            fail("The following commands or aliases are duplicated: ${commands.keys}")
        }
    }
}
