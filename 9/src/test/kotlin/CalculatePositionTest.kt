import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CalculatePositionTest{

    val task2 = Task2()


    fun `shouldWork`(list: List<Int>){
        val previous = Task2.Coordinate(list[0],list[1])
        val current = Task2.Coordinate(list[2],list[3])
        val expected = Task2.Coordinate(list[4],list[5])
        val result = task2.calculateNewTailPosition(previous, current)
        assertEquals(expected,result)
    }

    @Test
    fun test(){
        shouldWork(listOf(
            0,0,
            2,2,
            1,1
        ))
        shouldWork(listOf(
            0,0,
            -2,-2,
            -1,-1
        ))
        shouldWork(listOf(
            0,0,
            -2,-1,
            -1,-1
        ))
        shouldWork(listOf(
            0,0,
            0,2,
            0,1
        ))
        shouldWork(listOf(
            0,0,
            0,-2,
            0,-1
        ))
        shouldWork(listOf(
            0,0,
            0,2,
            0,1
        ))
        shouldWork(listOf(
            0,0,
            0,-2,
            0,-1
        ))
        shouldWork(listOf(
            0,0,
            1,2,
            1,1
        ))
        shouldWork(listOf(
            0,0,
            2,1,
            1,1
        ))
        shouldWork(listOf(
            0,0,
            -2,1,
            -1,1
        ))
        shouldWork(listOf(
            0,0,
            -1,2,
            -1,1
        ))
        shouldWork(listOf(
            0,0,
            -1,-2,
            -1,-1
        ))
        shouldWork(listOf(
            0,0,
            1,-2,
            1,-1
        ))
        shouldWork(listOf(
            3,3,
            1,2,
            2,2
        ))
        shouldWork(listOf(
            3,0,
            4,2,
            4,1
        ))
    }
}