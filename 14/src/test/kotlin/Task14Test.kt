import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Task14Test {

    private val task = Task14()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should properly sort`() {
        val result = task.run(example)
        assertEquals(24, result)
    }
}