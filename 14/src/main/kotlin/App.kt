fun main(args: Array<String>) {
    Task14().run {
        val result = run(Task14.inputFilePath)
        println("Result for part #1 is $result")
        val result2 = run(Task14.inputFilePath, Task14.part2Logic, Task14.part2BoardModification)
        println("Result for part #2 is $result2")
    }
}
