class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(filePath: String) =
        inputReader.withFileLines(filePath) { l ->
            val input = l.map {
                it.map { it.digitToIntOrNull()!! }
            }
            val linesRange = (1 until input.size - 1)
            val treeLineSize = input.first().size
            val treeLineRange = (1 until treeLineSize - 1)
            var visibleCount = 0
            for (i in linesRange) {
                for (j in treeLineRange) {
                    val tree = input[i][j]
                    val isVisible = input[i].subList(0, j).maxOrNull()!! < tree ||
                            input[i].subList(j + 1, treeLineSize).maxOrNull()!! < tree ||
                            (0 until i).map { input[it][j] }.maxOrNull()!! < tree ||
                            (i + 1 until input.size).map { input[it][j] }.maxOrNull()!! < tree
                    if (isVisible) visibleCount++
                }
            }
            visibleCount + 2 * treeLineSize + 2 * input.size - 4
        }

    fun part2(filePath: String) =
        inputReader.withFileLines(filePath) { l ->
            val input = l.map {
                it.map { it.digitToIntOrNull()!! }
            }
            val linesRange = (0 until input.size)
            val treeLineSize = input.first().size
            val treeLineRange = (0 until treeLineSize)
            var max = 0
            for (i in linesRange) {
                for (j in treeLineRange) {
                    val tree = input[i][j]
                    println("processing $i, $j with value $tree")
                    val left = input[i].subList(0, j).reversed().getScenicScore { it >= tree }
                    val right = input[i].subList(j + 1, treeLineSize).getScenicScore { it >= tree }
                    val up = (0 until i).map { input[it][j] }.reversed().getScenicScore { it >= tree }
                    val down = (i + 1 until input.size).map { input[it][j] }.getScenicScore { it >= tree }
                    val currentMax = left * right * up * down
                    println("result if left: $left, right: $right, up: $up, down: $down, result: $currentMax")
                    println("left $left")
                    println("right $right")
                    println("up $up")
                    println("down $down")
                    if(currentMax > max) max = currentMax
                }
            }
            max
        }

    fun <T> List<T>.getScenicScore(predicate: (T) -> Boolean): Int {
        val result = this.indexOfFirst(predicate)
        return if (result == -1) this.size
        else result + 1
    }
}

