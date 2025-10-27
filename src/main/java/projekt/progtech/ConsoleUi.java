package projekt.progtech;

import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Konzol alapú felhasználói felület.
 */
public class ConsoleUi {

  private static final Logger logger = LoggerFactory.getLogger(ConsoleUi.class);
  private final Scanner scanner;

  /**
   * Konstruktor.
   */
  public ConsoleUi() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * Üdvözlő üzenet megjelenítése.
   */
  public void udvozles() {
    System.out.println();
    System.out.println("╔═══════════════════════════════════════╗");
    System.out.println("║                                       ║");
    System.out.println("║      AMŐBA JÁTÉK - NxM TÁBLA          ║");
    System.out.println("║                                       ║");
    System.out.println("╚═══════════════════════════════════════╝");
    System.out.println();
    System.out.println("Játékszabály: 4 jelet egymás mellé kell rakni!");
    System.out.println("(vízszintesen, függőlegesen vagy átlósan)");
    System.out.println();
  }

  /**
   * Játékos nevének bekérése.
   *
   * @return játékos neve
   */
  public String bekerNevet() {
    System.out.print("Add meg a neved: ");
    String nev = scanner.nextLine().trim();

    if (nev.isEmpty()) {
      nev = "Játékos";
    }

    logger.info("Játékos neve: {}", nev);
    return nev;
  }

  /**
   * Tábla méretének bekérése.
   *
   * @return tömb [0] = sorok, [1] = oszlopok
   */
  public int[] bekerTablaMeretet() {
    System.out.println();
    System.out.println("Tábla méretének beállítása:");

    int sorok = bekerSzamot("Sorok száma (5-25): ", 5, 25);
    int oszlopok = bekerSzamot("Oszlopok száma (5-25): ", 5, 25);

    logger.info("Tábla méret: {}x{}", sorok, oszlopok);
    return new int[] {sorok, oszlopok};
  }

  /**
   * Számot kér be megadott tartományban.
   */
  private int bekerSzamot(String uzenet, int min, int max) {
    while (true) {
      System.out.print(uzenet);
      try {
        int szam = Integer.parseInt(scanner.nextLine().trim());
        if (szam >= min && szam <= max) {
          return szam;
        }
        System.out.println("A számnak " + min + " és " + max + " között kell lennie!");
      } catch (NumberFormatException e) {
        System.out.println("Érvénytelen szám! Próbáld újra.");
      }
    }
  }

  /**
   * Tábla megjelenítése.
   *
   * @param tabla a megjelenítendő tábla
   */
  public void kiirTabla(Board tabla) {
    System.out.println();
    System.out.println(tabla.toString());
  }

  /**
   * Játékos lépésének bekérése.
   *
   * @param jatekos aktuális játékos
   * @return választott pozíció
   */
  public Position bekerLepes(Player jatekos) {
    System.out.println();
    System.out.println(jatekos.getNev() + " következik (" + jatekos.getSzimbolum() + ")");

    while (true) {
      System.out.print("Add meg a lépést (pl. A5, B3): ");
      String input = scanner.nextLine().trim().toUpperCase();

      if (input.length() < 2) {
        System.out.println("Túl rövid bemenet! Próbáld újra.");
        continue;
      }

      try {
        char oszlopBetu = input.charAt(0);
        String sorSzoveg = input.substring(1);

        int oszlop = oszlopBetu - 'A';
        int sor = Integer.parseInt(sorSzoveg) - 1;

        Position pozicio = new Position(sor, oszlop);
        logger.info("Játékos lépése: {}", pozicio);
        return pozicio;

      } catch (Exception e) {
        System.out.println("Érvénytelen formátum! Használj pl. A5, B3 formát.");
      }
    }
  }

  /**
   * Hibaüzenet megjelenítése.
   *
   * @param uzenet hibaüzenet
   */
  public void hibaUzenet(String uzenet) {
    System.out.println();
    System.out.println("❌ HIBA: " + uzenet);
    System.out.println();
  }

  /**
   * Győztes kihirdetése.
   *
   * @param gyoztes a győztes játékos
   */
  public void gyoztesKihirdetese(Player gyoztes) {
    System.out.println();
    System.out.println("╔═══════════════════════════════════════╗");
    System.out.println("║                                       ║");
    System.out.println("║            GRATULÁLUNK!               ║");
    System.out.println("║                                       ║");
    System.out.println("║   " + String.format("%-30s", gyoztes.getNev() + " NYERT!") + "    ║");
    System.out.println("║                                       ║");
    System.out.println("╚═══════════════════════════════════════╝");
    System.out.println();
  }

  /**
   * Döntetlen kiírása.
   */
  public void dontetlen() {
    System.out.println();
    System.out.println("╔═══════════════════════════════════════╗");
    System.out.println("║                                       ║");
    System.out.println("║            DÖNTETLEN!                 ║");
    System.out.println("║                                       ║");
    System.out.println("╚═══════════════════════════════════════╝");
    System.out.println();
  }

  /**
   * Menü megjelenítése.
   *
   * @return választott menüpont (1-4)
   */
  public int menu() {
    System.out.println();
    System.out.println("═══════════════════════════════════════");
    System.out.println("              FŐMENÜ                   ");
    System.out.println("═══════════════════════════════════════");
    System.out.println();
    System.out.println("  1. Új játék indítása");
    System.out.println("  2. Játék betöltése fájlból");
    System.out.println("  3. High Score megtekintése");
    System.out.println("  4. Kilépés");
    System.out.println();

    return bekerSzamot("Válassz (1-4): ", 1, 4);
  }

  /**
   * Fájlnév bekérése.
   *
   * @param uzenet megjelenítendő üzenet
   * @return fájlnév
   */
  public String bekerFajlnevet(String uzenet) {
    System.out.print(uzenet);
    String fajlnev = scanner.nextLine().trim();

    if (fajlnev.isEmpty()) {
      fajlnev = "jatek.txt";
    }

    return fajlnev;
  }

  /**
   * Siker üzenet megjelenítése.
   *
   * @param uzenet az üzenet
   */
  public void sikerUzenet(String uzenet) {
    System.out.println();
    System.out.println("✓ " + uzenet);
    System.out.println();
  }

  /**
   * Gép lépésének kiírása.
   *
   * @param pozicio a gép lépése
   */
  public void gepLepese(Position pozicio) {
    System.out.println();
    System.out.println("Gép lépett: " + positionToString(pozicio));
    System.out.println();
  }

  /**
   * Pozíció stringgé alakítása (pl. A5).
   */
  private String positionToString(Position pozicio) {
    char oszlop = (char) ('A' + pozicio.getOszlop());
    int sor = pozicio.getSor() + 1;
    return "" + oszlop + sor;
  }

  /**
   * Várakozás (Enter leütésére).
   */
  public void varjEnter() {
    System.out.print("Nyomj Enter-t a folytatáshoz...");
    scanner.nextLine();
  }

  /**
   * Scanner lezárása.
   */
  public void close() {
    scanner.close();
  }

  /**
   * High score lista megjelenítése.
   *
   * @param lista a high score lista
   */
  public void megjelenitHighScore(List<HighScore> lista) {
    System.out.println();
    System.out.println("══════ High Scores ══════");
    if (lista == null || lista.isEmpty()) {
      System.out.println("Nincs még bejegyzés.");
    } else {
      int i = 1;
      for (HighScore hs : lista) {
        System.out.printf("%2d) %s%n", i++, hs.toString());
      }
    }
    System.out.println("══════════════════════════");
    System.out.println();
  }
}