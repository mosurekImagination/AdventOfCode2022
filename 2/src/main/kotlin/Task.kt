class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    enum class Move(private val opponent: String, private val you: String) {
        ROCK("A", "X"),
        PAPER("B", "Y"),
        SCISSORS("C", "Z");

        companion object {
            fun getMove(char: String) =
                values().first { it.you == char || it.opponent == char }
        }
    }

    enum class Result(val points: Int, val plannedResult: String) {
        WIN(6, "Z"),
        DRAW(3, "Y"),
        LOSS(0, "X");

        companion object {
            fun getPlannedResult(char: String) =
                values().first { it.plannedResult == char }
        }
    }

    fun run(filePath: String, wonPlanMode: Boolean = false) =
        inputReader.withFileLines(filePath) {
            val games = it.map { it.split(" ") }
            if (wonPlanMode) {
                games.map { Pair(Move.getMove(it[0]), Result.getPlannedResult(it[1])) }
                    .sumOf { it.second.points + getResponseValue(getPlannedMove(it)) }
            } else {
                games.map { Pair(Move.getMove(it[0]), Move.getMove(it[1])) }
                    .sumOf { getWonResult(it).points + getResponseValue(it.second) }
            }
        }

    private fun getWonResult(round: Round) = when (round.first) {
        Move.ROCK -> when (round.second) {
            Move.PAPER -> Result.WIN; Move.SCISSORS -> Result.LOSS; else -> Result.DRAW
        }
        Move.PAPER -> when (round.second) {
            Move.ROCK -> Result.LOSS; Move.SCISSORS -> Result.WIN; else -> Result.DRAW
        }
        Move.SCISSORS -> when (round.second) {
            Move.ROCK -> Result.WIN; Move.PAPER -> Result.LOSS; else -> Result.DRAW
        }
    }

    private fun getPlannedMove(plan: Pair<Move, Result>): Move = when (plan.first) {
        Move.ROCK -> when (plan.second) {
            Result.WIN -> Move.PAPER; Result.LOSS -> Move.SCISSORS; else -> Move.ROCK
        }
        Move.PAPER -> when (plan.second) {
            Result.WIN -> Move.SCISSORS; Result.LOSS -> Move.ROCK; else -> Move.PAPER
        }
        Move.SCISSORS -> when (plan.second) {
            Result.WIN -> Move.ROCK; Result.LOSS -> Move.PAPER; else -> Move.SCISSORS
        }
    }

    private fun getResponseValue(move: Move): Int = when (move) {
        Move.ROCK -> 1
        Move.PAPER -> 2
        Move.SCISSORS -> 3
    }

}

typealias Round = Pair<Task.Move, Task.Move>