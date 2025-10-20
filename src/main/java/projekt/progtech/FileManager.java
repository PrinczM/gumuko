package projekt.progtech;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fájlkezelésért felelős osztály.
 */
public class FileManager {

  private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

  /**
   * Tábla mentése fájlba.
   *
   * @param tabla   a mentendő tábla
   * @param fajlnev a fájl neve
   * @return true, ha sikeres volt
   */
  public boolean ment(Board tabla, String fajlnev) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fajlnev))) {
      // Első sor: méretek
      writer.write(tabla.getSorokSzama() + " " + tabla.getOszlopokSzama());
      writer.newLine();

      // Tábla sorai
      for (int i = 0; i < tabla.getSorokSzama(); i++) {
        for (int j = 0; j < tabla.getOszlopokSzama(); j++) {
          Position poz = new Position(i, j);
          writer.write(tabla.getMezo(poz));
        }
        writer.newLine();
      }

      logger.info("Tábla sikeresen mentve: {}", fajlnev);
      return true;

    } catch (IOException e) {
      logger.error("Hiba a mentés során: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Tábla betöltése fájlból.
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

    try (BufferedReader reader = new BufferedReader(new FileReader(fajlnev))) {
      // Első sor: méretek
      String elsoSor = reader.readLine();
      String[] meretek = elsoSor.split(" ");
      int sorokSzama = Integer.parseInt(meretek[0]);
      int oszlopokSzama = Integer.parseInt(meretek[1]);

      // Új tábla létrehozása
      Board tabla = new Board(sorokSzama, oszlopokSzama);

      // Sorok beolvasása
      for (int i = 0; i < sorokSzama; i++) {
        String sor = reader.readLine();
        for (int j = 0; j < oszlopokSzama; j++) {
          char karakter = sor.charAt(j);
          if (karakter != '.') {
            Position poz = new Position(i, j);
            tabla.lerak(poz, karakter);
          }
        }
      }

      logger.info("Tábla sikeresen betöltve: {}", fajlnev);
      return tabla;

    } catch (IOException e) {
      logger.error("Hiba a betöltés során: {}", e.getMessage());
      return null;
    } catch (NumberFormatException e) {
      logger.error("Hibás fájlformátum: {}", e.getMessage());
      return null;
    }
  }
}