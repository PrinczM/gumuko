package projekt.progtech.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projekt.progtech.model.Board;
import projekt.progtech.model.Position;

/**
 * Gépi ellenfél (AI) osztály.
 * Egyszerű random lépéseket generál.
 */
public class AiPlayer {

  private static final Logger logger = LoggerFactory.getLogger(AiPlayer.class);
  private final Random random;

  /**
   * Konstruktor.
   */
  public AiPlayer() {
    this.random = new Random();
  }

  /**
   * Konstruktor teszteléshez (fix seed).
   *
   * @param seed random seed
   */
  public AiPlayer(long seed) {
    this.random = new Random(seed);
  }

  /**
   * Generál egy random lépést.
   *
   * @param tabla a játéktábla
   * @param elsoLepes ez-e az első lépés?
   * @return egy érvényes pozíció
   */
  public Position generalLepes(Board tabla, boolean elsoLepes) {
    List<Position> lehetsegesLepesek = getLehetsegesLepesek(tabla, elsoLepes);

    if (lehetsegesLepesek.isEmpty()) {
      logger.error("Nincs lehetséges lépés!");
      return null;
    }

    // Random választás
    int index = random.nextInt(lehetsegesLepesek.size());
    Position valasztott = lehetsegesLepesek.get(index);

    logger.info("AI választott pozíciót: {}", valasztott);
    return valasztott;
  }

  /**
   * Összegyűjti az összes lehetséges lépést.
   *
   * @param tabla játéktábla
   * @param elsoLepes ez-e az első lépés?
   * @return érvényes pozíciók listája
   */
  private List<Position> getLehetsegesLepesek(Board tabla, boolean elsoLepes) {
    List<Position> lepesek = new ArrayList<>();

    for (int sor = 0; sor < tabla.getSorokSzama(); sor++) {
      for (int oszlop = 0; oszlop < tabla.getOszlopokSzama(); oszlop++) {
        Position poz = new Position(sor, oszlop);

        if (!tabla.isUres(poz)) {
          continue; // Foglalt mező
        }

        if (elsoLepes) {
          // Első lépés: csak középső mezők
          if (isKozepso(poz, tabla)) {
            lepesek.add(poz);
          }
        } else {
          // Többi lépés: érintkezni kell
          if (erintkezik(poz, tabla)) {
            lepesek.add(poz);
          }
        }
      }
    }

    return lepesek;
  }

  /**
   * Ellenőrzi, hogy egy pozíció középső-e.
   *
   * @param pozicio vizsgált pozíció
   * @param tabla játéktábla
   * @return true, ha középső
   */
  private boolean isKozepso(Position pozicio, Board tabla) {
    int kozepSor = tabla.getSorokSzama() / 2;
    int kozepOszlop = tabla.getOszlopokSzama() / 2;

    int sor = pozicio.getSor();
    int oszlop = pozicio.getOszlop();

    return Math.abs(sor - kozepSor) <= 1 && Math.abs(oszlop - kozepOszlop) <= 1;
  }

  /**
   * Ellenőrzi, hogy egy pozíció érintkezik-e más jelekkel.
   *
   * @param pozicio vizsgált pozíció
   * @param tabla játéktábla
   * @return true, ha érintkezik
   */
  private boolean erintkezik(Position pozicio, Board tabla) {
    int sor = pozicio.getSor();
    int oszlop = pozicio.getOszlop();

    // 8 irány
    int[] sorIranyok = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] oszlopIranyok = {-1, 0, 1, -1, 1, -1, 0, 1};

    for (int i = 0; i < 8; i++) {
      int ujSor = sor + sorIranyok[i];
      int ujOszlop = oszlop + oszlopIranyok[i];
      Position szomszed = new Position(ujSor, ujOszlop);

      if (tabla.isErvenyesPozicio(szomszed) && !tabla.isUres(szomszed)) {
        return true;
      }
    }

    return false;
  }
}