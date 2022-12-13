import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Task13Test {

    private val task = Task13()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should properly sort`() {
        val result = task.part2(example)
        assertEquals(140, result)
    }
    @Test
    fun `example should return correct length`() {
        val result = task.run(example)
        assertEquals(13, result)
    }

    @Test
    fun `should parse list`() {
        assertEquals(listOf<Int>(), task.parseList("[]"))
        assertEquals(listOf(listOf<Int>(), listOf<Int>()), task.parseList("[[],[]]"))
        assertEquals(listOf(5), task.parseList("[5]"))
        assertEquals(listOf(5, 4), task.parseList("[5,4]"))
        assertEquals(listOf(5, 4, listOf(1)), task.parseList("[5,4,[1]]"))
        assertEquals(listOf(5, 4, listOf<Int>()), task.parseList("[5,4,[]]"))
        assertEquals(listOf(listOf(listOf<Int>()), listOf(4, listOf<Int>())), task.parseList("[[[]],[4,[]]]"))

        assertEquals(listOf(listOf(1, listOf(2, listOf(10,8,2,1,1)),0)),  task.parseList("[[1,[2,[10,8,2,1,1]],0]]"))

    }

    @Test
    fun `compareNodes should work`() {
        val rootNode1 = RootNode()
        val arrayNode1 = ArrayNode(mutableListOf(), rootNode1)
        val intNode1 = IntNode(1, arrayNode1)
        val intNode12 = IntNode(1, arrayNode1)
        val intNode13 = IntNode(3, arrayNode1)
        val intNode14 = IntNode(7, arrayNode1)
        val intNode15 = IntNode(8, arrayNode1)
        arrayNode1.children.addAll(listOf(intNode1, intNode12, intNode13, intNode14, intNode15))
        rootNode1.children.add(arrayNode1)

        val rootNode2 = RootNode()
        val arrayNode2 = ArrayNode(mutableListOf(), rootNode1)
        val intNode2 = IntNode(1, arrayNode1)
        val intNode22 = IntNode(1, arrayNode1)
        val intNode23 = IntNode(5, arrayNode1)
        val intNode24 = IntNode(5, arrayNode1)
        val intNode25 = IntNode(6, arrayNode1)
        arrayNode2.children.addAll(listOf(intNode2, intNode22, intNode23, intNode24, intNode25))
        rootNode2.children.add(arrayNode2)

        val result = task.compareNodes(rootNode1, rootNode2)
        assertEquals(true, result)
    }

    @Test
    fun `second example should work`() {
        val list1 = task.parseNode("[[1],[2,3,4]]")
        val list2 = task.parseNode("[[1],4]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `third example should work`() {
        val list1 = task.parseNode("[9]")
        val list2 = task.parseNode("[[8,7,6]]")
        val result = task.compareNodes(list1, list2)
        assertEquals(false, result)
    }

    @Test
    fun `fourth example should work`() {
        val list1 = task.parseNode("[[4,4],4,4]")
        val list2 = task.parseNode("[[4,4],4,4,4]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `5 example`() {
        val list1 = task.parseNode("[7,7,7,7]")
        val list2 = task.parseNode("[7,7,7]")
        val result = task.compareNodes(list1, list2)
        assertEquals(false, result)
    }
    @Test
    fun `5 example 2`() {
        val list1 = task.parseNode("[7,7,7]")
        val list2 = task.parseNode("[7,7,7,7]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `6th example should work`() {
        val list1 = task.parseNode("[]")
        val list2 = task.parseNode("[3]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `7th example should work`() {
        val list1 = task.parseNode("[[[]]]")
        val list2 = task.parseNode("[[]]")
        val result = task.compareNodes(list1, list2)
        assertEquals(false, result)
    }

    @Test
    fun `8th example should work`() {
        val list1 = task.parseNode("[1,[2,[3,[4,[5,6,7]]]],8,9]")
        val list2 = task.parseNode("[1,[2,[3,[4,[5,6,0]]]],8,9]")
        val result = task.compareNodes(list1, list2)
        assertEquals(false, result)
    }

    @Test
    fun `0`() {
        val list1 = task.parseNode("[[2,3,4]]")
        val list2 = task.parseNode("[4]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `1`() {
        val list1 = task.parseNode("[[],[[]],4]")
        val list2 = task.parseNode("[[],[[]],3]")
        val result = task.compareNodes(list1, list2)
        assertEquals(false, result)
    }

    @Test
    fun `2`() {
        val list1 = task.parseNode("[[5,4],4]")
        val list2 = task.parseNode("[[[5,4]],4]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `3`() {
        val list1 = task.parseNode("[[],7]")
        val list2 = task.parseNode("[[3]]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `4`() {
        val list1 = task.parseNode("[[],3]")
        val list2 = task.parseNode("[[1,2,3]]")
        val result = task.compareNodes(list1, list2)
        assertEquals(true, result)
    }

    @Test
    fun `5`() {
        val list1 = task.parseNode("[[[[7],[2,5],[4,1,10,9]],[[],[6,0,2,1],[0],[7,0],9],8,[6],9],[4,[],[]],[2]]")
        val list2 = task.parseNode("[[7],[[6,6]]]")
        val result = task.compareNodes(list1, list2)
        assertEquals(false, result)
    }

    @Test
    fun `6`() {
        val list1 = task.parseNode("[[1,[2,[10,8,2,1,1]],0]]")
        val list2 =
            task.parseNode("[[[1]],[[[2,4,10,2],[]],3,8],[9,3,[5,[3,0],[0],[4]],6,[[9,8,3,7],4,[10,10,8],10,[6,6]]],[[[3],7,[],[10,5]],0],[5,[[3,9,0,2,1],0,[4,5,2],[6]]]]")
        val result = task.compareNodes(list1, list2)
        assertEquals(false, result)
    }
}