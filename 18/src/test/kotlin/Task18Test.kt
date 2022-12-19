import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Task18Test {

    private val task = Task18()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(File(example).readText())
        assertEquals(64, result)
    }

    @Test
    fun `example should work for part2`() {
        val result = task.run(File(example).readText(), true)
        assertEquals(58, result)
    }

    @Test
    fun `simple input should work`() {
        val result = task.run(
            """1,1,1
            |2,1,1
        """.trimMargin())
        assertEquals(10, result)
    }

}