class Task18 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(
        input: String,
        part2Enabled: Boolean = false
    ): Int = input
        .lines()
        .map { it.split(",") }
        .map { Coordinate(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
        .run {
            val maxValue = this.maxOf { listOf(it.x, it.y, it.z).max() } + 1
            val coordinates = (0..maxValue).map { true }
            val matrix = (0..maxValue).map { ((0..maxValue).map { coordinates.toMutableList() }) }
            this.forEach { cube ->
                matrix[cube.x][cube.y][cube.z] = false
            }
            if (part2Enabled) {
                matrix.forEachIndexed { xi, x ->
                    x.forEachIndexed { yi, y ->
                        y.forEachIndexed { zi, b ->
                            if (b) matrix[xi][yi][zi] = !isAirPocket(Coordinate(xi, yi, zi), matrix)
                        }
                    }
                }
            }
            this.fold(0) { acc, cube ->
                val sumOfAreas = listOf(
                    matrix.isFree(cube.x - 1, cube.y, cube.z),
                    matrix.isFree(cube.x + 1, cube.y, cube.z),
                    matrix.isFree(cube.x, cube.y - 1, cube.z),
                    matrix.isFree(cube.x, cube.y + 1, cube.z),
                    matrix.isFree(cube.x, cube.y, cube.z - 1),
                    matrix.isFree(cube.x, cube.y, cube.z + 1),
                ).sumOf { it.toInt() }
                acc + sumOfAreas
            }
        }

    private fun isAirPocket(coord: Coordinate, matrix: List<List<MutableList<Boolean>>>): Boolean {
        val bubble = mutableListOf<Coordinate>()
        return isAirPocket(coord, matrix, bubble)
    }

    private fun isAirPocket(
        coord: Coordinate,
        matrix: List<List<MutableList<Boolean>>>,
        bubble: MutableList<Coordinate>
    ): Boolean {
        return if (coord.x <= 0 || coord.y <= 0 || coord.z <= 0) false
        else if (coord.x >= matrix.size || coord.y >= matrix.size || coord.z >= matrix.size) false
        else if (bubble.contains(coord)) true
        else {
            bubble.add(coord)
            listOf(
                coord.copy(x = coord.x - 1),
                coord.copy(x = coord.x + 1),
                coord.copy(y = coord.y - 1),
                coord.copy(y = coord.y + 1),
                coord.copy(z = coord.z - 1),
                coord.copy(z = coord.z + 1)
            ).all {
                bubble.contains(it) ||
                        !matrix.isFree(it.x, it.y, it.z) || isAirPocket(it, matrix, bubble)
            }
        }
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    data class Coordinate(val x: Int, val y: Int, val z: Int)

    private fun List<List<MutableList<Boolean>>>.isFree(x: Int, y: Int, z: Int): Boolean =
        if (x < 0 || y < 0 || z < 0) true
        else if (x in this.indices && y in this.indices && z in this.indices) this[x][y][z] else true
}


