import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    Task11().run{
        val result = run(Task11.inputFilePath)
        println("Result for part #1 is $result")
        val leastCommonMultiple = "9699690".toBigInteger()
        println(measureTimeMillis {
            val result2 = run(Task11.inputFilePath, 10000) { it % leastCommonMultiple }
            println("Result for part #2 is $result2")
        })
    }
}
