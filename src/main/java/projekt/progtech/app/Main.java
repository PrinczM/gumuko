package projekt.progtech.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projekt.progtech.ai.AiPlayer;
import projekt.progtech.engine.GameEngine;
import projekt.progtech.model.Board;
import projekt.progtech.model.HighScore;
import projekt.progtech.model.Player;
import projekt.progtech.model.Position;
import projekt.progtech.persistence.FileManager;
import projekt.progtech.persistence.HighScoreRepository;
import projekt.progtech.ui.ConsoleUi;
import projekt.progtech.ui.FelhasznaloiBevitel;

import java.io.File;

/**
 * Az alkalmazás belépési pontja.
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private static final String ALAP_FAJLNEV = "jatek.xml";
  private static final String HS_DB_PATH = "./highscore";
  private static final String SAVE_DIR = "saves";

  /**
   * Főprogram indítása.
   *
   * @param args parancssori argumentumok
   */
  public static void main(String[] args) {
    logger.info("Amőba játék elindult");

    // Mentések könyvtára biztosan létezzen
    new File(SAVE_DIR).mkdirs();

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

    // Mentési fájlnév kiválasztása: saves/jatek.xml, ha foglalt akkor jatek2.xml stb.
    String mentesiFajl = kovetkezoMentettFajl();

    jatekMenet(ui, engine, ai, new FileManager(), repo, mentesiFajl);
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

    // Ha relatív/egyszerű név, próbáljuk a saves könyvtárban
    File f = new File(fajlnev);
    if (!f.isAbsolute()) {
      f = new File(SAVE_DIR, fajlnev);
    }

    Board tabla = fileManager.betolt(f.getPath());
    if (tabla == null) {
      ui.hibaUzenet("Nem sikerült betölteni a fájlt!");
      ui.varjEnter();
      return;
    }

    ui.sikerUzenet("Játék sikeresen betöltve!");
    String nev = ui.bekerNevet();

    GameEngine engine = new GameEngine(tabla, nev);
    // Betöltött játék további mentése ugyanarra a fájlra történik
    jatekMenet(ui, engine, ai, fileManager, repo, f.getPath());
  }

  /**
   * Játék menetének kezelése automatikus mentéssel minden lépés után.
   */
  private static void jatekMenet(ConsoleUi ui, GameEngine engine,
                                 AiPlayer ai, FileManager fileManager,
                                 HighScoreRepository repo, String mentesiFajl) {
    logger.info("Játék menet kezdődik (auto-mentés: {})", mentesiFajl);

    while (!engine.isJatekVege()) {
      ui.kiirTabla(engine.getTabla());

      Player aktualis = engine.getAktualisJatekos();

      if (aktualis.isX()) {
        // Humán játékos: csak lépés megadása (nincs külön mentés parancs)
        boolean sikeresLepes = false;
        while (!sikeresLepes) {
          Position lepes = ui.bekerLepes(aktualis);
          sikeresLepes = engine.lepes(lepes);
          if (!sikeresLepes) {
            ui.hibaUzenet("Érvénytelen lépés! Próbáld újra.");
          }
        }
        // Sikeres humán lépés után automatikus mentés
        fileManager.ment(engine.getTabla(), mentesiFajl);
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
        // AI lépés után automatikus mentés
        fileManager.ment(engine.getTabla(), mentesiFajl);
      }
    }

    // Játék vége
    ui.kiirTabla(engine.getTabla());

    if (engine.getGyoztes() != null) {
      ui.gyoztesKihirdetese(engine.getGyoztes());
    } else {
      ui.dontetlen();
    }

    // EREDMÉNY mentése HighScore-ba (automatikus)
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

  /**
   * Kiválasztja a következő elérhető mentési fájlnevet a saves mappában:
   * jatek.xml, majd jatek2.xml, jatek3.xml, ...
   */
  private static String kovetkezoMentettFajl() {
    File dir = new File(SAVE_DIR);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File elso = new File(dir, ALAP_FAJLNEV);
    if (!elso.exists()) {
      return elso.getPath();
    }
    String alapNev = ALAP_FAJLNEV.replace(".xml", "");
    int i = 2;
    while (true) {
      File kov = new File(dir, alapNev + i + ".xml");
      if (!kov.exists()) {
        return kov.getPath();
      }
      i++;
    }
  }
}