import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Task14Test {

    private val task = Task14()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(example)
        assertEquals(24, result)
    }
    @Test
    fun `example should work for part 2`() {
        val result = task.run(example, Task14.part2Logic, Task14.part2BoardModification)
        assertEquals(93, result)
    }
}