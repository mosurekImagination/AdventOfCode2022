import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger

internal class Task11Test {

    private val task = Task11()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task.run(example)
        assertEquals(10605, result)
    }
    @Test
    fun `should work with big numbers`() {
        val leastCommonMultiple = "96577".toBigInteger()
        val worryRelief: (BigInteger) -> BigInteger = {
            it % leastCommonMultiple
        }
        val result = task.run(example, 10000, worryRelief)
        assertEquals(2713310158L, result)
    }

    @Test
    fun test() {

    }
}