class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
        const val PART_1_MARKER_LENGTH = 4
        const val PART_2_MARKER_LENGTH = 14
    }

    fun run(filePath: String, markerLength: Int = PART_1_MARKER_LENGTH) =
        inputReader.withFileLines(filePath) { lines ->
            lines.first().windowed(markerLength).indexOfFirst { it.toSet().size == markerLength } + markerLength
        }
}

