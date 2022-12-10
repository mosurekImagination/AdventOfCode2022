import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Task10Test {

    private val task = Task10()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should return correct result`() {
        val result = task.run(example).signalStrength
        assertEquals(13140, result)
    }

    @Test
    fun `example should return correct result for part 2`() {
        val result = task.run(example).image
        assertEquals(listOf(
            "##..##..##..##..##..##..##..##..##..##..",
            "###...###...###...###...###...###...###.",
            "####....####....####....####....####....",
            "#####.....#####.....#####.....#####.....",
            "######......######......######......####",
            "#######.......#######.......#######.....")
            .flatMap { it.split("").filterNot{ it.isBlank()} }, result)
    }

}