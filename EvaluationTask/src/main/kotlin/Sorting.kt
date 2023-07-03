import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class Sorting {

    fun bubbleSort():IntArray{
        val array: IntArray = intArrayOf(1, 12,45646,56,112,678,234,7,56,2343,65674)
        val n = array.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                if (array[j] > array[j + 1]) {
                    val temp = array[j]
                    array[j] = array[j + 1]
                    array[j + 1] = temp
                }
            }
        }
        return array
    }
    suspend fun selectionSort():IntArray{
        val array: IntArray = intArrayOf(1, 12,45646,56,112,678,234,7,56,2343,65674)
        val n = array.size
        for (i in 0 until n - 1) {
            var minIndex = i
            for (j in i + 1 until n) {
                delay(10)

                if (array[j] < array[minIndex]) {
                    minIndex = j
                }
            }
            val temp = array[minIndex]
            array[minIndex] = array[i]
            array[i] = temp
        }
        return array

    }
    suspend fun insertionSort():IntArray{
        val array: IntArray = intArrayOf(1, 12,45646,56,112,678,234,7,56,2343,65674)
        val n = array.size
        delay(100)
        for (i in 1 until n) {
            val key = array[i]
            var j = i - 1
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j]
                j--
            }
            array[j + 1] = key
        }
        return array
    }

}
fun main()= runBlocking {
    val sorting=Sorting()
    val result1 = async { sorting.bubbleSort() }
    val result2 = async { sorting.insertionSort() }
    val result3 = async { sorting.selectionSort() }
    println("After Bubble Sort ${result1.await().joinToString()} \n")
    println("After Insertion Sort ${result2.await().joinToString()}\n" )
    println("After Selection Sort  ${result3.await().joinToString()}")

}