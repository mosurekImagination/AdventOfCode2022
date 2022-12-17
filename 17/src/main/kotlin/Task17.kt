import java.io.File
import java.lang.RuntimeException

class Task17 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    val rowSize = 7


    fun run(
        filePath: String,
        maxFallenRocks: Int = 2022
    ): Int {
        val moves = File(filePath)
            .readLines()
            .first()
            .split("")
            .filter { it.isNotBlank() }
            .map { it.first() }
            .map { move -> Move.values().first { it.char == move } }
            .run { this.asSequence() }
            .run {
                generateSequence(this) { this }.flatten()
            }.iterator()

        val possibleFigures = Figure.values().toList()
        val figures = generateSequence(possibleFigures) { possibleFigures }.flatten().iterator()


        var currentRock = 0
        var currentMove = moves.next()
        var currentMaxY = -1
        val matrix = mutableListOf<MutableList<String>>()
        val appearingX = 2

        while (currentRock < maxFallenRocks) {
            var currentFigure = figures.next()
            currentRock++
            //extend list to contain new figure
            val startingPoint = Coordinate(currentMaxY + 3 + 1, appearingX)
            val requiredMatrixHeight =
                startingPoint.y + currentFigure.height // -1 as starting point is already giving one height
            if (requiredMatrixHeight > matrix.size - 1) { // TO BE CHECKED indexes and if -1 is required
                matrix.addRows(requiredMatrixHeight - matrix.size + 1)
            }
            println("Adding new figure $currentFigure")
            currentFigure.setFigureCoordinatesStartingFrom(startingPoint)
            printMatrix(currentFigure, matrix)
            if (currentFigure.canMove(currentMove, matrix)) {
                currentFigure.move(currentMove)
                println("Figure $currentFigure moving with $currentMove")
            } else {
                println("Figure $currentFigure can't move with $currentMove")
            }
            printMatrix(currentFigure, matrix)
            currentMove = moves.next()

            while (currentFigure.canFall(matrix)) {
                currentFigure.fall()
                printMatrix(currentFigure, matrix)
                if (currentFigure.canMove(currentMove, matrix)) {
                    currentFigure.move(currentMove)
                    println("Figure $currentFigure moving with $currentMove")
                } else {
                    println("Figure $currentFigure can't move with $currentMove")
                }
                printMatrix(currentFigure, matrix)
                currentMove = moves.next()
            }
            //END ROCK FALLING
            currentFigure.markFinalPositionInMatrix(matrix)
            printMatrix(currentFigure, matrix)

            currentMaxY = matrix.size - matrix.reversed().indexOfFirst { it.contains("#") } - 1
            // update max y
        }
        return currentMaxY + 1
    }

    private fun printMatrix(currentFigure: Figure, matrix: MutableList<MutableList<String>>, printingEnabled: Boolean = false) {
        if(printingEnabled) {
            println("==")
            matrix.reversed().forEachIndexed { yi, line ->
                line.forEachIndexed { xi, value ->
                    val printValue =
                        if (currentFigure.coordinates.contains(Coordinate(matrix.size - yi - 1, xi))) "@" else value
                    print(printValue)
                }
                println()
            }
            println("==")
        }
    }

    fun MutableList<MutableList<String>>.addRows(numberOfRows: Int) {
        (0 until numberOfRows).forEach { this.add((0 until rowSize).map { "." }.toMutableList()) }
    }

    enum class Move(val char: Char) {
        LEFT('<'),
        RIGHT('>')
    }

    data class Coordinate(val y: Int, val x: Int)

    enum class Figure(val height: Int, var coordinates: MutableList<Coordinate> = mutableListOf()) {
        LINE(1),
        CROSS(3),
        L_SIGN(3),
        VERTICAL_LINE(4),
        BOX(2);

        fun setFigureCoordinatesStartingFrom(coord: Coordinate) {
            coordinates =
                when (this) {
                    LINE -> (coord.x..coord.x + 3).map { Coordinate(coord.y, it) }
                    VERTICAL_LINE -> (coord.y..coord.y + 3).map { Coordinate(it, coord.x) }
                    BOX -> (coord.x..coord.x + 1).flatMap { x -> (coord.y..coord.y + 1).map { Coordinate(it, x) } }
                    L_SIGN -> (coord.x..coord.x + 1).map { x ->
                        Coordinate(
                            coord.y,
                            x
                        )
                    } + (coord.y..coord.y + 2).map { y -> Coordinate(y, coord.x + 2) }
                    CROSS -> (coord.x..coord.x + 2).map { x -> Coordinate(coord.y+1, x) } + listOf(
                        coord.y ,
                        coord.y + 2
                    ).map { y -> Coordinate(y, coord.x+1) }
                }.toMutableList()
        }

        fun canFall(matrix: MutableList<MutableList<String>>): Boolean {
            return if (this.coordinates.any { it.y - 1 < 0 }) false else
                this.coordinates.all { matrix[it.y - 1][it.x] == "." }
        }

        fun fall() {
            this.coordinates = coordinates.map { Coordinate(it.y - 1, it.x) }.toMutableList()
        }

        fun move(currentMove: Move) {
            this.coordinates = when (currentMove) {
                Move.LEFT -> coordinates.map { Coordinate(it.y, it.x - 1) }.toMutableList()
                Move.RIGHT -> coordinates.map { Coordinate(it.y, it.x + 1) }.toMutableList()
            }
        }

        fun markFinalPositionInMatrix(matrix: MutableList<MutableList<String>>) {
            this.coordinates.forEach {
                matrix[it.y][it.x] = "#"
            }
        }

        fun canMove(currentMove: Move, matrix: MutableList<MutableList<String>>): Boolean {
            return try {
                when (currentMove) {
                    Move.LEFT -> this.coordinates.all { matrix[it.y][it.x - 1] == "." }
                    Move.RIGHT -> this.coordinates.all { matrix[it.y][it.x + 1] == "." }
                }
            } catch (e: RuntimeException) {
                false
            }
        }
    }
}
