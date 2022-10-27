import java.io.File
import java.util.*

object Hello {
    init {
        val libraryFileName ="jni_dome.${System.getProperty("os.name").lowercase(Locale.getDefault()).takeIf { it.contains("win") }?.let { "dll" } ?: "so"}"
        this.javaClass.getResource(libraryFileName)?.let {
            val dllFile = File(it.file)
            val temp = File.createTempFile(libraryFileName, "")
            temp.writeBytes(dllFile.readBytes())

            System.load(temp.path)
        }
    }

    var count: Int
        external get
        external set

    @JvmStatic
    fun main(args: Array<String>) {
        println(count)
        count = 12345
        println(count)
    }
}