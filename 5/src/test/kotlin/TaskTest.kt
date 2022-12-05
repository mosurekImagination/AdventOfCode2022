import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskTest {

    private val task = Task()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task.run(example)
        assertEquals("CMZ", result)
    }

    @Test
    fun `example should return correct result for part 2`() {
        val result = task.run(example, true)
        assertEquals("MCD", result)
    }
//
//    @Test
//    fun `example should return correct result for overlapping`() {
//        val result = task.run(example, Task.overlapPredicate)
//        assertEquals(4, result)
//    }

}