class Task22 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(
        input: String,
        returnStatus: Boolean = false,
        startStatus: Status? = null
    ): Any =
        input.split("(\r\n\r\n|\n\n)".toRegex())
            .run {
                val maxIndex = this.first().lines().maxOf { it.length }
                val map = this.first()
                    .lines()
                    .map { it.padEnd(maxIndex, MatrixItem.AIR.char) }
                    .map { it.toCharArray().map { char -> MatrixItem.values().first { it.char == char } } }

                val numbers = this.last()
                    .split("(R|L)".toRegex()).filter { it.isNotEmpty() }
                val rotations = this.last().split("\\d".toRegex()).filter { it.isNotEmpty() }
                val movements = numbers
                    .zip(if (numbers.size > rotations.size) rotations + listOf("Z") else rotations)
                    .map { Movement(it.first.toInt(), Rotate.valueOf(it.second)) }
                Pair(map, movements)
            }.run {
                val startingX = this.first.first().indexOfFirst { it == MatrixItem.TILE }
                val startingStatus = startStatus ?: Status(0, startingX, Direction.RIGHT)
                val ySize = this.first.size
                val xSize = this.first.first().size

                this.second.fold(startingStatus) { status, movement ->
                    processMovement(status, movement, this.first, ySize, xSize)
                }
            }.run {
                if (returnStatus) this
                else {
                    val endStatus = this
                    (endStatus.y + 1) * 1000 + (endStatus.x + 1) * 4 + endStatus.direction.intValue
                }
            }

    private fun processMovement(
        status: Status,
        movement: Movement,
        matrix: List<List<MatrixItem>>,
        ySize: Int,
        xSize: Int
    ): Status {
        return (1..movement.length).fold(status) { currentStatus, _ ->
            processStep(
                currentStatus,
                matrix,
                ySize,
                xSize
            )
        }.run { processRotation(this, movement) }
    }

    private fun processRotation(status: Status, movement: Movement): Status {
        var newIntValue = (status.direction.intValue + movement.rotate.intChange) % 4
        if (newIntValue < 0) newIntValue += 4
        return status.copy(direction = Direction.values().first { it.intValue == newIntValue })
    }

    private fun processStep(status: Status, matrix: List<List<MatrixItem>>, ySize: Int, xSize: Int): Status {
        var newStatus = when (status.direction) {
            Direction.LEFT -> Status(status.y, if (status.x - 1 < 0) xSize - 1 else status.x - 1, Direction.LEFT)
            Direction.RIGHT -> Status(status.y, if (status.x + 1 == xSize) 0 else status.x + 1, Direction.RIGHT)
            Direction.DOWN -> Status(if (status.y + 1 == ySize) 0 else status.y + 1, status.x, Direction.DOWN)
            Direction.TOP -> Status(if (status.y - 1 < 0) ySize - 1 else status.y - 1, status.x, Direction.TOP)
        }
        if (matrix[newStatus.y][newStatus.x] == MatrixItem.AIR) {
            newStatus = when (status.direction) {
                Direction.LEFT -> Status(
                    newStatus.y,
                    matrix[newStatus.y].indexOfLast { it != MatrixItem.AIR },
                    Direction.LEFT
                )

                Direction.RIGHT -> Status(
                    newStatus.y,
                    matrix[newStatus.y].indexOfFirst { it != MatrixItem.AIR },
                    Direction.RIGHT
                )

                Direction.DOWN -> Status(
                    matrix.indices.first { matrix[it][newStatus.x] != MatrixItem.AIR },
                    newStatus.x,
                    Direction.DOWN
                )

                Direction.TOP -> Status(
                    matrix.indices.last { matrix[it][newStatus.x] != MatrixItem.AIR },
                    newStatus.x,
                    Direction.TOP
                )
            }
        }
        return if (matrix[newStatus.y][newStatus.x] == MatrixItem.TILE) newStatus
        else status
    }

    data class Status(val y: Int, val x: Int, val direction: Direction)
    data class Movement(val length: Int, val rotate: Rotate)
    enum class MatrixItem(val char: Char) {
        WALL('#'),
        TILE('.'),
        AIR(' ')
    }

    enum class Direction(val intValue: Int) {
        RIGHT(0),
        DOWN(1),
        LEFT(2),
        TOP(3)
    }

    enum class Rotate(val intChange: Int) {
        L(-1),
        R(1),
        Z(0)
    }
}


