import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskTest {

    private val task = Task()
    private val example = this::class.java.getResource("/example.txt").path
    private val include = this::class.java.getResource("/include.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task.run(example)
        assertEquals(2, result)
    }

    @Test
    fun `example should return correct result for overlapping`() {
        val result = task.run(example, Task.overlapPredicate)
        assertEquals(4, result)
    }

    @Test
    fun `should include all test cases`() {
        val result = task.run(include)
        assertEquals(6, result)
    }

//    @Test
//    fun `example should return correct result in case of groupped backpacks`() {
//        val result = task.groups(example)
//        assertEquals(70, result)
//    }
}