import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Task21Test {

    private val task = Task21()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(File(example).readText())
        assertEquals("152".toBigDecimal(), result)
    }
    @Test
    fun `example should work for part 2`() {
        val result = task.run(File(example).readText(), true)
        assertEquals("301".toBigDecimal(), result)
    }
    @Test
    fun `part 2 should work for simple example with left + `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: humn + 2value
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals("3".toBigDecimal(), result)
    }
    @Test
    fun `part 2 should work for simple example with left - `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: humn - 2value
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals("7".toBigDecimal(), result)
    }
    @Test
    fun `part 2 should work for simple example with left division `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: humn / 2value
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals(10, result.toInt())
    }

    @Test
    fun `part 2 should work for simple example with left multiplication `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: humn * 2value
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals("2.5".toBigDecimal(), result)
    }
    @Test
    fun `part 2 should work for simple example with right + `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: 2value + humn
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals("3".toBigDecimal(), result)
    }
    @Test
    fun `part 2 should work for simple example with right - `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: 2value - humn
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals("-3".toBigDecimal(), result)
    }
    @Test
    fun `part 2 should work for simple example with right division `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: 2value / humn
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals("0.4".toBigDecimal(), result)
    }

    @Test
    fun `part 2 should work for simple example with right multiplication `() {
        val result = task.run(
            """
                root: human5 / 5value
                human5: 2value * humn
                humn: -1
                2value: 2
                5value: 4value + 1value
                4value: 4
                1value: 1
            """.trimIndent(), true)
        assertEquals("2.5".toBigDecimal(), result)
    }

}