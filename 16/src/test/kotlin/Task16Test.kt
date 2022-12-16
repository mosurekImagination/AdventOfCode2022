import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Task16Test {

    private val task = Task16()
    private val example = this::class.java.getResource("/example.txt").path
    private val test = this::class.java.getResource("/test.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(example)
        assertEquals(1651, result)
    }
    @Test
    fun `example should work to 10 seconds`() {
        val result = task.run(example, 10)
        assertEquals(246, result)
    }

    @Test
    fun `test should work`() {
        val result = task.run(test, 6)
        assertEquals(10, result)
    }

    @Test
    fun `getNeighbours should work`(){
        val extractInput = task.extractInput(
            """
            Valve AA has flow rate=0; tunnels lead to valves BB, CC, DD, FF
            Valve BB has flow rate=0; tunnels lead to valves AA, BB
            Valve CC has flow rate=0; tunnels lead to valves AA, DD
            Valve DD has flow rate=1; tunnels lead to valves AA, EE
            Valve EE has flow rate=2; tunnels lead to valves DD, BB
            Valve FF has flow rate=2; tunnels lead to valves AA, BB
        """.trimIndent()
        )

        val neighbours = task.getNeighbours(extractInput.first()).map { it.node.name}
        val neighbours2 = task.getNeighbours(extractInput[3]).map { it.node.name}
        assertEquals(listOf("DD", "FF", "EE"), neighbours)
        assertEquals(listOf("EE", "FF"), neighbours2)
    }

}