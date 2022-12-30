import java.io.File

fun main(args: Array<String>) {
    Task23().run {
        val result = run(File(Task23.inputFilePath).readText())
        println("Result for part #1 is $result")
        //28927640190471 is wrong
    }

}
