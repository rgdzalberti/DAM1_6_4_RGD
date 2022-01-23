import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import java.util.logging.Level
import java.util.logging.LogManager

val l = LogManager.getLogManager().getLogger("").apply { level = Level.OFF }

class CatalogoLibrosXML(ruta: String) {

    val xmlDoc = readXml(ruta)

    init {constructor(ruta)}

    //No estoy seguro a que te refieres con abortar. Lo voy a interpretar como que se para el programa si el path introducido está mal
    private fun constructor(cargador: String) = try { readXml(cargador) } catch (e: java.io.FileNotFoundException) { System.exit(-1) }

    fun existeLibro(id: String): Boolean {
        val bookList: NodeList = xmlDoc.getElementsByTagName("book")
        //La siguiente variable estará por defecto en false y se cambiará a true si se encuentra la id en una posterior línea. El return del método será esta variable
        var existeBoolean: Boolean = false
        for (i in 0 until bookList.length) {
            var bookNode: Node = bookList.item(i)

            if (bookNode.getNodeType() === Node.ELEMENT_NODE) {
                val elem = bookNode as Element
                val mMap = mutableMapOf<String, String>()

                for (j in 0..elem.attributes.length - 1) {
                    mMap.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
                }
                if (mMap.containsValue(id)) {
                    existeBoolean = true
                }
            }
        }
        return existeBoolean
    }

    fun infoLibro(idLibro: String): Map<String,Any> {

        val map = mutableMapOf<String,Any>()
        val lista = obtenerListaNodosPorNombre(xmlDoc, "book")

        for (j in 0 until lista.size) {

            val elem = lista[j] as Element
            val mMap = obtenerAtributosEnMapKV(elem)

            for (i in 0 until mMap.size)
            {
                if (mMap.containsValue(idLibro)) {
                    map["id"] = idLibro
                    map["Author"] = elem.getElementsByTagName("author").item(i).textContent
                    map["Title"] = elem.getElementsByTagName("title").item(i).textContent
                    map["Genre"] = elem.getElementsByTagName("genre").item(i).textContent
                    map["Price"] = elem.getElementsByTagName("price").item(i).textContent
                    map["Publish Date"] = elem.getElementsByTagName("publish_date").item(i).textContent
                    map["Description"] = elem.getElementsByTagName("description").item(i).textContent

                }
            }
        }
        return map
    }

    private fun readXml(pathName: String): Document {
        val xmlFile = File(pathName)
        return  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)
    }

    private fun obtenerAtributosEnMapKV(e: Element ):MutableMap<String, String>
    {
        val mMap = mutableMapOf<String, String>()
        for(j in 0..e.attributes.length - 1)
            mMap.putIfAbsent(e.attributes.item(j).nodeName, e.attributes.item(j).nodeValue)
        return mMap
    }

    private fun obtenerListaNodosPorNombre(doc: Document, tagName: String): MutableList<Node>
    {
        val bookList: NodeList = doc.getElementsByTagName(tagName)
        val lista = mutableListOf<Node>()
        for(i in 0..bookList.length - 1)
            lista.add(bookList.item(i))
        return lista
    }
}




fun main() {

    val Libro = CatalogoLibrosXML("C:\\Users\\Ricar\\Desktop\\Programacion\\XML.xml")

    //El constructor compruebo que funciona con el init

    //Compruebo que funciona el método existeLibro
    println(Libro.existeLibro("bk101"))
    l.info("He comprobado que existe el libro.")

    //Compruebo que funciona el método infoLibro
    println(Libro.infoLibro("bk101"))
    l.info("He comprobado los diferentes datos del libro.")


}