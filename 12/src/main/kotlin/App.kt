import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    Task12().run{
        val result = run(Task12.inputFilePath)
        println("Result for part #1 is $result")
        val milis = measureTimeMillis {
            val result2 = run(Task12.inputFilePath, 'a')
            println("Result for part #2 is $result2")
        }
        println("Part 2 took ${milis/1000} seconds")
    }
}
