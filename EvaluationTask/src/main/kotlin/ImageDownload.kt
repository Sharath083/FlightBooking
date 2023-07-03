import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.URL

class ImageDownload {
    fun download(link:String,location:String){
        val listLinks= URL(link)
        val file= File(location)
        listLinks.openStream().use {
                input->file.outputStream().use {
                output->input.copyTo(output)
        }
        }
        println("Downloaded and Stored At $location")
    }
}
fun main() {
    val images: MutableList<String> = mutableListOf(
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
        "http://localhost:8080/customer/download",
    )
    runBlocking {
        val imageDownload=ImageDownload()
        val result =
            async {
                var length = images.size-1
                while (length >= 0) {
                    val location = "C:\\Users\\sharathkumar.b\\Desktop\\images/${"image${length}"}"
                    imageDownload.download(images[length], location)
                    length--

                }
            }

        result.await()
    }

}