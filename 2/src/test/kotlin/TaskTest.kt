import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskTest {

    private val task = Task()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task.run(example)
        assertEquals(15, result)
    }

    @Test
    fun `example should return correct result in case of planned moves`() {
        val result = task.run(example, true)
        assertEquals(12, result)
    }
}