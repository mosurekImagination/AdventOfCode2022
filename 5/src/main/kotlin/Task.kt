class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
        private const val CARGO_INPUT_LENGTH = 4
    }

    fun run(filePath: String, upgradedCrane: Boolean = false) =
        inputReader.withFileLines(filePath) { lines ->
            val cargoInput = lines.takeWhile { isCargoInput(it) }
            val cargo = parseCargo(cargoInput)
            val moves = lines.takeLastWhile { isMovesInput(it) }
                .extractNumbers()
                .parseMoves()

            processMoves(moves, cargo, upgradedCrane)
            cargo.map { it.last() }.joinToString("")
        }

    private fun processMoves(
        moves: List<Move>,
        cargo: List<MutableList<String>>,
        upgradedCrane: Boolean
    ) {
        moves.forEach {
            val cargoFrom = cargo[it.from]
            val crates = cargoFrom.takeLast(it.amount).run {
                if (upgradedCrane) this else reversed()
            }
            cargo[it.to].addAll(crates)
            crates.forEach {
                cargoFrom.removeLast()
            }
        }
    }

    private fun parseCargo(cargoInput: List<String>): List<MutableList<String>> {
        val cargo = (0..cargoInput.first().length / CARGO_INPUT_LENGTH).map { mutableListOf<String>() }
        cargoInput.map {
            it.chunked(CARGO_INPUT_LENGTH)
                .mapIndexed { i, o ->
                    if (o.isNotBlank()) {
                        cargo[i].add(0, o.trim('[', ']', ' '))
                    }
                }
        }
        return cargo
    }

    private fun List<String>.extractNumbers() = this.map { Regex("\\d+").findAll(it).map { it.value } }
        .map { it.toList() }

    private fun  List<List<String>>.parseMoves() = this.map { Move(it[0].toInt(), it[1].toInt() - 1, it[2].toInt() - 1) }

    private fun isMovesInput(it: String) = it.contains("move")

    private fun isCargoInput(it: String) = !it.contains("1")

    data class Move(val amount: Int, val from: Int, val to: Int)
}
