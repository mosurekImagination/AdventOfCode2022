import java.math.BigDecimal
import java.math.RoundingMode

class Task21 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    private val HUMN = "humn"
    private val ROOT = "root"
    private val emptyNode = EmptyNode()

    fun run(
        input: String, part2Enabled: Boolean = false
    ) = input.lines().map { it.split(": ") }.map {
            val nodeName = it.first()
            if (it.last().toBigDecimalOrNull() != null) ValueNode(nodeName, it.last().toBigDecimal())
            else {
                val operationSplit = it.last().split(" ")
                OperationNode(
                    nodeName,
                    emptyNode,
                    emptyNode,
                    Operation.values().first { it.sign == operationSplit[1] },
                    operationSplit.first(),
                    operationSplit.last()
                )
            }
        }.run {
            val nodes = this
            nodes.forEach { node ->
                if (node is OperationNode) {
                    node.left = nodes.first { it.name == node.leftInput }
                    node.right = nodes.first { it.name == node.rightInput }
                }
            }
            nodes
        }.run {
            if (part2Enabled) {
                val nodes = this.toMutableList()
                val rootNode = nodes.first { it.name == ROOT } as OperationNode
                val humanNode = nodes.first { it.name == HUMN } as ValueNode
                val humanParentNodeLeft =
                    nodes.filter { it is OperationNode }.filter { (it as OperationNode).left.name == HUMN }
                val humanParentNodeRight =
                    nodes.filter { it is OperationNode }.filter { (it as OperationNode).right.name == HUMN }
                nodes.remove(rootNode)
                nodes.remove(humanNode)
                nodes.add(RootNode(rootNode.name, rootNode.left, rootNode.right))
                val newHumanNode = HumanNode(humanNode.name, "-1".toBigDecimal())
                nodes.add(newHumanNode)
                humanParentNodeLeft.forEach { (it as OperationNode).left = newHumanNode }
                humanParentNodeRight.forEach { (it as OperationNode).right = newHumanNode }
                nodes.toList()
            } else this
        }.run {
            val nodes = this
            val rootNodeSolve = nodes.first { it.name == ROOT }.solve()
            if (part2Enabled) {
                (nodes.first { it.name == HUMN } as HumanNode).value.stripTrailingZeros()
            } else {
                rootNodeSolve
            }
        }

    enum class Operation(val sign: String) {
        PLUS("+"), MINUS("-"), DIVIDE("/"), MULTIPLY("*")
    }

    class EmptyNode(override val name: String = "EMPTY") : Node {
        override fun solve() = throw RuntimeException("Empty Node should be changed to another implementation")

        override fun containsHumanNodeNode() =
            throw RuntimeException("Empty Node should be changed to another implementation")

        override fun solveToValue(targetValue: BigDecimal) =
            throw RuntimeException("Empty Node should be changed to another implementation")
    }

    sealed interface Node {
        fun solve(): BigDecimal

        fun containsHumanNodeNode(): Boolean
        val name: String
        fun solveToValue(targetValue: BigDecimal): BigDecimal
    }

    data class OperationNode(
        override val name: String,
        var left: Node,
        var right: Node,
        val operation: Operation,
        val leftInput: String,
        val rightInput: String
    ) : Node {
        override fun solve(): BigDecimal = when (operation) {
            Operation.PLUS -> left.solve() + right.solve()
            Operation.MINUS -> left.solve() - right.solve()
            Operation.DIVIDE -> left.solve() / right.solve()
            Operation.MULTIPLY -> left.solve() * right.solve()
        }

        override fun containsHumanNodeNode(): Boolean {
            return left.containsHumanNodeNode() || right.containsHumanNodeNode()
        }

        override fun solveToValue(targetValue: BigDecimal): BigDecimal {
            if (left.containsHumanNodeNode()) {
                val rightValue = right.solve()
                val requiredValue = when (operation) {
                    Operation.PLUS -> targetValue - rightValue
                    Operation.MINUS -> targetValue + rightValue
                    Operation.DIVIDE -> targetValue * rightValue
                    Operation.MULTIPLY -> targetValue.divide(rightValue, 100, RoundingMode.DOWN)
                }
                left.solveToValue(requiredValue)
            } else {
                val leftValue = left.solve()
                val requiredValue = when (operation) {
                    Operation.PLUS -> targetValue - leftValue
                    Operation.MINUS -> leftValue - targetValue
                    Operation.DIVIDE -> leftValue.divide(targetValue, 100, RoundingMode.DOWN)
                    Operation.MULTIPLY -> targetValue.divide(leftValue, 100, RoundingMode.DOWN)
                }
                right.solveToValue(requiredValue)
            }
            return targetValue
        }
    }

    data class ValueNode(override val name: String, val value: BigDecimal) : Node {
        override fun solve() = value
        override fun containsHumanNodeNode(): Boolean = false
        override fun solveToValue(targetValue: BigDecimal) = throw RuntimeException("Value node cannot solve to value")
    }

    data class RootNode(
        override val name: String, val left: Node, val right: Node
    ) : Node {
        override fun solve() = if (left.containsHumanNodeNode()) {
            val rightValue = right.solve()
            left.solveToValue(rightValue)
        } else {
            val leftValue = left.solve()
            right.solveToValue(leftValue)
        }

        override fun containsHumanNodeNode(): Boolean = true
        override fun solveToValue(targetValue: BigDecimal): BigDecimal =
            throw RuntimeException("Root node cannot solve to value")
    }

    data class HumanNode(override val name: String, var value: BigDecimal) : Node {
        override fun solve() = throw RuntimeException("Human node cannot just solve")
        override fun solveToValue(targetValue: BigDecimal): BigDecimal {
            value = targetValue
            return value
        }

        override fun containsHumanNodeNode(): Boolean = true
    }
}


