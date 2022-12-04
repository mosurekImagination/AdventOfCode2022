class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(filePath: String) =
        inputReader.withFileLines(filePath) { backpacks ->
            backpacks.flatMap { backpack ->
                val splitPoint = backpack.length / 2
                val first = backpack.subSequence(0, splitPoint).toSet()
                val second = backpack.subSequence(splitPoint, backpack.length).toSet()
                first.intersect(second)
            }.sumOf { calculatePriority(it) }
        }

    fun groups(filePath: String) =
        inputReader.withFileLines(filePath) { backpacks ->
            backpacks
                .chunked(3)
                .map { it.map { it.toCharArray().toSet() } }
                .flatMap { it.reduce { first, second -> first.intersect(second) } }
                .sumOf { calculatePriority(it) }
        }

    private fun calculatePriority(it: Char): Int =
        when (it.category) {
            CharCategory.LOWERCASE_LETTER -> it.code - 96;
            CharCategory.UPPERCASE_LETTER -> it.code - 65 + 27
            else -> 0
        }
}
