import java.io.InputStream

fun interface DemoAssetManager {
  fun open(fileName: String): InputStream
}
