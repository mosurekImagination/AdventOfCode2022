class Task10(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(filePath: String):Status =
        inputReader.withFileLines(filePath) { l ->
            l.map {
                if (it isOperation "noop") Operation(1, 0)
                else it.split(" ").run { Operation(2, this[1].toInt()) }
            }.flatMap { spreadOperations(it) }
                .fold(Status(0, 1, 0, (0 until 40 * 6).map { "." }.toMutableList())) { acc, operation ->
                    showSprite(acc.registry)
                    showImage(acc.image)

                    val cycle = acc.cycle + operation.remainingCycles
                    val registry = acc.registry + operation.registerIncrease
                    val signalStrength = if ((cycle - 20) % 40 == 0) {
                        println(
                            "Adding signal strength cycle: $cycle * $registry, strength ${cycle * registry}"
                        )
                        cycle * acc.registry
                    } else 0
                    if (listOf(acc.registry - 1, acc.registry, acc.registry + 1).contains(acc.cycle % 40)) {
                        acc.image[acc.cycle] = "#"
                    }
                    Status(cycle, registry, acc.signalStrength + signalStrength, acc.image)
                }
        } as Status

    fun showSprite(middle: Int) {
        if (middle > 0 && middle < 40) {
            val list = (0..40).map { "." }.toMutableList()
            list[middle - 1] = "#"
            list[middle] = "#"
            list[middle + 1] = "#"
            println("Sprite:")
            println(list.joinToString(""))
        }
    }

    fun showImage(image: List<String>) {
        image.chunked(40).map {
            println(it.joinToString(""))
        }
        println("----")
    }

    private fun spreadOperations(it: Operation) =
        (1 until it.remainingCycles).map { Operation(1, 0) } + listOf(Operation(1, it.registerIncrease))

    data class Status(val cycle: Int, val registry: Int, val signalStrength: Int, val image: MutableList<String>)
    data class Operation(val remainingCycles: Int, val registerIncrease: Int)

    infix fun String.isOperation(operationType: String) = this.startsWith("$operationType")
}

