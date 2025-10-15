package projekt.progtech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Az alkalmazás belépési pontja.
 */
public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  /**
   * Főprogram indítása.
   *
   * @param args parancssori argumentumok
   */
  public static void main(String[] args) {
    logger.info("Amőba játék elindult!");
    System.out.println("╔═══════════════════════════════╗");
    System.out.println("║   AMŐBA JÁTÉK - NxM TÁBLA     ║");
    System.out.println("╚═══════════════════════════════╝");
    System.out.println();
    System.out.println("Fejlesztés alatt...");
    System.out.println();
    logger.info("Alkalmazás sikeresen inicializálva");
  }
}