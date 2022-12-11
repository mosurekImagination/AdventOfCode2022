import java.io.File
import java.lang.RuntimeException
import java.math.BigInteger

class Task11(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(
        filePath: String,
        roundsCount: Int = 20,
        worryRelief: (BigInteger) -> BigInteger = { it / "3".toBigInteger() }
    ) =
        File(filePath)
            .readLines()
            .chunked(7)
            .fold(listOf<Monkey>()) { acc, monkeyInput ->
                val itemsWorryLevels =
                    monkeyInput[1].substringAfter(": ").replace(" ", "").split(",").map { it.toBigInteger() }
                val operation = monkeyInput[2].substringAfter("new = ").parseOperation()
                val divisibleNumber = monkeyInput[3].substringAfterLast(" ").toBigInteger()
                val ifTrueMonkeyIndex = monkeyInput[4].substringAfterLast(" ").toInt()
                val ifFalseMonkeyIndex = monkeyInput[5].substringAfterLast(" ").toInt()
                val monkeyTest =
                    { a: BigInteger -> if (a.mod(divisibleNumber) == BigInteger.ZERO) ifTrueMonkeyIndex else ifFalseMonkeyIndex }
                acc + listOf(Monkey(itemsWorryLevels.toMutableList(), operation, monkeyTest))
            }.run {
                val monkeys = this
                val inspectionCounts = monkeys.indices.map { 0 }.toMutableList()
                (0 until roundsCount).map { round ->
                    monkeys.mapIndexed { index, monkey ->
                        monkey.itemsWorryLevels
                            .map { monkey.inspection(it) }
                            .map(worryRelief)
                            .also { inspectionCounts[index] = inspectionCounts[index] + it.size }
                            .map { worryLevelItem ->
                                monkey.getThrownMonkeyTest(worryLevelItem).let {
                                    monkeys[it].itemsWorryLevels.add(worryLevelItem)
                                }
                            }
                        monkey.itemsWorryLevels.removeAll { true }
                    }
                    println("round: ${round + 1} finished, $inspectionCounts")
                }
                inspectionCounts
            }.sortedDescending().run {
            get(0).toLong() * get(1).toLong()
        }


    data class Monkey(
        val itemsWorryLevels: MutableList<BigInteger>,
        val inspection: (BigInteger) -> BigInteger,
        val getThrownMonkeyTest: (BigInteger) -> Int
    )

    private fun String.parseOperation(): (BigInteger) -> BigInteger {
        val number = this.substringAfterLast(" ").toBigIntegerOrNull()
        return when {
            this isOperation "+" -> { a -> a + (number ?: a) }
            this isOperation "*" -> { a -> a * (number ?: a) }
            else -> throw RuntimeException()
        }
    }

    private infix fun String.isOperation(operationMark: String): Boolean = this.contains(operationMark)
}


