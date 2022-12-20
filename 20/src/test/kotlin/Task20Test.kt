import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Task20Test {

    private val task = Task20()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(File(example).readText())
        assertEquals("3".toBigInteger(), result)
    }

    @Test
    fun `example should work for part2`() {
        val result = task.run(File(example).readText(), "811589153".toBigInteger(),10)
        assertEquals("1623178306".toBigInteger(), result)
    }

    @Test
    fun `simple move should work 2`() {
        val result = task.getTargetList(
            """
            0
            0
            -1
        """.trimIndent()
        )
        assertEquals(listOf(0,-1,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `cyclic move should work`() {
        val result = task.getTargetList(
            """
            -1
            0
            0
        """.trimIndent()
        )
        assertEquals(listOf(0,-1,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `cyclic move should work 2`() {
        val result = task.getTargetList(
            """
            0
            0
            1
        """.trimIndent()
        )
        assertEquals(listOf(0,1,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `double cyclic should work 2`() {
        val result = task.getTargetList(
            """
            3
            0
            0
        """.trimIndent()
        )
        assertEquals(listOf(0,3,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `double cyclic should work 3`() {
        val result = task.getTargetList(
            """
            5
            0
            0
        """.trimIndent()
        )
        assertEquals(listOf(0,5,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `double cyclic should work 6`() {
        val result = task.getTargetList(
            """
            0
            0
            4
        """.trimIndent()
        )
        assertEquals(listOf(4,0,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `double cyclic should work 5`() {
        val result = task.getTargetList(
            """
            0
            0
            -5
        """.trimIndent()
        )
        assertEquals(listOf(0,-5,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `should work with duplicates`() {
        val result = task.getTargetList(
            """
            1
            2
            1
            0
        """.trimIndent()
        )
        assertEquals(listOf(1,2,1,0).map { it.toBigInteger() }, result)
    }

    @Test
    fun `should work with duplicates 2`() {
        val result = task.getTargetList(
            """
            1
            0
            1
            0
            1
            0
        """.trimIndent()
        )
        assertEquals(listOf(0,1,0,1,0,1).map { it.toBigInteger() }, result)
    }

}