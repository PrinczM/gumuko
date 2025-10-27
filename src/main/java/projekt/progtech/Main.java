package projekt.progtech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Az alkalmazás belépési pontja.
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static final String ALAP_FAJLNEV = "jatek.xml";
  private static final String HS_DB_PATH = "./highscore";

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
    HighScoreRepository repo = new HighScoreRepository(HS_DB_PATH);

    ui.udvozles();

    boolean fut = true;
    while (fut) {
      int valasztas = ui.menu();

      switch (valasztas) {
        case 1:
          ujJatek(ui, ai, repo);
          break;
        case 2:
          jatekBetoltese(ui, fileManager, ai, repo);
          break;
        case 3:
          ui.megjelenitHighScore(repo.lekerTop(20));
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
  private static void ujJatek(ConsoleUi ui, AiPlayer ai, HighScoreRepository repo) {
    String nev = ui.bekerNevet();
    int[] meretek = ui.bekerTablaMeretet();

    Board tabla = new Board(meretek[0], meretek[1]);
    GameEngine engine = new GameEngine(tabla, nev);

    jatekMenet(ui, engine, ai, new FileManager(), repo);
  }

  /**
   * Játék betöltése fájlból.
   */
  private static void jatekBetoltese(
      ConsoleUi ui,
      FileManager fileManager,
      AiPlayer ai,
      HighScoreRepository repo) {
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
    jatekMenet(ui, engine, ai, fileManager, repo);
  }

  /**
   * Játék menetének kezelése.
   */
  private static void jatekMenet(ConsoleUi ui, GameEngine engine,
                                 AiPlayer ai, FileManager fileManager, HighScoreRepository repo) {
    logger.info("Játék menet kezdődik");

    while (!engine.isJatekVege()) {
      ui.kiirTabla(engine.getTabla());

      Player aktualis = engine.getAktualisJatekos();

      if (aktualis.isX()) {
        // Humán játékos: lépés E5 formátumban, vagy 'save' a mentéshez
        FelhasznaloiBevitel bevitel = ui.bekerLepesVagyMentes(aktualis);
        if (bevitel.isMentesKerese()) {
          String fn = ui.bekerFajlnevet("Fájlnév (Enter = jatek.xml): ");
          if (fileManager.ment(engine.getTabla(), fn)) {
            ui.sikerUzenet("Játék sikeresen mentve!");
          } else {
            ui.hibaUzenet("Mentés sikertelen!");
          }
          // Mentés után kilépünk a játék menetéből a főmenübe
          return;
        }

        // Humán játékos lépése
        boolean sikeresLepes = false;
        while (!sikeresLepes) {
          Position lepes = bevitel.getPozicio();
          sikeresLepes = engine.lepes(lepes);

          if (!sikeresLepes) {
            ui.hibaUzenet("Érvénytelen lépés! Próbáld újra.");
            // új bevitel
            bevitel = ui.bekerLepesVagyMentes(aktualis);
            if (bevitel.isMentesKerese()) {
              String fn2 = ui.bekerFajlnevet("Fájlnév (Enter = jatek.xml): ");
              if (fileManager.ment(engine.getTabla(), fn2)) {
                ui.sikerUzenet("Játék sikeresen mentve!");
              } else {
                ui.hibaUzenet("Mentés sikertelen!");
              }
              return;
            }
          }
        }
      } else {
        // AI játékos
        System.out.println();
        System.out.println("Gép gondolkodik...");

        try {
          // noinspection BusyWait
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
    }

    // Játék vége
    ui.kiirTabla(engine.getTabla());

    if (engine.getGyoztes() != null) {
      ui.gyoztesKihirdetese(engine.getGyoztes());
    } else {
      ui.dontetlen();
    }

    // EREDMÉNY mentése HighScore-ba (automatikus) – fájlmentést itt nem ajánlunk
    HighScore.Eredmeny eredmeny = (engine.getGyoztes() == null)
        ? HighScore.Eredmeny.DRAW
        : (engine.getGyoztes().isX() ? HighScore.Eredmeny.WIN : HighScore.Eredmeny.LOSS);
    repo.ment(new HighScore(
        engine.getHumanJatekos().getNev(),
        eredmeny,
        engine.getTabla().getSorokSzama(),
        engine.getTabla().getOszlopokSzama(),
        engine.getLepesekSzama(),
        java.time.LocalDateTime.now()
    ));


    ui.varjEnter();
    logger.info("Játék menet vége");
  }
}