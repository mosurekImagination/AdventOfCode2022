import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Task16Test {

    private val task = Task16()
    private val example = this::class.java.getResource("/example.txt").path
    private val test = this::class.java.getResource("/test.txt").path
    private val custom = this::class.java.getResource("/custom.txt").path

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
    fun `getNeighbours should work 2`() {
        val extractInput = task.extractInput(
            """
            Valve AA has flow rate=0; tunnels lead to valves BB
            Valve BB has flow rate=0; tunnels lead to valves AA, CC
            Valve CC has flow rate=0; tunnels lead to valves AA, DD
            Valve DD has flow rate=0; tunnels lead to valves DD, ZZ
            Valve ZZ has flow rate=10; tunnels lead to valves DD
        """.trimIndent()
        )
    }

    @Test
    fun `getNeighbours should work with loops`(){
        val extractInput = task.extractInput(
            """
            Valve AA has flow rate=0; tunnels lead to valves BB
            Valve BB has flow rate=0; tunnels lead to valves AA, CC, DD
            Valve CC has flow rate=5; tunnels lead to valves AA
            Valve DD has flow rate=0; tunnels lead to valves DD, ZZ
            Valve ZZ has flow rate=10; tunnels lead to valves DD
        """.trimIndent()
        )
    }

    @Test
    fun `going through non empty valves should work`(){
        val extractInput = task.extractInput(
            """
            Valve AA has flow rate=0; tunnels lead to valves BB
            Valve BB has flow rate=0; tunnels lead to valves CC
            Valve CC has flow rate=5; tunnels lead to valves DD
            Valve DD has flow rate=5; tunnels lead to valves AA
        """.trimIndent())
        val neighbours = task.getNeighbours(extractInput.first())
        val a = 5
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
            Valve XX has flow rate=2; tunnels lead to valves ZZ
            Valve ZZ has flow rate=0; tunnels lead to valves XX
        """.trimIndent()
        )

        val neighbours = task.getNeighbours(extractInput.first()).map { it.node.name}
        val neighbours2 = task.getNeighbours(extractInput[3]).map { it.node.name}
        assertEquals(listOf("DD", "FF", "EE"), neighbours)
        assertEquals(listOf("EE", "FF"), neighbours2)
    }

    @Test
    fun `custom test should work`(){
        val result = task.run(custom, 30)
        assertEquals(56, result)
    }

}