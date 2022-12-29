import java.io.File

fun main(args: Array<String>) {
    Task23().run {
        val result = run(File(Task23.inputFilePath).readText())
        println("Result for part #1 is $result")
        val result2 = run(File(Task23.inputFilePath).readText(), true)
        println("Result for part #2 is $result2")
        //862 too low
        //863 too low
    }

}
