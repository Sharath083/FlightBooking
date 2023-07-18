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
                output->input.copyTo(output) }
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
        "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8Y2hlbm5haXxlbnwwfHwwfHx8MA%3D%3D&w=1000&q=80"
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