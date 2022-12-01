import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TaskTest{

    private val task = Task()

    private val first = this::class.java.getResource("/first.txt").path
    private val middle = this::class.java.getResource("/middle.txt").path
    private val last = this::class.java.getResource("/last.txt").path
    private val exampleInput = this::class.java.getResource("/exampleInput.txt").path

    @Test
    fun `should find elf if it is first`(){
        val result = task.run(first)
        assertEquals(3, result)
    }

    @Test
    fun `should find elf if it is in the middle`(){
        val result = task.run(middle)
        assertEquals(4, result)
    }

    @Test
    fun `should find elf if it is last`(){
        val result = task.run(last)
        assertEquals( 6, result)
    }

    @Test
    fun `should work for example`(){
        val result = task.run(exampleInput, 3)
        assertEquals( 45000, result)
    }
}