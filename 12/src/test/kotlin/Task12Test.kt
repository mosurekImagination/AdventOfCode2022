import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigInteger

internal class Task12Test {

    private val task = Task12()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should return correct length`() {
        val result = task.run(example, 'S')
        assertEquals(31, result)
    }
    @Test
    fun `example should work for part 2`() {
        val result = task.run(example, 'a')
        assertEquals(29, result)
    }
}