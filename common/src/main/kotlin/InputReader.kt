import org.slf4j.LoggerFactory
import java.io.File

class InputReader {

    companion object{
        private val logger = LoggerFactory.getLogger(InputReader::class.java)
    }
    fun withFileLines(filePath: String, process: (List<String>) -> Any?): Any?{
        logger.info("reading the file: $filePath")
        val lines = File(filePath).readLines()
        return process(lines)
    }
}