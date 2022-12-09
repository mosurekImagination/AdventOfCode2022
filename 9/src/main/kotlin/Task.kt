class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }


    fun run(filePath: String) =
        inputReader.withFileLines(filePath) { l ->
            val startingPoint = Coordinate(0, 0)
            val visitedPositions = mutableSetOf(startingPoint)
            l.map { it.split(" ").run { Move(Direction.valueOf(this.first().toString()), this.last().toInt()) } }
                .flatMap { spreadMoves(it) }
                .fold(CurrentStatus(startingPoint, startingPoint)) { acc, move ->
//                    printCurrentStatus(acc)
                    val newHeadCoordinate = acc.headCoordinate.move(move)
                    println(" $acc, moving $move, resulting in $newHeadCoordinate")
                    if (!isSticky(newHeadCoordinate, acc.tailCoordinate)) {
                        val newTailPosition =
                            calculateNewTailPosition(newHeadCoordinate, acc.tailCoordinate, acc.headCoordinate, move)
                        visitedPositions.add(newTailPosition)
                        CurrentStatus(newHeadCoordinate, newTailPosition)
                    } else
                        CurrentStatus(newHeadCoordinate, acc.tailCoordinate)
                }
//                .run { printCurrentStatus(this) }
//            printVisitedMatrix(visitedPositions)
            visitedPositions.size
        }

    fun part2(filePath: String) =
        inputReader.withFileLines(filePath) { l ->
            val startingPoint = Coordinate(0, 0)
            val visitedPositions = mutableSetOf(startingPoint)
            val tails = (0..9).map { startingPoint.copy() }
            l.map { it.split(" ").run { Move(Direction.valueOf(this.first().toString()), this.last().toInt()) } }
                .flatMap { spreadMoves(it) }
                .fold(CurrentStatus2(tails)) { acc, move ->
                    println("=========")
                    println("current status is $acc")
                    println("moving $move")
                    val tails = listOf(acc.tailsCoordinates.first().move(move)) + acc.tailsCoordinates.subList(
                        1,
                        acc.tailsCoordinates.size
                    )
                    val newTails = tails.toMutableList()
                    tails.mapIndexed { index, tail ->
                        newTails[index] = if (index == 0) tail
                        else {
                            val newHead = newTails[index - 1]
                            if (!isSticky(newHead, tail)) {
                                println("$tail is not sticky to new head $newHead")
                                calculateNewTailPosition(
                                    newHead,
                                    tail,
                                    acc.tailsCoordinates[index-1],
                                    move
                                )
                            } else tail
                        }
                    }
                    println("$newTails")
                    visitedPositions.add(newTails.last())
                    CurrentStatus2(newTails).also {
                        printCurrentStatus2(it)
                    }
                }
//                .run { printCurrentStatus(this) }
//            printVisitedMatrix(visitedPositions)
            visitedPositions.size
        }

    private fun calculateNewTailPosition(
        newHeadCoordinate: Coordinate,
        tailCoordinate: Coordinate,
        headCoordinate: Coordinate,
        move: Move
    ): Coordinate {
        return if (tailCoordinate.x == newHeadCoordinate.x || tailCoordinate.y == newHeadCoordinate.y) {
            headCoordinate
        } else {
            val xs =
                (newHeadCoordinate.x - 1..newHeadCoordinate.x + 1).intersect(tailCoordinate.x - 1..tailCoordinate.x + 1)
            val ys =
                (newHeadCoordinate.y - 1..newHeadCoordinate.y + 1).intersect(tailCoordinate.y - 1..tailCoordinate.y + 1)
            try {
                when (move.direction) {
                    Direction.U, Direction.D -> try {
                        Coordinate(xs.first { it == newHeadCoordinate.x }, ys.first())
                    } catch (e: Exception) {
                        Coordinate(xs.first(), ys.first { it == newHeadCoordinate.y })
                    }
                    Direction.R, Direction.L -> try {
                        Coordinate(xs.first(), ys.first { it == newHeadCoordinate.y })
                    } catch (e: Exception) {
                        Coordinate(xs.first { it == newHeadCoordinate.x }, ys.first())
                    }
                }
            } catch (e: Exception){
                Coordinate(xs.first(), ys.first())
            }
        }
    }


    fun printCurrentStatus(currentStatus: CurrentStatus) {
        val matrix = (0..6).map { ((0..6).map { "." }.toMutableList()) }
        println("currentStatus $currentStatus")
        matrix[currentStatus.headCoordinate.y][currentStatus.headCoordinate.x] = "H"
        matrix[currentStatus.tailCoordinate.y][currentStatus.tailCoordinate.x] = "T"
        matrix.reversed().map {
            it.map {
                print(it)
            }
            println()
        }
    }

    fun printCurrentStatus2(currentStatus: CurrentStatus2) {
        val matrix = (0..10).map { ((0..10).map { "." }.toMutableList()) }
        println("currentStatus $currentStatus")
        currentStatus.tailsCoordinates.forEachIndexed { index, it ->
            matrix[it.y][it.x] = index.toString()
        }
        matrix.reversed().map {
            it.map {
                print(it)
            }
            println()
        }
    }

    fun printVisitedMatrix(coordinates: Set<Coordinate>) {
        val matrix = (0..6).map { ((0..6).map { "." }.toMutableList()) }
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

    fun isSticky(headCoordinate: Coordinate, tailCoordinate: Coordinate) =
        (headCoordinate.x - 1..headCoordinate.x + 1).contains(tailCoordinate.x) &&
                (headCoordinate.y - 1..headCoordinate.y + 1).contains(tailCoordinate.y)

    fun spreadMoves(move: Move): List<Move> =
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

    data class CurrentStatus(
        val headCoordinate: Coordinate,
        val tailCoordinate: Coordinate
    )

    data class CurrentStatus2(
        val tailsCoordinates: List<Coordinate>
    )

}

