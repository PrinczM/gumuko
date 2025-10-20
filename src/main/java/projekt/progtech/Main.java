package projekt.progtech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Az alkalmazás belépési pontja.
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static final String ALAP_FAJLNEV = "jatek.txt";

  /**
   * Főprogram indítása.
   *
   * @param args parancssori argumentumok
   */
  public static void main(String[] args) {
    logger.info("Amőba játék elindult");

    ConsoleUi ui = new ConsoleUi();
    FileManager fileManager = new FileManager();
    AiPlayer ai = new AiPlayer();

    ui.udvozles();

    boolean fut = true;
    while (fut) {
      int valasztas = ui.menu();

      switch (valasztas) {
        case 1:
          ujJatek(ui, ai);
          break;
        case 2:
          jatekBetoltese(ui, fileManager, ai);
          break;
        case 3:
          // High Score - később implementáljuk
          ui.hibaUzenet("High Score még nem elérhető (2. védés)");
          ui.varjEnter();
          break;
        case 4:
          fut = false;
          System.out.println("Viszlát!");
          logger.info("Alkalmazás leállt");
          break;
        default:
          ui.hibaUzenet("Érvénytelen választás!");
      }
    }

    ui.close();
  }

  /**
   * Új játék indítása.
   */
  private static void ujJatek(ConsoleUi ui, AiPlayer ai) {
    String nev = ui.bekerNevet();
    int[] meretek = ui.bekerTablaMeretet();

    Board tabla = new Board(meretek[0], meretek[1]);
    GameEngine engine = new GameEngine(tabla, nev);

    jatekMenet(ui, engine, ai, new FileManager());
  }

  /**
   * Játék betöltése fájlból.
   */
  private static void jatekBetoltese(ConsoleUi ui, FileManager fileManager, AiPlayer ai) {
    String fajlnev = ui.bekerFajlnevet("Fájlnév (Enter = " + ALAP_FAJLNEV + "): ");

    Board tabla = fileManager.betolt(fajlnev);
    if (tabla == null) {
      ui.hibaUzenet("Nem sikerült betölteni a fájlt!");
      ui.varjEnter();
      return;
    }

    ui.sikerUzenet("Játék sikeresen betöltve!");
    String nev = ui.bekerNevet();

    GameEngine engine = new GameEngine(tabla, nev);
    jatekMenet(ui, engine, ai, fileManager);
  }

  /**
   * Játék menetének kezelése.
   */
  private static void jatekMenet(ConsoleUi ui, GameEngine engine,
                                 AiPlayer ai, FileManager fileManager) {
    logger.info("Játék menet kezdődik");

    while (!engine.isJatekVege()) {
      ui.kiirTabla(engine.getTabla());

      Player aktualis = engine.getAktualisJatekos();

      if (aktualis.isX()) {
        // Humán játékos
        boolean sikeresLepes = false;
        while (!sikeresLepes) {
          Position lepes = ui.bekerLepes(aktualis);

          sikeresLepes = engine.lepes(lepes);

          if (!sikeresLepes) {
            ui.hibaUzenet("Érvénytelen lépés! Próbáld újra.");
          }
        }
      } else {
        // AI játékos
        System.out.println();
        System.out.println("Gép gondolkodik...");

        try {
          Thread.sleep(1000); // Kicsit várunk
        } catch (InterruptedException e) {
          logger.error("Sleep interrupted", e);
        }

        // Meghatározzuk, hogy első lépés-e
        boolean elsoLepes = true;
        for (int i = 0; i < engine.getTabla().getSorokSzama(); i++) {
          for (int j = 0; j < engine.getTabla().getOszlopokSzama(); j++) {
            if (!engine.getTabla().isUres(new Position(i, j))) {
              elsoLepes = false;
              break;
            }
          }
          if (!elsoLepes) {
            break;
          }
        }

        Position gepLepes = ai.generalLepes(engine.getTabla(), elsoLepes);
        engine.lepes(gepLepes);
        ui.gepLepese(gepLepes);
      }

      // Játék mentése minden lépés után (opcionális)
      // fileManager.ment(engine.getTabla(), "auto_save.txt");
    }

    // Játék vége
    ui.kiirTabla(engine.getTabla());

    if (engine.getGyoztes() != null) {
      ui.gyoztesKihirdetese(engine.getGyoztes());
    } else {
      ui.dontetlen();
    }

    // Mentés felajánlása
    System.out.println();
    System.out.print("Szeretnéd menteni a játékot? (i/n): ");
    java.util.Scanner sc = new java.util.Scanner(System.in);
    String valasz = sc.nextLine().trim().toLowerCase();

    if (valasz.equals("i") || valasz.equals("igen")) {
      String fajlnev = ui.bekerFajlnevet("Fájlnév (Enter = " + ALAP_FAJLNEV + "): ");
      if (fileManager.ment(engine.getTabla(), fajlnev)) {
        ui.sikerUzenet("Játék sikeresen mentve!");
      } else {
        ui.hibaUzenet("Mentés sikertelen!");
      }
    }

    ui.varjEnter();
    logger.info("Játék menet vége");
  }
}