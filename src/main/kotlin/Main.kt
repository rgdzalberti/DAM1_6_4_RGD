import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import java.util.logging.Level
import java.util.logging.LogManager

val l = LogManager.getLogManager().getLogger("").apply { level = Level.OFF }

/**
 * Esta es nuestra clase, a la que le introduciremos de input la ruta del archivo.
 * @param ruta es el input de nuestra clase.
 */
class CatalogoLibrosXML(ruta: String) {

    val xmlDoc = readXml(ruta)

    /**
     * Inicializamos el constructor comprobando que la ruta existe. Lo inicializamos ahora por jerarquía normativa.
     */
    init {constructor(ruta)}

    private fun constructor(cargador: String) = try { readXml(cargador) } catch (e: java.io.FileNotFoundException) { System.exit(-1) }

    /**
     * El método existeLibro determinará si el libro introducido existe y devuelve @return [Boolean] según el resultado.
     */
    fun existeLibro(id: String): Boolean {
        val bookList: NodeList = xmlDoc.getElementsByTagName("book")
        //La siguiente variable estará por defecto en false y se cambiará a true si se encuentra la id en una posterior línea. El return del método será esta variable
        var existeBoolean: Boolean = false
        /**
         * En este método se creará una lista de nodos a buscar según el tag introducido, en este caso será "book".
         */
        for (i in 0 until bookList.length) {
            var bookNode: Node = bookList.item(i)
            /**
             * Después se buscará si el nodo es igual al que se está buscando y si es así se comprobará que contiene la id introducida incial y finalmente devuelve el boolean de variable @return [existeBoolean]
             */
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

    /**
     * El método infoLibro devolverá un mapa con los tags y respectiva información del libro a introducir con @param idLibro.
     */
    fun infoLibro(idLibro: String): Map<String,Any> {

        val map = mutableMapOf<String,Any>()
        val lista = obtenerListaNodosPorNombre(xmlDoc, "book")

        for (j in 0 until lista.size) {

            val elem = lista[j] as Element
            val mMap = obtenerAtributosEnMapKV(elem)

            /**
             * Con el mapa creado se buscará si el mapa en las distintas posiciones contiene el valor introducido con idLibro.
             */
            for (i in 0 until mMap.size)
            {
                /**
                 * De ser así, se rellenará el mapa con la información correspondiente y se devolverá.
                 */
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