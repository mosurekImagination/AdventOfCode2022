import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskTest {

    private val task = Task()
    private val example = this::class.java.getResource("/example.txt").path
    private val allVisible = this::class.java.getResource("/allvisible.txt").path
    private val invisible = this::class.java.getResource("/invisible.txt").path
    private val leftRight = this::class.java.getResource("/leftRight.txt").path
    private val topDown = this::class.java.getResource("/topDown.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task.run(example)
        assertEquals(21, result)
    }

    @Test
    fun `should work with All visible`() {
        val result = task.run(allVisible)
        assertEquals(16, result)
    }

    @Test
    fun `should work with invisible`() {
        val result = task.run(invisible)
        assertEquals(14, result)
    }

    @Test
    fun `example should return correct result for part 2`() {
        val result = task.part2(example)
        assertEquals(8, result)
    }

    @Test
    fun `left-right view should work`() {
        val result = task.part2(leftRight)
        assertEquals(12, result)
    }

    @Test
    fun `top-down view should work`() {
        val result = task.part2(topDown)
        assertEquals(6, result)
    }

}