fun main(args: Array<String>) {
    Task15().run {
        val result = run(Task15.inputFilePath, 2000000)
        println("Result for part #1 is $result")

        val result2 = part2(Task15.inputFilePath, 4000000)
        println("Result for part #2 is $result2")
    }
}
