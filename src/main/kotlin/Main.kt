import java.io.File
import java.util.*

object Hello {
    init {
        loadLibraryByResources("jni_dome")
        loadLibraryByResources("rust")
    }

    fun loadLibraryByResources(libraryName: String) {
        val libraryFileName ="${libraryName}.${System.getProperty("os.name").lowercase(Locale.getDefault()).takeIf { it.contains("win") }?.let { "dll" } ?: "so"}"
        this.javaClass.getResource(libraryFileName)?.let {
            val dllFile = File(it.file)
            val temp = File.createTempFile(libraryFileName, "")
            temp.writeBytes(dllFile.readBytes())

            System.getLogger("load ${temp.path}")
            System.load(temp.path)
        }
    }

    var count: Int
        external get
        external set

    external fun hello(input: String) : String

    @JvmStatic
    fun main(args: Array<String>) {
        println(count)
        count = 12345
        println(count)

        println(hello("Sagiri"))
    }
}