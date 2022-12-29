class Task23 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    private val directionList = listOf(Direction.N, Direction.S, Direction.W, Direction.E)
    private val directions = generateSequence(directionList) { directionList }.flatten().iterator()

    fun run(
        input: String,
        part2Enabled: Boolean = false
    ) = extractMatrix(input).run {
        if(part2Enabled){
            var round = 1
            ensureMatrixSize(this)
            while (getElfsReadyToMove(this).isNotEmpty()) {
                playRound(this)
                ensureMatrixSize(this)
                round++
            }
            return round
        } else {
            this.run {
                (0 until 10).map { playRound(this) }
                this
            }.run {
                removeEmptySpaces(this)
                println("Target matrix is:")
                printMatrix(this)
                this.fold(0) { acc, matrixItems -> acc + matrixItems.count { it == MatrixItem.AIR } }
            }
        }
    }


    fun extractMatrix(input: String) =
        input.lines().map {
            it.toCharArray().toList().map { matrixItem -> MatrixItem.values().first { it.char == matrixItem } }
                .toMutableList()
        }.toMutableList()

    fun removeEmptySpaces(matrix: MutableList<MutableList<MatrixItem>>): MutableList<MutableList<MatrixItem>> {
        val emptyFirstRows = matrix.takeWhile { !it.contains(MatrixItem.ELF) }.size
        (0 until emptyFirstRows).forEach { matrix.removeAt(0) }

        val emptyLastRows = matrix.takeLastWhile { !it.contains(MatrixItem.ELF) }.size
        (0 until emptyLastRows).forEach { matrix.removeAt(matrix.size - 1) }

        val emptyFirstColumns = matrix.first().indices.takeWhile { x ->
            !matrix.indices.map { matrix[it][x] }.contains(MatrixItem.ELF)
        }.size
        (0 until emptyFirstColumns).forEach { matrix.forEach { it.removeAt(0) } }

        val emptyLastColumns = matrix.first().indices.toList().takeLastWhile { x ->
            !matrix.indices.map { matrix[it][x] }.contains(MatrixItem.ELF)
        }.size
        val lastColumnIndex = matrix.first().size - 1
        (0 until emptyLastColumns).forEach { matrix.forEach { it.removeAt(lastColumnIndex)} }
        return matrix
    }

    private fun playRound(matrix: MutableList<MutableList<MatrixItem>>) {
        //if some elf touches wall, enlarge matrix
        var remainingDirections = 4 + 1
        ensureMatrixSize(matrix)
        var propositions = mutableListOf<Proposition>()
        //get elfs capable of moving
        val elfsReadyToMove = getElfsReadyToMove(matrix)
        //until all elfs capable of moving proposed moves

        while (remainingDirections > 0) {
            //try to propose next move
            val nextMove = directions.next()
            remainingDirections--
            elfsReadyToMove.removeIf { elf ->
                //if elf is capable to move into that direction. Create a proposition and don't process it further
                isCapableToMoveInto(elf, nextMove, matrix).also {
                    if (it)
                        propositions.add(getProposition(elf, nextMove))
                }
            }
        }
        //delete clashing proposition
        propositions = getPropositionsWithoutDuplicates(propositions)
        //move elfs
        moveElfs(matrix, propositions)
        //printMatrix(matrix)
    }

    private fun printMatrix(matrix: MutableList<MutableList<MatrixItem>>) {
        matrix.forEach {
            it.forEach { print(it.char) }
            println()
        }
        println("---")
    }

    fun moveElfs(
        matrix: MutableList<MutableList<MatrixItem>>,
        propositions: MutableList<Proposition>
    ) {
        propositions.forEach {
            matrix[it.from.y][it.from.x] = MatrixItem.AIR
            matrix[it.to.y][it.to.x] = MatrixItem.ELF
        }
    }

    fun getPropositionsWithoutDuplicates(propositions: List<Proposition>): MutableList<Proposition> {
        val values = mutableListOf<Proposition>()
        val deletedValues = mutableListOf<Coordinate>()
        propositions.forEach { prep-> if(values.any{it.to==prep.to}) {
            deletedValues.add(prep.to)
            values.removeIf{it.to == prep.to}
        } else if(!deletedValues.contains(prep.to))
            values.add(prep)}
        return values.toMutableList()
    }

    fun ensureMatrixSize(matrix: MutableList<MutableList<MatrixItem>>): MutableList<MutableList<MatrixItem>> {
        val emptyRow = matrix[0].indices.map { MatrixItem.AIR }
        if (matrix.first().contains(MatrixItem.ELF))
            matrix.add(0, emptyRow.toMutableList())
        if (matrix.last().contains(MatrixItem.ELF))
            matrix.add(matrix.size, emptyRow.toMutableList())
        if (matrix.indices.map { matrix[it].first() }.contains(MatrixItem.ELF))
            matrix.forEach { it.add(0, MatrixItem.AIR) }
        if (matrix.indices.map { matrix[it].last() }.contains(MatrixItem.ELF))
            matrix.forEach { it.add(it.size, MatrixItem.AIR) }
        return matrix
    }

    private fun getProposition(elf: Coordinate, nextMove: Direction): Proposition {
        return when (nextMove) {
            Direction.N -> Proposition(elf, elf.copy(y = elf.y - 1))
            Direction.S -> Proposition(elf, elf.copy(y = elf.y + 1))
            Direction.W -> Proposition(elf, elf.copy(x = elf.x - 1))
            Direction.E -> Proposition(elf, elf.copy(x = elf.x + 1))
        }
    }

    fun isCapableToMoveInto(
        elf: Coordinate,
        nextMove: Direction,
        matrix: MutableList<MutableList<MatrixItem>>
    ): Boolean {
        return when (nextMove) {
            Direction.N -> (elf.x - 1..elf.x + 1).map { matrix[elf.y - 1][it] }.none { it == MatrixItem.ELF }
            Direction.S -> (elf.x - 1..elf.x + 1).map { matrix[elf.y + 1][it] }.none { it == MatrixItem.ELF }
            Direction.W -> (elf.y - 1..elf.y + 1).map { matrix[it][elf.x - 1] }.none { it == MatrixItem.ELF }
            Direction.E -> (elf.y - 1..elf.y + 1).map { matrix[it][elf.x + 1] }.none { it == MatrixItem.ELF }
        }
    }

    fun getElfsReadyToMove(matrix: MutableList<MutableList<MatrixItem>>): MutableList<Coordinate> {
        val elfsList = mutableListOf<Coordinate>()
        matrix.forEachIndexed { yi, y ->
            y.forEachIndexed { xi, item ->
                if (item == MatrixItem.ELF && (
                            matrix[yi - 1][xi - 1] == MatrixItem.ELF ||
                                    matrix[yi][xi - 1] == MatrixItem.ELF ||
                                    matrix[yi + 1][xi - 1] == MatrixItem.ELF ||
                                    matrix[yi - 1][xi] == MatrixItem.ELF ||
                                    matrix[yi + 1][xi] == MatrixItem.ELF ||
                                    matrix[yi - 1][xi + 1] == MatrixItem.ELF ||
                                    matrix[yi][xi + 1] == MatrixItem.ELF ||
                                    matrix[yi + 1][xi + 1] == MatrixItem.ELF
                            )
                )
                    elfsList.add(Coordinate(yi, xi))
            }
        }
        return elfsList
    }

    enum class MatrixItem(val char: Char) {
        ELF('#'),
        AIR('.')
    }

    enum class Direction {
        N,
        S,
        W,
        E
    }

    data class Coordinate(val y: Int, val x: Int)
    data class Proposition(val from: Coordinate, val to: Coordinate)
}


