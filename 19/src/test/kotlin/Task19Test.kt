import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Task19Test {

    private val task = Task19()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(File(example).readText())
        assertEquals(33, result)
    }

}