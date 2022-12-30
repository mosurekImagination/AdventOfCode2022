import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

internal class Task23Test {

    private val task = Task23()
    private val example = this::class.java.getResource("/example.txt").path

    @Test
    fun `example should work`() {
        val result = task.run(File(example).readText())
        assertEquals(110, result)
    }

    @Test
    fun `part 2 example should work`() {
        val result = task.run(File(example).readText(), true)
        assertEquals(20, result)
    }

    @Test
    fun `get elves ready to move should work`() {
        val matrix = task.ensureMatrixSize(task.extractMatrix(
            """ 
                |#...#.
                |..#...
                |#...#.
        """.trimMargin()
        ))
        val actual = task.getElfsReadyToMove(matrix)
        Assertions.assertThat(actual)
            .containsExactlyInAnyOrderElementsOf(emptyList());
    }

    @Test
    fun `get elves ready to move should work 2`() {
        val matrix = task.ensureMatrixSize(task.extractMatrix(
            """ 
                |##.
                |..#
                |#.#
        """.trimMargin()
        ))
        val actual = task.getElfsReadyToMove(matrix)
        assertEquals(4, actual.size)
    }

    @Test
    fun `getElfsReadyToMove should work`() {
        val matrix = task.extractMatrix(
            """ 
                |.........
                |.#.....#.
                |..#.#.#..
                |.#.....#.
                |.........
        """.trimMargin()
        )
        val expected = listOf(
            Task23.Coordinate(1, 1),
            Task23.Coordinate(3, 1),
            Task23.Coordinate(2, 2),
            Task23.Coordinate(1, 7),
            Task23.Coordinate(2, 6),
            Task23.Coordinate(3, 7),
        )
        val actual = task.getElfsReadyToMove(matrix)
        Assertions.assertThat(actual)
            .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    fun `remove empty columns should work`() {
        val matrix = task.extractMatrix(
            """ 
                |........
                |........
                |....#.#.
                |........
                |........
        """.trimMargin()
        )
        val expected = listOf(
            listOf(Task23.MatrixItem.ELF,
            Task23.MatrixItem.AIR,
            Task23.MatrixItem.ELF)
        )
        val actual = task.removeEmptySpaces(matrix)
        assertEquals(expected, actual)
    }

    @Test
    fun `ensure Matrix size should work`() {
        val matrix = task.extractMatrix(
            """ 
                |#
        """.trimMargin()
        )
        val expected = mutableListOf(
            mutableListOf(
                Task23.MatrixItem.AIR,
                Task23.MatrixItem.AIR,
                Task23.MatrixItem.AIR
            ),
            mutableListOf(
                Task23.MatrixItem.AIR,
                Task23.MatrixItem.ELF,
                Task23.MatrixItem.AIR
            ), mutableListOf(
                Task23.MatrixItem.AIR,
                Task23.MatrixItem.AIR,
                Task23.MatrixItem.AIR
            )
        )
        val actual = task.ensureMatrixSize(matrix)
        assertEquals(expected, actual)
    }

    @Test
    fun `ensure isCapable of moving`() {
        val matrix = task.extractMatrix(
            """ 
                |.....
                |.#.#.
                |..#..
                |.#.#.
                |.#...
        """.trimMargin()
        )
        task.isCapableToMoveInto(
            Task23.Coordinate(1, 1), Task23.Direction.N,
            matrix
        ).run {
            assertEquals(true, this)
        }
        task.isCapableToMoveInto(
            Task23.Coordinate(1, 1), Task23.Direction.W,
            matrix
        ).run {
            assertEquals(true, this)
        }
        Task23.Direction.values().map { direction ->
            task.isCapableToMoveInto(
                Task23.Coordinate(2, 2), direction,
                matrix
            ).run {
                assertEquals(false, this)
            }
        }
    }

    @Test
    fun `remove clashing propositions should work`() {
        val elf = Task23.Coordinate(0, 0)
        val propositions = listOf(
            Task23.Proposition(elf, Task23.Coordinate(0, 1)),
            Task23.Proposition(elf.copy(x=7), Task23.Coordinate(0, 2)),
            Task23.Proposition(elf.copy(x=6), Task23.Coordinate(1, 0)),
            Task23.Proposition(elf.copy(x=5), Task23.Coordinate(1, 1)),
            Task23.Proposition(elf.copy(x=4), Task23.Coordinate(1, 1)),
            Task23.Proposition(elf.copy(x=3), Task23.Coordinate(1, 1)),
            Task23.Proposition(elf.copy(x=2), Task23.Coordinate(0, 0)),
            Task23.Proposition(elf.copy(x=1), Task23.Coordinate(0, 0))
        ).toMutableList()

        val actual = task.getPropositionsWithoutDuplicates(propositions)
        val expected = propositions.take(3)
        assertEquals(expected, actual)
    }

    @Test
    fun `move elfs should work`() {
        val matrix = task.extractMatrix(
            """ 
                |.....
                |.#.#.
                |..#..
                |.#.#.
                |.#...
        """.trimMargin()
        )
        val propositions = mutableListOf(
            Task23.Proposition(Task23.Coordinate(1, 1), Task23.Coordinate(0, 1)),
            Task23.Proposition(Task23.Coordinate(1, 3), Task23.Coordinate(2, 3))
        )
        task.moveElfs(matrix, propositions)
        assert(matrix[0][1] == Task23.MatrixItem.ELF)
        assert(matrix[1][1] == Task23.MatrixItem.AIR)
        assert(matrix[2][3] == Task23.MatrixItem.ELF)
        assert(matrix[1][3] == Task23.MatrixItem.AIR)
    }

    @Test
    fun `remove empty spaces should work`() {
        val matrix = task.extractMatrix(
            """ 
                |.....
                |.#.#.
                |.....
        """.trimMargin()
        )
        val actual = task.removeEmptySpaces(matrix)
        val expected = mutableListOf(mutableListOf(Task23.MatrixItem.ELF, Task23.MatrixItem.AIR, Task23.MatrixItem.ELF))
        assertEquals(expected, actual)
    }
    @Test
    fun `simple example should work`() {
        val input =
            """ 
.....
..##.
..#..
.....
..##.
.....
        """.trimMargin()
        val actual = task.run(input)
        assertEquals(25, actual)
    }

    @Test
    fun `simple example should work for part 2`() {
        val input =
            """ 
.....
..##.
..#..
.....
..##.
.....
        """.trimMargin()
        val actual = task.run(input, true)
        assertEquals(4, actual)
    }
}