import java.lang.Math.abs

class Task2(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(filePath: String, snakeSize: Int = 2, printingEnabled: Boolean = false, printSize: Int = 30) =
        inputReader.withFileLines(filePath) { l ->
            val startingPoint = Coordinate(0, 0)
            val visitedPositions = mutableSetOf(startingPoint)
            val tails = (0..snakeSize).map { startingPoint.copy() }.toMutableList()
            l.map { it.split(" ").run { Move(Direction.valueOf(this.first().toString()), this.last().toInt()) } }
                .flatMap { spreadMoves(it) }
                .fold(tails) { acc, move ->
                    if(printingEnabled)printCurrentStatus(acc, printSize)
                    acc.forEachIndexed { index, coordinate ->
                        acc[index] = if (index == 0) coordinate.move(move)
                        else {
                            val previous = acc[index - 1]
                            val current = acc[index]
                            if (!isSticky(previous, current)) {
                                val newPosition = calculateNewTailPosition(
                                    previous,
                                    current
                                )
                                println("$index $current: is not sticky with $previous, moving it to $newPosition")
                                newPosition
                            } else coordinate
                        }
                    }
                    println("----")
                    println("=====")
                    visitedPositions.add(acc.last())
                    acc
                }
            if(printingEnabled) printVisitedMatrix(visitedPositions, printSize)
            visitedPositions.size
        }

    fun isSticky(headCoordinate: Coordinate, tailCoordinate: Coordinate) =
        (headCoordinate.x - 1..headCoordinate.x + 1).contains(tailCoordinate.x) &&
                (headCoordinate.y - 1..headCoordinate.y + 1).contains(tailCoordinate.y)


    fun calculateNewTailPosition(
        previous: Coordinate,
        current: Coordinate,
    ): Coordinate {
        return if (previous.x == current.x) Coordinate(current.x, listOf(previous.y, current.y).average().toInt()) else
            if (previous.y == current.y) Coordinate(listOf(previous.x, current.x).average().toInt(), current.y) else
                if(abs(previous.y - current.y) == 2 && abs(previous.x-current.x ) == 2)
                    Coordinate(
                        listOf(previous.x, current.x).average().toInt(),
                        listOf(previous.y, current.y).average().toInt()
                    )
                else if (abs(previous.y - current.y) == 2) Coordinate(
                    current.x,
                    listOf(previous.y, current.y).average().toInt()
                )
                else Coordinate(listOf(previous.x, current.x).average().toInt(), current.y)
    }

fun printCurrentStatus(tails: List<Coordinate>, printSize: Int) {
    val matrix = (0..printSize).map { ((0..printSize).map { "." }.toMutableList()) }
    tails.forEachIndexed { index, it ->
        matrix[it.y][it.x] = if(index== 0 ) "H" else (index).toString()
    }
    matrix.reversed().map {
        it.map {
            print(it)
        }
        println()
    }
}

fun printVisitedMatrix(coordinates: Set<Coordinate>, printSize: Int) {
    val matrix = (0..printSize).map { ((0..printSize).map { "." }.toMutableList()) }
    coordinates.forEach {
        matrix[it.y][it.x] = "X"
    }
    matrix.reversed().map {
        it.map {
            print(it)
        }
        println()
    }
}

    private fun spreadMoves(move: Move): List<Move> =
        (0 until move.length).map { Move(move.direction, 1) }

    enum class Direction(private val directionName: String) {
        R("Right"),
        U("Up"),
        L("Left"),
        D("Down")
    }

    data class Coordinate(val x: Int, val y: Int) {
        fun move(move: Move) =
            when (move.direction) {
                Direction.R -> Coordinate(x + move.length, y)
                Direction.U -> Coordinate(x, y + move.length)
                Direction.L -> Coordinate(x - move.length, y)
                Direction.D -> Coordinate(x, y - move.length)
            }
    }

    data class Move(val direction: Direction, val length: Int)

}

