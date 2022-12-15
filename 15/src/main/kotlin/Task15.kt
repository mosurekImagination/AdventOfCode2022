import java.io.File
import kotlin.math.abs

class Task15 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun part2(
        filePath: String,
        endLineNumber: Int = 10
    ) =
        File(filePath).readLines()
            .map {
                it.split("=").map { Regex("(-?)[0-9]").findAll(it).map(MatchResult::value).toList() }
                    .filter { it.isNotEmpty() }
                    .map { it.reduce { acc, it -> acc + it } }
                    .map { it.toInt() }
            }
            .map {
                it.chunked(2).run {
                    Pair(
                        Coordinate(this.first().first(), this.first().last()),
                        Coordinate(this.last().first(), this.last().last())
                    )
                }
            }
            .run {
                val sensorWithDistances = this.map { Pair(it.first, calculateDistance(it.first, it.second)) }
                val linesCount = endLineNumber
                val linesRanges = (0..linesCount).map { emptyList<IntRange>().toMutableList() }
                sensorWithDistances.map {
                    val sensor = it.first
                    val distance = it.second
                    ((sensor.y - distance).toPositive()..listOf((sensor.y + distance), linesCount).min()).forEach { y ->
                        val distanceFromCenter = abs(y - sensor.y)
                        val rangeInLine = distance - distanceFromCenter
                        linesRanges[y].add(
                            ((sensor.x - rangeInLine).toPositive()..listOf(
                                (sensor.x + rangeInLine),
                                linesCount
                            ).min())
                        )
                    }
                }
                println("Created ranges for each line, merging them")
                var y = -1
                var currentXEnd = 0
                var foundEmpty = false
                linesRanges.find {
                    currentXEnd = 0
                    println(++y)
                    while (currentXEnd != linesCount && !foundEmpty) {
                        val foundRange = it.find { it.first <= currentXEnd && it.last > currentXEnd }
                        if (foundRange != null) {
                            currentXEnd = foundRange.last
                        } else {
                            foundEmpty = true
                        }
                    }
                    currentXEnd++
                    foundEmpty
                }!!
                currentXEnd * 4000000L + y
            }

    fun run(
        filePath: String,
        lineNumber: Int = 10
    ) =
        File(filePath).readLines()
            .map {
                it.split("=").map { Regex("(-?)[0-9]").findAll(it).map(MatchResult::value).toList() }
                    .filter { it.isNotEmpty() }
                    .map { it.reduce { acc, it -> acc + it } }
                    .map { it.toInt() }
            }
            .map {
                it.chunked(2).run {
                    Pair(
                        Coordinate(this.first().first(), this.first().last()),
                        Coordinate(this.last().first(), this.last().last())
                    )
                }
            }
            .run {
                val sensorWithDistances = this.map { Pair(it.first, calculateDistance(it.first, it.second)) }
                val maxDistance = sensorWithDistances.maxOf { it.second }
                val matrixInfo = getMatrixInfo(this.map { listOf(it.first, it.second) })
                val startX = matrixInfo.minX - maxDistance
                val endX = matrixInfo.maxX + maxDistance
                val result = (startX..endX).filter { x ->
                    sensorWithDistances.any {
                        calculateDistance(
                            Coordinate(x, lineNumber),
                            it.first
                        ) <= it.second
                    }
                }
                result.count { x ->
                    this.none { x == it.first.x && lineNumber == it.first.y || x == it.second.x && lineNumber == it.second.y }
                }
            }

    fun calculateDistance(coord1: Coordinate, coord2: Coordinate) = abs(coord1.x - coord2.x) + abs(coord1.y - coord2.y)

    fun Int.toPositive() = if (this < 0) 0 else this

    data class Coordinate(val x: Int, val y: Int, var item: MatrixItem = MatrixItem.SENSOR)

    fun getMatrixInfo(sensorsWithBeacons: List<List<Coordinate>>): MatrixInfo {
        val items = sensorsWithBeacons.flatMap { listOf(it.first(), it.last()) }
        val minY = items.minOf { it.y }
        val maxY = items.maxOf { it.y }
        val minX = items.minOf { it.x }
        val maxX = items.maxOf { it.x }
        val xOffset = if (minX < 0) abs(minX) else 0
        val yOffset = if (minY < 0) abs(minY) else 0
        return MatrixInfo(items, minX, maxX, minY, maxY, xOffset, yOffset)
    }

    data class MatrixInfo(
        val items: List<Coordinate>,
        val minX: Int,
        val maxX: Int,
        val minY: Int,
        val maxY: Int,
        val xOffset: Int,
        val yOffset: Int
    )

    fun printMatrix(matrix: List<List<Coordinate>>) {
        with(getMatrixInfo(matrix)) {
            val xList = (minX..maxX)
                .map { it.toString().padStart(2, ' ').split("") }

            (0 until 3).forEach { number ->
                print("  ")
                xList.forEach { print(it[number]) }
                println()
            }
            (minY..maxY).forEach { y ->
                print("${y.toString().padStart(3, ' ')} ")
                (minX..maxX).forEach { x ->
                    print(matrix[x + xOffset][y + yOffset].item.char)
                }
                println()
            }
            println("\n")
        }
    }

    enum class MatrixItem(val char: Char) {
        BEACON('B'),
        SENSOR('S'),
        AIR('.'),
        FORBIDDEN_AREA('#')
    }

    fun print(
        filePath: String,
        lineNumber: Int = 10
    ) =
        File(filePath).readLines()
            .map {
                it.split("=").map { Regex("(-?)[0-9]").findAll(it).map(MatchResult::value).toList() }
                    .filter { it.isNotEmpty() }
                    .map { it.reduce { acc, it -> acc + it } }
                    .map { it.toInt() }
            }
            .map {
                it.chunked(2).run {
                    Pair(
                        Coordinate(this.first().first(), this.first().last(), MatrixItem.SENSOR),
                        Coordinate(this.last().first(), this.last().last(), MatrixItem.BEACON)
                    )
                }
            }
            .run {
                val maxDistance: Int = this.maxOf { calculateDistance(it.first, it.second) }
                Pair(with(getMatrixInfo(this.map { listOf(it.first, it.second) })) {
                    (minX - maxDistance..maxX + maxDistance).toList().parallelStream().map { x ->
                        println("processing X: $x")
                        (minY - maxDistance..maxY + maxDistance).toList().parallelStream().map { y ->
                            Coordinate(x, y, MatrixItem.AIR)
                        }.toArray().toList()
                    }.toArray().toList()
                } as List<List<Coordinate>>, this)
            }
            .run {
                val matrixInfo = getMatrixInfo(this.first)
                val matrixItems = this.first.flatMap { it.map { it } }
                this.second.forEach {
                    println("Processing $it")
                    markNotAvailableArea(it.first, it.second, matrixInfo, matrixItems)
                }
                printMatrix(this.first)
                matrixItems.count { it.y == lineNumber && it.item == MatrixItem.FORBIDDEN_AREA }
            }

    private fun markNotAvailableArea(
        sensor: Coordinate,
        beacon: Coordinate,
        matrixInfo: MatrixInfo,
        matrixItems: List<Coordinate>
    ) {
        val distance = calculateDistance(sensor, beacon)
        with(matrixInfo) {
            (sensor.x - distance..sensor.x + distance).filter { it >= minX }.filter { it <= maxX }.forEach { x ->
                (sensor.y - distance..sensor.y + distance).filter { it >= minY }.filter { it <= maxY }
                    .forEach { y ->
                        val point = matrixItems.first { it.x == x && it.y == y }
                        if (calculateDistance(sensor, point) <= distance) {
                            if (point.item == MatrixItem.AIR) point.item = MatrixItem.FORBIDDEN_AREA
                        }
                    }
            }
        }
    }
}
