import java.lang.RuntimeException
import java.math.BigInteger

class Task23 {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    val i5 = BigInteger.valueOf(5L)
    fun run(
        input: String
    ) = input.lines().sumOf { parseNumber(it) }

    fun parseNumber(it: String):BigInteger {
        val number = it.toCharArray().toList().reversed()
        val result = number.foldIndexed(BigInteger.ZERO){ index: Int, acc: BigInteger, c: Char -> acc + "5".toBigInteger().pow(index) * getValue(c) }
        return result
    }

    fun encodeNumber(it: BigInteger):String {
        //5^x - 2x5^x-1 > it => x = x-1
        var maxPow = 0
        while(getminValueOfPower(maxPow) <= it ) maxPow ++
        var number = (0 until maxPow).map { "." }.toMutableList()
        var currentPower = maxPow
        var remainingNumber = it

        while (currentPower > 0){
            currentPower--
            if (i5.pow(currentPower) * BigInteger.TWO <= remainingNumber){
                number[currentPower] = "2"
                remainingNumber = remainingNumber - i5.pow(currentPower) * BigInteger.TWO
            } else if (i5.pow(currentPower) <= remainingNumber){
                number[currentPower] = "1"
                remainingNumber = remainingNumber - i5.pow(currentPower)
            } else {
                number[currentPower] = "0"
            }
        }
        return number.joinToString(separator = "")
    }

    private fun getminValueOfPower(x: Int): BigInteger {
        if (x == 0) return "0".toBigInteger()
        return i5.pow(x) - "2".toBigInteger() * i5.pow(x - 1)
    }

    private fun getValue(c: Char): BigInteger {
        return if(c.isDigit()) c.toString().toBigInteger()
        else when(c){
            '-' -> "-1".toBigInteger()
            '=' -> "-2".toBigInteger()
            else -> throw RuntimeException("")
        }
    }


}


