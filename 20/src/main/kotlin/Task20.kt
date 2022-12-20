import java.math.BigInteger

class Task20 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    fun run(
        input: String,
        decryptionKey: BigInteger = BigInteger.ONE,
        rounds: Int = 1
    ) = getTargetList(input, decryptionKey, rounds).run {
        val zeroIndex = this.indexOfFirst { it == BigInteger.ZERO }
        val firstIndex = zeroIndex + 1000
        val secondIndex = zeroIndex + 2000
        val thirdIndex = zeroIndex + 3000
        val first = this[firstIndex % size]
        val second = this[secondIndex % size]
        val third = this[thirdIndex % size]
        println("$first, $second, $third")
        first + second + third
    }

    fun getTargetList(input: String, decryptionKey: BigInteger = BigInteger.ONE, rounds: Int = 1) = input.lines()
        .mapIndexed { index, it -> Entry(it.toBigInteger() * decryptionKey, index) }
        .run {
            val originalList = this
            val mutableList = this.toMutableList()
            val size = originalList.size
            (0 until rounds).forEach { _ ->
                originalList.forEach { original ->
                    val entryIndex = mutableList.indexOf(original)
                    val entry = mutableList[entryIndex]
                    val targetIndex = entryIndex.toBigInteger() + entry.value
                    val size = size.toBigInteger()
                    val moduloIndex = if (targetIndex >= BigInteger.ZERO && targetIndex < size) targetIndex
                    else if (entry.value >= BigInteger.ZERO) targetIndex % (size - BigInteger.ONE)
                    else size - (targetIndex % (size - BigInteger.ONE)).abs() - BigInteger.ONE
                    mutableList.remove(entry)
                    mutableList.add(moduloIndex.toInt(), entry)
                }
            }
            mutableList.map { it.value }
        }

    data class Entry(val value: BigInteger, val index: Int)
}


