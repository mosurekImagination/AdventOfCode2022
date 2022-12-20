import java.io.File

fun main(args: Array<String>) {
    Task19().run {
        val result = run(File(Task19.inputFilePath).readText())
        println("Result for part #1 is $result")
        val result2 = run(File(Task19.inputFilePath).readText(), true)
        println("Result for part #2 is $result2")
    }
}
