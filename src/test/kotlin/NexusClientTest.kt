import nexus.DownloadRequest
import nexus.parseDownloadRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class NexusClientTest {

    @Test
    fun basic(){
        val actual = parseDownloadRequest("nxm://starfield/mods/4183/files/12955?key=abcdefghijklmnop&expires=1697023374&user_id=111111")
        val expected = DownloadRequest(4183, 12955, "abcdefghijklmnop", "1697023374")

        assertEquals(expected, actual)
    }

    @Test
    fun parseFileExtension(){
        val actual = nexus.parseFileExtension("https://chicago-premium.nexus-cdn.com/4187/4183/Sleepy Time - Less Suggestive Wakeup Lines-4183-1-0-1695904632.zip?md5=aaaabbb0026expires=1696950578\\u0026user_id=11111")
        val expected = ".zip"

        assertEquals(expected, actual)
    }


}