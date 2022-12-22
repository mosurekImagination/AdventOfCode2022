import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Task22Test {

    private val task = Task22()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(File(example).readText())
        assertEquals(6032, result)
    }
    @Test
    fun `simple moving right should work`() {
        val input =
        """ |...
            |...
            |
            |2R""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,2,Task22.Direction.DOWN), result)
    }
    @Test
    fun `simple moving down should work`() {
        val input =
        """ |...
            |...
            |...
            |
            |1R2R""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(2,1,Task22.Direction.LEFT), result)
    }
    @Test
    fun `simple moving left should work`() {
        val input =
        """ |...
            |...
            |...
            |
            |2L2L2L2L""".trimMargin()
        val result = task.run(input, true, Task22.Status(0,0, Task22.Direction.TOP))
        assertEquals(Task22.Status(0,0,Task22.Direction.TOP), result)
    }
    @Test
    fun `left circle should work`() {
        val input =
        """ |...
            |...
            |
            |0L0L0L0L""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,0,Task22.Direction.RIGHT), result)
    }
    @Test
    fun `overflow should work`() {
        val input =
        """ |...
            |...
            |
            |3R0R3R0R""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,0,Task22.Direction.RIGHT), result)
    }
    @Test
    fun `wall should block`() {
        val input =
        """ |#..
            |...
            |
            |3R""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,2,Task22.Direction.DOWN), result)
    }

    @Test
    fun `custom overflow`() {
        val input =
        """ |...
            |...
            |...
            |. .
            |
            |1L1L""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(2,1,Task22.Direction.LEFT), result)
    }

    @Test
    fun `custom overflow with wall`() {
        val input =
        """ |...
            |...
            |.#.
            |. .
            |
            |1L1L""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,1,Task22.Direction.LEFT), result)
    }

    @Test
    fun `right custom overflow with wall`() {
        val input =
        """ | ... 
            | ... 
            |#.###
            |# ###
            |
            |3L3L3L3L""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,1,Task22.Direction.RIGHT), result)
    }

    @Test
    fun `longer overflow`() {
        val input =
        """ | ... 
            | ... 
            |#.###
            |# ###
            |
            |7L""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,2,Task22.Direction.TOP), result)
    }

    @Test
    fun `custom overflow with wall 2`() {
        val input =
        """ | .  
            | ... 
            |#####
            |# ###
            |
            |0L10L""".trimMargin()
        val result = task.run(input, true)
        assertEquals(Task22.Status(0,1,Task22.Direction.LEFT), result)
    }

    @Test
    fun `custom overflows`() {
        val input =
        """ | .. 
            |...
            |...
            | ...
            |  ..
            |
            |1L""".trimMargin()
        val result = task.run(input, true, Task22.Status(3, 3, Task22.Direction.TOP))
        assertEquals(Task22.Status(4,3,Task22.Direction.LEFT), result)
        val result2 = task.run(input, true, Task22.Status(4, 3, Task22.Direction.DOWN))
        assertEquals(Task22.Status(3,3,Task22.Direction.RIGHT), result2)
        val result3 = task.run(input, true, Task22.Status(1, 0, Task22.Direction.TOP))
        assertEquals(Task22.Status(2,0,Task22.Direction.LEFT), result3)
    }
    @Test
    fun `custom up overflows with walls`() {
        val input =
        """ | .. 
            |.#.
            |...
            | ...
            |  ..
            |
            |10L""".trimMargin()
        val result = task.run(input, true, Task22.Status(0, 1, Task22.Direction.TOP))
        assertEquals(Task22.Status(2,1,Task22.Direction.LEFT), result)
    }
    @Test
    fun `custom right left down overflows with walls at edges`() {
        val input =
        """ |.###.
            | #..
            | ...#
            | .
            |
            |10R10R10L10L""".trimMargin()
        val result = task.run(input, true, Task22.Status(1, 2, Task22.Direction.RIGHT))
        assertEquals(Task22.Status(3,1,Task22.Direction.RIGHT), result)
    }

}