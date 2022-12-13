fun main(args: Array<String>) {
    Task13().run {
        val result = run(Task13.inputFilePath)
        println("Result for part #1 is $result")
        val result2 = part2(Task13.inputFilePath)
        println("Result for part #2 is $result2")
    }
}
