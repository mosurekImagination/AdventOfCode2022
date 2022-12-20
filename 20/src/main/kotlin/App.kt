import java.io.File

fun main(args: Array<String>) {
    Task20().run {
        val result = run(File(Task20.inputFilePath).readText())
        println("Result for part #1 is $result")
        val result2 = run(File(Task20.inputFilePath).readText(), "811589153".toBigInteger(), 10)
        println("Result for part #2 is $result2")
    }

}
