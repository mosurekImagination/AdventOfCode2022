import java.io.File

class Task16 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun extractInput(input:String): List<Node> {
        return input.lines().map {
            val valves = Regex("[A-Z][A-Z]").findAll(it).map(MatchResult::value).toList()
            val flowRate = Regex("\\d+").findAll(it).map(MatchResult::value).toList().first()
            Pair(Node(valves.first(), flowRate.toInt(), emptyList(), Int.MIN_VALUE), valves.subList(1, valves.size))
        }.run {
            val nodes = this.map { it.first }
            this.map { (node, neighboursInput) ->
                node.neighbours = nodes.filter { neighboursInput.contains(it.name) }
                    .map { if (it.flowRate == 0) Path(1, it) else Path(2, it) }
            }
            nodes
        }
    }

    fun run(
        filePath: String,
        time: Int = 30
    ) = File(filePath).run{extractInput(this.readText())}
        .run {
        val emptyNodes = this.filter { it.flowRate == 0 }.toSet()
        val notEmptyNodes = this - emptyNodes
        notEmptyNodes.forEach { it.neighbours = getNeighbours(it, mutableSetOf(), 0) }
        val startingNode = emptyNodes.filter { it.name == "AA" }.first()
        startingNode.neighbours = getNeighbours(startingNode, mutableSetOf(), 0)
        startingNode.maxFlowRate = 0
        this - (emptyNodes.toSet() - startingNode).toSet()
    }
        .run {
            this.filter { it.name != "AA" }
                .forEach { if (it.neighbours.any { it.toEmptyNode() }) throw RuntimeException() }
            val startingNode = this.filter { it.name == "AA" }.first()
            val processNode = processNode(startingNode, time)
            processNode
        }

    private fun processNode(node: Node, remainingTime: Int): Int {
        return if (node.neighbours.all { node.containsInPrevious(it.node) } && remainingTime > 0) {
            println("Checking path: ${getPreviousList(node).reversed().map { it.name }.joinToString()}")
            node.flowRate * remainingTime
        } else if (remainingTime <= 0)
        {
            println("Out of time, checking path: ${getPreviousList(node).reversed().map { it.name }.joinToString()}")
            0
        }
        else {
            val result = node.flowRate * remainingTime + node.neighbours.filter { !node.containsInPrevious(it.node) }.maxOf {
                it.node.previousNode = node
                it.node.remainingMinutes = remainingTime
                processNode(it.node, remainingTime - it.cost)
            }
            println("Returning result: for ${getPreviousList(node).reversed().map { it.name }.joinToString()} + ${node.name}, $result")
            result
        }
    }

    fun getNeighbours(node: Node, visited: MutableSet<Path> = mutableSetOf(), depth: Int = 0): List<Path> {
        node.neighbours.forEach { p ->
            if (visited.none { it.node == p.node && it.cost < p.cost + depth }) {
                visited.add(Path(p.cost + depth, p.node))
                getNeighbours(p.node, visited, depth + 1)
            }
        }
        val notEmpty = visited.filter { !it.toEmptyNode() }
        return notEmpty.filter { it.node != node }.sortedBy { it.cost }.distinctBy { it.node }
    }

    fun Node.containsInPrevious(previous: Node): Boolean {
        return getPreviousList(this).contains(previous)
    }

    fun getPreviousList(node: Node): List<Node> {
        var current: Node? = node
        var list = mutableListOf<Node>()
        while (current != null) {
            list.add(current)
            current = current.previousNode
        }
        return list.toList()
    }

    fun Path.toEmptyNode() = this.node.flowRate == 0
    data class Path(
        val cost: Int,
        val node: Node
    )

    data class Node(
        val name: String,
        val flowRate: Int,
        var neighbours: List<Path>,
        var maxFlowRate: Int,
        var ownFlowRate: Int = 0,
        var previousNode: Node? = null,
        var remainingMinutes: Int = 30
    ) {
        override fun toString(): String {
            return "$name, flow Rate: $flowRate, maxFlow: $maxFlowRate, previous: ${previousNode?.name}, remainingMinutes: $remainingMinutes, ownFlowRate: $ownFlowRate"
        }

        override fun equals(other: Any?): Boolean {
            return this.name == (other as Node).name
        }

        override fun hashCode(): Int {
            return this.name.hashCode()
        }
    }
}
