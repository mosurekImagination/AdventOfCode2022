import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Task23Test {

    private val task = Task23()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(File(example).readText())
        assertEquals("4890".toBigInteger(), result)
    }
    @Test
    fun `simple examples should work`() {
        val lists = listOf(
            Pair("12", "7")
        )
        lists.forEach {
            assertEquals(it.second.toBigInteger(), task.parseNumber(it.first),"${it.first} is not equal ${it.second}")
        }
    }

    @Test
    fun `simple examples should work for encoding`() {
        val lists = listOf(
            Pair("2", "2"),
            Pair("3", "1="),
            Pair("10", "20"),
            Pair("15", "1=0"),
            Pair("314159265", "1121-1110-1=0"),
        )
        lists.forEach {
            assertEquals(it.second.length, task.encodeNumber(it.first.toBigInteger()).length,"${it.first} is not equal ${it.second}")
        }
    }

}