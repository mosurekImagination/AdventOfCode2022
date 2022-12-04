class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path

        val includePredicate: (Assignment) -> Boolean = {
            (it.firstElfStart <= it.secondElfStart && it.firstElfEnd >= it.secondElfEnd) ||
                    (it.secondElfStart <= it.firstElfStart && it.secondElfEnd >= it.firstElfEnd)
        }

        val overlapPredicate: (Assignment) -> Boolean = {
            !(it.firstElfEnd < it.secondElfStart || it.firstElfStart > it.secondElfEnd)
        }

    }

    fun run(filePath: String, predicate: (Assignment) -> Boolean = includePredicate) =
        inputReader.withFileLines(filePath) { lines ->
            lines
                .map { Assignment(it.split(",")) }
                .count(predicate)
        }

    data class Assignment(
        val firstElfStart: Int,
        val firstElfEnd: Int,
        val secondElfStart: Int,
        val secondElfEnd: Int
    ) {
        constructor(list: List<String>) : this(
            list.first().split("-").first().toInt(),
            list.first().split("-").last().toInt(),
            list.last().split("-").first().toInt(),
            list.last().split("-").last().toInt(),
        ) {
            require(list.size == 2)
        }
    }
}
