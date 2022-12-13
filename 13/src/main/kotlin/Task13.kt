import java.io.File

class Task13 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    private val additionalPackage2 = parseNode("[[2]]")
    private val additionalPackage6 = parseNode("[[6]]")

    fun part2(
        filePath: String
    ) = File(filePath)
        .readLines()
        .filter { it.isNotBlank() }
        .map { parseNode(it) }
        .run { this + listOf(additionalPackage2, additionalPackage6) }
        .sortedWith { a, b -> if (isInRightOrder(Pair(a, b))) -1 else 1 }
        .run {
            (indexOf(additionalPackage2) + 1) * (indexOf(additionalPackage6) + 1)
        }

    fun run(
        filePath: String
    ) = File(filePath)
        .readText()
        .split("\r\n\r\n")
        .map { it.split("\r\n") }
        .map { Pair(parseNode(it.first()), parseNode(it.last())) }
        .mapIndexed { index, it ->
            if (isInRightOrder(it))
                index + 1
            else 0
        }.sum()

    private fun isInRightOrder(pair: Pair<Node, Node>): Boolean {
        return compareNodes(pair.first, pair.second) ?: true
    }

    fun compareNodes(first: Node, second: Node): Boolean? {
        return when (first) {
            is ArrayNode -> when (second) {
                is IntNode -> compareNodes(first, ArrayNode(children = mutableListOf(second), second.parent))
                is ArrayNode ->
                    first.children.mapIndexed { index, node ->
                        val secondNode = second.children.getOrNull(index)
                        if (secondNode == null) false
                        else compareNodes(node, secondNode)
                    }
                        .filterNotNull()
                        .firstOrNull()
                        ?: (if (first.children.size == second.children.size) null else first.children.size < second.children.size)
            }

            is IntNode -> if (second is IntNode) {
                if (first.value == second.value) null
                else first.value < second.value
            } else
                compareNodes(ArrayNode(children = mutableListOf(first), first.parent), second)
        }
    }


    fun parseList(input: String): List<Any> = parseIntoNode(input, RootNode()).toList() as List<Any>
    fun parseNode(input: String): Node = parseIntoNode(input, RootNode())

    private fun parseIntoNode(input: String, currentNode: ArrayNode): Node =
        if (input currentOperation "openArray") {
            val newNode = ArrayNode(parent = currentNode)
            currentNode.children.add(newNode)
            parseIntoNode(moveForward(input), newNode)
        } else if (input currentOperation "closeArray")
            parseIntoNode(moveForward(input), currentNode.parent!!)
        else if (input currentOperation "comma")
            parseIntoNode(moveForward(input), currentNode)
        else if (input currentOperation "number") {
            val commaIndex = input.indexOfFirst { it == ',' }
            val intValue = input.substringBefore(",").toInt()
            currentNode.children.add(IntNode(intValue, currentNode))
            parseIntoNode(input.substring(commaIndex + 1), currentNode)
        } else if (input currentOperation "lastNumberInArray") {
            val arrayCloseIndex = input.indexOfFirst { it == ']' }
            val intValue = input.substringBefore("]").toInt()
            currentNode.children.add(IntNode(intValue, currentNode))
            parseIntoNode(input.substring(arrayCloseIndex), currentNode)
        } else currentNode


    private fun moveForward(input: String) = input.substring(1 until input.length)

    private infix fun String.currentOperation(operation: String): Boolean =
        when (operation) {
            "openArray" -> this.startsWith("[")
            "closeArray" -> this.startsWith("]")
            "number" -> this.substringBefore(",").toIntOrNull() != null
            "comma" -> this.startsWith(",")
            "lastNumberInArray" -> this.substringBefore("]").toIntOrNull() != null
            else -> false
        }

}

sealed class Node(open val parent: Node?)
class RootNode(children: MutableList<Node> = mutableListOf()) : ArrayNode(children, null)
class IntNode(val value: Int, override val parent: ArrayNode) : Node(parent)
open class ArrayNode(val children: MutableList<Node> = mutableListOf(), override val parent: ArrayNode?) : Node(parent)

fun Node.toList(): Any {
    return when (this) {
        is RootNode -> this.children.map { it.toList() }.first()
        is ArrayNode -> this.children.map { it.toList() }
        is IntNode -> this.value
        else -> throw RuntimeException()
    }
}
