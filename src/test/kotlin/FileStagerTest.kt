import kotlin.test.Test
import kotlin.test.assertEquals

class FileStagerTest {

    @Test
    fun lowerCasePaths(){
        val stageFolderPath = "/some/Directory/Data/"
        val filePath = "Video/Menus/MainMenuLoop.bk2"
        val expected = "/some/Directory/Data/video/menus/MainMenuLoop.bk2"
        val actual = properlyCapitalize(stageFolderPath, filePath)
        assertEquals(expected, actual)
    }

    @Test
    fun makeDataUppercase(){
        val stageFolderPath = "/some/Directory/data/"
        val filePath = "Video/Menus/MainMenuLoop.bk2"
        val expected = "/some/Directory/Data/video/menus/MainMenuLoop.bk2"
        val actual = properlyCapitalize(stageFolderPath, filePath)
        assertEquals(expected, actual)
    }


}