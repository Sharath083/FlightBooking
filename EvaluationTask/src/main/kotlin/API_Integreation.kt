import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import okhttp3.*


fun main()= runBlocking {
    val api1="https://restcountries.com/v3.1/translation/germany"
    val api2="https://jsonplaceholder.typicode.com/todos/1"
    val api3="https://restcountries.com/v3.1/name/Jordan"

    val client= OkHttpClient()
//    val jsonFormat= Json { prettyPrint=true }

    data class Output(val info1: String?,val info2: String?,val info3: String?)


    val result1=async {
        requestPass(client,api1)
    }
    val result2=async {
        requestPass(client,api2)
    }
    val result3=async {
        requestPass(client,api3)
    }


//    println(result1.await().body?.string()?.let { jsonFormat.parseToJsonElement(it) })
    val apiOutput= Output(
        result1.await().body?.string(),
        result2.await().body?.string(),
        result3.await().body?.string()
    )

    println(apiOutput)
//    println("API1:\n  ${result1.await().body?.string()}")
//    println("API2:\n  ${result2.await().body?.string()}")
//    println("API3:\n  ${result3.await().body?.string()}")

}
fun requestPass(client: OkHttpClient, link:String): Response {
    val request= Request.Builder().url(link).build()
    return client.newCall(request).execute()
}




