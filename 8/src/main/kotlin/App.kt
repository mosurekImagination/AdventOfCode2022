
fun main(args: Array<String>) {
    Task().run{
        val result = run(Task.inputFilePath)
        println("Result for part #1 is $result")
        val result2 = part2(Task.inputFilePath)
        println("Result for part #2 is $result2")
    }
}
