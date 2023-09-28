import java.io.EOFException
import java.io.FileInputStream
import java.io.ObjectInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun main(args: Array<String>) {
    val f = ObjectInputStream(FileInputStream ("Rutes.obj"))

    val doc = DocumentBuilderFactory.newInstance ().newDocumentBuilder().newDocument()
    val arrel = doc.createElement ("rutes")
    doc.appendChild(arrel)

    try {
        while (true) {
            val e = f.readObject () as Ruta
            val rut = doc.createElement ("ruta")
            //rut.setAttribute("numero", Integer.toString(e.num))

            val nom = doc.createElement ("nom")
            nom.appendChild(doc.createTextNode(e.nom)) // forma llarga: afegim un fill que és un node de text
            rut.appendChild(nom)

            val des = doc.createElement("desnivell")
            des.setTextContent(e.desnivell.toString()) // forma curta: amb setTextContent() li posem contingut
            rut.appendChild(des)

            val decAcu = doc.createElement("edat")
            decAcu.setTextContent(e.desnivellAcumulat.toString())
            rut.appendChild(decAcu)

            val punts = doc.createElement("punts");
            //punts.setTextContent(e.p.toString())
            //rut.appendChild(punts)
            var numPunts = e.size()
            for(i in 0 until numPunts){
                val punt = doc.createElement("punt")
                punt.setAttribute("num", Integer.toString(i +1))
                punts.appendChild(punt)

                val nom = doc.createElement("nom")
                nom.setTextContent(e.getPuntNom(i))
                punt.appendChild(nom)

                val lat = doc.createElement("latitud")
                lat.setTextContent(e.getPuntLatitud(i).toString())
                punt.appendChild(lat)

                val long = doc.createElement("longitud")
                long.setTextContent(e.getPuntLongitud(i ).toString())
                punt.appendChild(long)
                punts.appendChild(punt)
            }
            arrel.appendChild(rut)
            rut.appendChild(punts)
        }

    } catch (eof: EOFException) {
        f.close();
    }
    val trans = TransformerFactory.newInstance().newTransformer()

    trans.setOutputProperty(OutputKeys.INDENT, "yes")
    trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
    trans.transform(DOMSource(doc), StreamResult("Rutes.xml"))
}