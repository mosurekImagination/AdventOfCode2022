import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskTest {

    private val task = Task()
    private val task2 = Task2()
    private val example = this::class.java.getResource("/example.txt").path
    private val diagonal = this::class.java.getResource("/diagonal.txt").path
    private val simple = this::class.java.getResource("/simple.txt").path
    private val large = this::class.java.getResource("/largeExample.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task2.run(example, 1, true)
        assertEquals(13, result)
    }

    @Test
    fun `should work with diagonal`() {
        val result = task2.run(diagonal ,1)
        assertEquals(10, result)
    }

    @Test
    fun `should work with simple`() {
        val result = task2.run(simple, 1)
        assertEquals(10, result)
    }

    @Test
    fun `should work for part 2 example`() {
        val result = task2.run(example,9)
        assertEquals(1, result)
    }

    @Test
    fun `should for part 2 large example`() {
        val result = task2.run(large, 9)
        assertEquals(36, result)
    }

}