import kotlin.test.Test
import kotlin.test.assertEquals

class InputProcessorTest {

    @Test
    fun basic(){
        val actual = "add flashy-weapons weapons.zip".parseArgs()
        val expected = listOf("add", "flashy-weapons", "weapons.zip")

        assertEquals(expected, actual)
    }

    @Test
    fun argsWithStrings(){
        val actual = "add \"flashy weapons\" weapons.zip".parseArgs()
        val expected = listOf("add", "flashy weapons", "weapons.zip")

        assertEquals(expected, actual)
    }

    @Test
    fun multipleArgsWithStrings(){
        val actual = "add \"flashy weapons\" \"weapons and stuff.zip\"".parseArgs()
        val expected = listOf("add", "flashy weapons", "weapons and stuff.zip")

        assertEquals(expected, actual)
    }

    @Test
    fun endsWithStrings(){
        val actual = "add \"flashy weapons\"".parseArgs()
        val expected = listOf("add", "flashy weapons")

        assertEquals(expected, actual)
    }


}