import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskTest {

    private val task = Task()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task.run(example)
        assertEquals(7, result)
    }

    @Test
    fun `example should return correct result for part 2`() {
        val result = task.run(example, Task.PART_2_MARKER_LENGTH)
        assertEquals(19, result)
    }

}