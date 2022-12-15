import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Task15Test {

    private val task = Task15()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `mapPrinting`() {
        task.print(example)
    }

    @Test
    fun `example should work`() {
        val result = task.run(example)
        assertEquals(26, result)
    }

    @Test
    fun `part2 should work`() {
        val result = task.part2(example,20)
        assertEquals(56000011, result)
    }
}