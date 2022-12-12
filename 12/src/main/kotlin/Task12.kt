import java.util.concurrent.atomic.AtomicInteger

class Task12(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(
        filePath: String,
        startingPointChar: Char = 'S'
    ) = inputReader.withFileLines(filePath) { lines ->
        lines.reversed().mapIndexed { yi, line ->
            line.split("").filter { it.isNotBlank() }
                .map { it.first() }
        }.run {
            (0 until this.first().size).map { x ->
                (0 until this.size).map { y ->
                    this[y][x]
                }
            }
        }.mapIndexed { xi, column ->
            column.mapIndexed { yi, it ->
                when (it) {
                    'S' -> 'a'.code
                    'E' -> 'z'.code
                    else -> it.code
                }.run { Field(it, this, Coordinate(xi, yi), emptyList(), Int.MAX_VALUE) }
            }
        }
    }.run {
        this as Matrix<Field>
        val fields = this.flatMap { it.map { it } }
        val aStartingPoint = fields.filter { it.charValue == startingPointChar }
        val startingPointSize = aStartingPoint.size
        println("Possible starting points: $startingPointSize")
        val count = AtomicInteger(0)
        aStartingPoint.parallelStream().mapToInt { startField ->
            val matrixCopy = this.map { it.map { it.copy() } }
            val fieldsCopy =
                matrixCopy.flatMap { it.map { it } }.map { it.neighbors = getFieldNeighbours(matrixCopy, it); it }
            val startFieldCopy = fieldsCopy.find { it.coordinate == startField.coordinate }!!
            startFieldCopy.travelCost = 0
            val endingField = fieldsCopy.find { it.charValue == 'E' }!!
            fillFieldsTravelCostViaDjikstraAlgorithm(startFieldCopy, fieldsCopy)
            val loopNumber = count.incrementAndGet()
            println("Calculated $loopNumber/$startingPointSize travelCost")
            endingField.travelCost
        }.filter { it > 0 }.min().asInt
    }

    private fun fillFieldsTravelCostViaDjikstraAlgorithm(startingPoint: Field, fields: List<Field>) {
        val visitedFields = mutableListOf<Field>()
        val newFields = fields.toMutableList()
        processField(startingPoint, visitedFields, newFields)
        while (newFields.isNotEmpty()) {
            newFields.sortBy { it.travelCost }
            val field = newFields.first()
            processField(field, visitedFields, newFields)
        }
    }

    private fun processField(field: Field, visitedFields: MutableList<Field>, newFields: MutableList<Field>) {
        newFields.removeIf { it.coordinate == field.coordinate }
        visitedFields.add(field)
        field.neighbors.forEach { neighbour ->
            if (field.travelCost + 1 < neighbour.travelCost) {
                neighbour.travelCost = field.travelCost + 1
            }
        }
    }

    private fun getFieldNeighbours(
        matrix: Matrix<Field>,
        fromPoint: Field
    ): List<Field> {
        val xSize = matrix.size
        val ySize = matrix.first().size
        return listOfNotNull(
            matrix.getOrNull(fromPoint.coordinate.x - 1)?.getOrNull(fromPoint.coordinate.y),
            matrix.getOrNull(fromPoint.coordinate.x + 1)?.getOrNull(fromPoint.coordinate.y),
            matrix.getOrNull(fromPoint.coordinate.x)?.getOrNull(fromPoint.coordinate.y + 1),
            matrix.getOrNull(fromPoint.coordinate.x)?.getOrNull(fromPoint.coordinate.y - 1)
        )
            .filter { it.coordinate.x in 0 until xSize }
            .filter { it.coordinate.y in 0 until ySize }
            .filter { fromPoint.value + 1 >= it.value }
    }

    data class Coordinate(val x: Int, val y: Int)
    data class Field(
        val charValue: Char,
        val value: Int,
        val coordinate: Coordinate,
        var neighbors: List<Field>,
        var travelCost: Int
    )
}
typealias Matrix<T> = List<List<T>>


