import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Task17Test {

    private val task = Task17()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(example)
        assertEquals(3068, result)
    }

    @Test
    fun `cyclic sequence test`() {
        val sequence = listOf(1, 2, 3).run {
            asSequence()
        }.run {
            generateSequence(this) { this }.flatten()
        }
        assertEquals(1,sequence.elementAt(3))
        assertEquals(2,sequence.elementAt(4))
        assertEquals(3,sequence.elementAt(5))
        val iterator = sequence.iterator()
        assertEquals(1,iterator.next())
        assertEquals(2,iterator.next())
        assertEquals(3,iterator.next())
        assertEquals(1,iterator.next())
    }
}