class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(filePath: String, listSize: Int = 1) = inputReader.withFileLines(filePath) { fileLines ->
        val initResultsList = (1..listSize).map { 0 }
        fileLines.fold(CurrentResult(initResultsList, 0)) { currentResult, value ->
            if (isEndOfElf(value))
                processElfCarriage(currentResult)
            else increaseElfCarriage(currentResult, value)
        }.run { processElfCarriage(this).maxResultsList.sum() }
    }

    private fun increaseElfCarriage(currentResult: CurrentResult, value: String) =
        currentResult.copy(current = currentResult.current + value.toInt())

    private fun isEndOfElf(it: String) = it.isEmpty()

    private fun processElfCarriage(currentResult: CurrentResult): CurrentResult {
        val min = currentResult.maxResultsList.min()
        val minIndex = currentResult.maxResultsList.indexOf(currentResult.maxResultsList.min())
        return if (currentResult.current > min) CurrentResult(
            currentResult.maxResultsList.toMutableList().apply { this[minIndex] = currentResult.current }, 0
        ) else currentResult.copy(current = 0)
    }
}

data class CurrentResult(val maxResultsList: List<Int>, val current: Int)
