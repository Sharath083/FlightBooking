
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


fun main()= runBlocking {
    val api1="https://restcountries.com/v3.1/translation/germany"
    val api2="https://jsonplaceholder.typicode.com/todos/1"
    val api3="https://restcountries.com/v3.1/name/Jordan"

    val client= HttpClient(CIO)

    val result1=async { client.get(api1)}
    val result2=async { client.get(api2)}
    val result3=async { client.get(api3)}


    println(result1.await())
    println(result2.await())
    println(result3.await())





}




