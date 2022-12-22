import java.io.File

fun main(args: Array<String>) {
    Task22().run {
        val result = run(File(Task22.inputFilePath).readText())
        println("Result for part #1 is $result")
    }

}
