package projekt.progtech.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import projekt.progtech.model.Board;
import projekt.progtech.model.Position;

/**
 * Fájlkezelésért felelős osztály - XML alapú mentés/betöltés.
 */
public class FileManager {

  private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

  /**
   * Tábla mentése XML fájlba.
   *
   * @param tabla   a mentendő tábla
   * @param fajlnev a fájl neve (ajánlott .xml)
   * @return true, ha sikeres volt
   */
  public boolean ment(Board tabla, String fajlnev) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.newDocument();

      Element gyoker = doc.createElement("tabla");
      gyoker.setAttribute("sorok", String.valueOf(tabla.getSorokSzama()));
      gyoker.setAttribute("oszlopok", String.valueOf(tabla.getOszlopokSzama()));
      doc.appendChild(gyoker);

      for (int i = 0; i < tabla.getSorokSzama(); i++) {
        Element sorElem = doc.createElement("sor");
        sorElem.setAttribute("index", String.valueOf(i));
        StringBuilder sb = new StringBuilder(tabla.getOszlopokSzama());
        for (int j = 0; j < tabla.getOszlopokSzama(); j++) {
          char ch = tabla.getMezo(new Position(i, j));
          sb.append(ch);
        }
        sorElem.setTextContent(sb.toString());
        gyoker.appendChild(sorElem);
      }

      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());

      try (FileOutputStream fos = new FileOutputStream(fajlnev)) {
        transformer.transform(new DOMSource(doc), new StreamResult(fos));
      }

      logger.info("Tábla sikeresen mentve (XML): {}", fajlnev);
      return true;
    } catch (ParserConfigurationException | TransformerException | IOException e) {
      logger.error("Hiba az XML mentés során: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Tábla betöltése XML fájlból.
   *
   * @param fajlnev a fájl neve
   * @return a betöltött tábla, vagy null ha hiba volt
   */
  public Board betolt(String fajlnev) {
    File fajl = new File(fajlnev);
    if (!fajl.exists()) {
      logger.warn("A fájl nem létezik: {}", fajlnev);
      return null;
    }

    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(fajl);

      Element gyoker = doc.getDocumentElement();
      int sorok = Integer.parseInt(gyoker.getAttribute("sorok"));
      int oszlopok = Integer.parseInt(gyoker.getAttribute("oszlopok"));

      Board tabla = new Board(sorok, oszlopok);

      NodeList sorokLista = gyoker.getElementsByTagName("sor");
      for (int i = 0; i < sorokLista.getLength(); i++) {
        Element sorElem = (Element) sorokLista.item(i);
        int index = Integer.parseInt(sorElem.getAttribute("index"));
        String tartalom = sorElem.getTextContent();
        for (int j = 0; j < tartalom.length() && j < oszlopok; j++) {
          char ch = tartalom.charAt(j);
          if (ch != '.') {
            tabla.lerak(new Position(index, j), ch);
          }
        }
      }

      logger.info("Tábla sikeresen betöltve (XML): {}", fajlnev);
      return tabla;

    } catch (Exception e) {
      logger.error("Hiba az XML betöltés során: {}", e.getMessage());
      return null;
    }
  }
}