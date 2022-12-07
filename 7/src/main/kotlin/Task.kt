class Task(
    private val inputReader: InputReader = InputReader()
) {
    companion object {
        val inputFilePath: String = this::class.java.getResource("/input.txt").path
    }

    private val MIN_REQUIRED_SIZE = 30000000
    private val DISK_SPACE = 70000000

    fun run(filePath: String) =
        inputReader.withFileLines(filePath) { lines ->
            buildDirTree(lines)
                .getAllDirectories()
                .filter { it.size <= 100000 }
                .sumOf { it.size }
        }

    fun findSizeOfSmallestFolderToDelete(filePath: String) =
        inputReader.withFileLines(filePath) { lines ->
            val directories = buildDirTree(lines)
                .getAllDirectories()
            with(directories) {
                val storageConsumption = first().size
                val storageToBeDeleted = storageConsumption + MIN_REQUIRED_SIZE - DISK_SPACE
                this.filter { it.size >= storageToBeDeleted }
                    .minByOrNull { it.size }!!.size
            }
        }

    private fun buildDirTree(lines: List<String>): TreeNode {
        val root = TreeNode(FileType.DIR, 0, "", null)
        lines.fold(root) { node, line ->
            when {
                line isCommand "cd" -> when (val path = line.substringAfterLast(" ")) {
                    ".." -> node.parent!!
                    else -> node.addChild(FileType.DIR, 0, path)
                }
                line isCommand "ls" -> node
                else -> {
                    val split = line.split(" ")
                    extractSize(line)?.let {
                        node.addChild(FileType.FILE, it, split.last())
                    }
                    node
                }
            }
        }
        return root.children.first().apply {
            calculateDirSizes()
        }
    }

    private infix fun String.isCommand(commandType: String) = startsWith("$ $commandType")

    private fun extractSize(line: String): Int? = line.split(" ").first().toIntOrNull()

    enum class FileType {
        DIR,
        FILE
    }

    data class TreeNode(
        val fileType: FileType,
        var size: Int,
        val path: String,
        val parent: TreeNode?,
        val children: MutableList<TreeNode> = mutableListOf()
    ) {

        fun addChild(fileType: FileType, size: Int, path: String) =
            TreeNode(fileType, size, path, this).also {
                children.add(it)
            }

        fun calculateDirSizes(): Int {
            if (fileType == FileType.DIR) {
                size = children.sumOf { it.calculateDirSizes() }
            }
            return size
        }

        fun getAllDirectories(): List<TreeNode> =
            when (fileType) {
                FileType.DIR -> listOf(this) + children.flatMap { it.getAllDirectories() }
                else -> emptyList()
            }

    }
}

