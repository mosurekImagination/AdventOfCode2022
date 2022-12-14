import java.io.File

class Task14 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
        private val source = Coordinate(500, 0)

        val part1Logic: (Board) -> Int = {
            with(it) {
                var sandInAbyss = false
                while (!sandInAbyss) {
                    processRound(this)
                    sandInAbyss = sands.last().coordinate.y >= this.maxY
                }
                sands.size - 1
            }
        }
        val part2Logic: (Board) -> Int = {
            with(it) {
                var sourceBlocked = false
                while (!sourceBlocked) {
                    processRound(it)
                    sourceBlocked = if (sands.size > 3) sands.last() == sands[sands.size - 2] else false
                }
                this.print()
                sands.size - 1
            }
        }
        val part2BoardModification: (Board) -> Board = {
            with(it) {
                val floorLevel = this.maxY + 2
                val floorEnd = this.maxX + 1000
                val floor = Path(listOf(Line(Coordinate(0, floorLevel), Coordinate(floorEnd, floorLevel))))
                Board(this.paths + floor, mutableListOf())
            }
        }

        private fun processRound(board: Board) {
            val sand = board.sands.lastOrNull()
            if (sand == null) board.sands.add(Sand(source))
            else {
                val matrix = board.matrix
                with(sand.coordinate) {
                    matrix setAirAt this
                    if (matrix[x][y + 1] == MatrixItem.AIR) {
                        sand.coordinate = Coordinate(x, y + 1)
                    } else if (matrix[x - 1][y + 1] == MatrixItem.AIR) {
                        sand.coordinate = Coordinate(x - 1, y + 1)
                    } else if (matrix[x + 1][y + 1] == MatrixItem.AIR) {
                        sand.coordinate = Coordinate(x + 1, y + 1)
                    } else {
                        board.sands.add(Sand(source))
                    }
                    matrix setSandAt sand.coordinate

                }
            }
        }

        infix fun List<MutableList<MatrixItem>>.setSandAt(coordinate: Coordinate) {
            this[coordinate.x][coordinate.y] = MatrixItem.SAND
        }

        infix fun List<MutableList<MatrixItem>>.setAirAt(coordinate: Coordinate) {
            this[coordinate.x][coordinate.y] = MatrixItem.AIR
        }
    }

    fun run(
        filePath: String,
        partFunction: (Board) -> Int = part1Logic,
        boardModification: (Board) -> Board = { it }
    ): Int {
        return File(filePath).readLines()
            .map { line ->
                line.split(" -> ")
                    .map { it.split(",") }
                    .map { Coordinate(it.first().toInt(), it.last().toInt()) }
                    .windowed(2)
                    .map { it.sortedWith { a, b -> (a.x + a.y) - (b.x + b.y) } }
                    .map { Line(it.first(), it.last()) }
            }
            .map { Path(it) }
            .run { Board(this, mutableListOf()) }
            .run(boardModification)
            .also { it.print() }
            .run(partFunction)
    }

    data class Sand(var coordinate: Coordinate)
    data class Coordinate(val x: Int, val y: Int)

    data class Board(val paths: List<Path>, val sands: MutableList<Sand>) {
        val maxY = paths.map { it.getMaxY() }.max()
        val maxX = paths.map { it.getMaxX() }.max()
        val minX = paths.map { it.getMinX() }.min()

        fun print() {
            val xList = (minX..maxX).map { it.toString().padStart(3, '0').split("").filter { it.isNotBlank() } }
            (0 until 3).forEach { number ->
                print("  ")
                xList.forEach { print(it[number]) }
                println()
            }
            (0..maxY).forEach { y ->
                print("$y ")
                (minX..maxX).forEach { x ->
                    print(matrix[x][y].char)
                }
                println()
            }
            println("\n")
        }

        val matrix: List<MutableList<MatrixItem>> by lazy {
                val minX = paths.map { it.getMinX() }.min()
                val maxX = paths.map { it.getMaxX() }.max()
                val maxY = paths.map { it.getMaxY() }.max()
                val matrix = (0..maxX + 1).map { (0..maxY).map { MatrixItem.AIR }.toMutableList() }
                (0..maxY).map { y ->
                    (minX..maxX).map { x ->
                        val pointInLines =
                            paths.any {
                                it.lines.any {
                                    x in (it.startCoordinate.x..it.endCoordinate.x) && y == it.endCoordinate.y ||
                                            y in (it.startCoordinate.y..it.endCoordinate.y) && x == it.endCoordinate.x
                                }
                            }
                        val sandPoint = sands.any { it.coordinate.x == x && it.coordinate.y == y }
                        matrix[x][y] =
                            if (pointInLines) MatrixItem.LINE else if (sandPoint) MatrixItem.SAND else MatrixItem.AIR
                    }
                }
                matrix[source.x][source.y] = MatrixItem.SOURCE
            matrix
            }
    }

    enum class MatrixItem(val char: Char) {
        LINE('#'),
        AIR('.'),
        SAND('o'),
        SOURCE('+')
    }

    data class Path(val lines: List<Line>) {
        fun getMinX() = lines.map { listOf(it.startCoordinate.x, it.endCoordinate.x).min() }.min()
        fun getMaxX() = lines.map { listOf(it.startCoordinate.x, it.endCoordinate.x).max() }.max()
        fun getMaxY() = lines.map { listOf(it.startCoordinate.y, it.endCoordinate.y).max() }.max()
    }

    data class Line(val startCoordinate: Coordinate, val endCoordinate: Coordinate)
}
