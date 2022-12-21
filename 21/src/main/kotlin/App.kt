import java.io.File

fun main(args: Array<String>) {
    Task21().run {
        val result = run(File(Task21.inputFilePath).readText())
        println("Result for part #1 is $result")
        val result2 = run(File(Task21.inputFilePath).readText(), true)
        println("Result for part #2 is $result2")
    }

}
