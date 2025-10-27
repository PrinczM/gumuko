package projekt.progtech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A játék logikáját kezelő osztály.
 */
public class GameEngine {

  private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

  private final Board tabla;
  private final Player humanJatekos;
  private final Player gepJatekos;
  private Player aktualisJatekos;
  private boolean jatekVege;
  private Player gyoztes;
  private boolean elsoLepes;
  private int lepesekSzama;

  /**
   * Konstruktor.
   *
   * @param tabla a játéktábla
   * @param humanNev a humán játékos neve
   */
  public GameEngine(Board tabla, String humanNev) {
    this.tabla = tabla;
    this.humanJatekos = new Player(humanNev, 'X');
    this.gepJatekos = new Player("Gép", 'O');
    this.aktualisJatekos = humanJatekos; // alapértelmezetten humán kezd
    this.jatekVege = false;
    this.gyoztes = null;
    this.elsoLepes = true;
    this.lepesekSzama = 0;

    // Inicializáljuk az állapotot a táblán lévő jelek alapján (betöltött játék esetén fontos)
    initAllapotTablaAlapjan();
  }

  /**
   * Visszaadja az aktuális játékost.
   *
   * @return aktuális játékos
   */
  public Player getAktualisJatekos() {
    return aktualisJatekos;
  }

  /**
   * Visszaadja a humán játékost.
   *
   * @return humán játékos
   */
  public Player getHumanJatekos() {
    return humanJatekos;
  }

  /**
   * Visszaadja a gépi játékost.
   *
   * @return gépi játékos
   */
  @SuppressWarnings("unused")
  public Player getGepJatekos() {
    return gepJatekos;
  }

  /**
   * Visszaadja a táblát.
   *
   * @return játéktábla
   */
  public Board getTabla() {
    return tabla;
  }

  /**
   * Játék vége van-e?.
   *
   * @return true, ha vége
   */
  public boolean isJatekVege() {
    return jatekVege;
  }

  /**
   * Visszaadja a győztest.
   *
   * @return győztes játékos, vagy null ha döntetlen/nincs vége
   */
  public Player getGyoztes() {
    return gyoztes;
  }

  /**
   * Visszaadja a lépések számát.
   *
   * @return lépések száma
   */
  public int getLepesekSzama() {
    return lepesekSzama;
  }

  /**
   * Ellenőrzi, hogy egy pozíció a tábla közepe-e (első lépéshez).
   *
   * @param pozicio vizsgált pozíció
   * @return true, ha középen van
   */
  private boolean isKozepso(Position pozicio) {
    int kozepSor = tabla.getSorokSzama() / 2;
    int kozepOszlop = tabla.getOszlopokSzama() / 2;

    // A középső pozíció és a körülötte lévők is jók
    int sor = pozicio.getSor();
    int oszlop = pozicio.getOszlop();

    return Math.abs(sor - kozepSor) <= 1 && Math.abs(oszlop - kozepOszlop) <= 1;
  }

  /**
   * Ellenőrzi, hogy egy pozíció érintkezik-e már lerakott jelekkel.
   *
   * @param pozicio vizsgált pozíció
   * @return true, ha érintkezik (vagy első lépés)
   */
  private boolean erintkezik(Position pozicio) {
    if (elsoLepes) {
      return true; // Első lépésnél nem kell érintkezés
    }

    int sor = pozicio.getSor();
    int oszlop = pozicio.getOszlop();

    // 8 irány (körben a pozíció körül)
    int[] sorIranyok = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] oszlopIranyok = {-1, 0, 1, -1, 1, -1, 0, 1};

    for (int i = 0; i < 8; i++) {
      int ujSor = sor + sorIranyok[i];
      int ujOszlop = oszlop + oszlopIranyok[i];
      Position szomszed = new Position(ujSor, ujOszlop);

      if (tabla.isErvenyesPozicio(szomszed) && !tabla.isUres(szomszed)) {
        return true; // Van szomszéd
      }
    }

    return false;
  }

  /**
   * Lépés végrehajtása.
   *
   * @param pozicio ahova lépünk
   * @return true, ha sikeres volt
   */
  public boolean lepes(Position pozicio) {
    if (jatekVege) {
      logger.warn("A játék már véget ért!");
      return false;
    }

    // Ellenőrzések
    if (!tabla.isErvenyesPozicio(pozicio)) {
      logger.warn("Érvénytelen pozíció: {}", pozicio);
      return false;
    }

    if (!tabla.isUres(pozicio)) {
      logger.warn("A pozíció már foglalt: {}", pozicio);
      return false;
    }

    if (elsoLepes && !isKozepso(pozicio)) {
      logger.warn("Az első lépésnek középen kell lennie!");
      return false;
    }

    if (!erintkezik(pozicio)) {
      logger.warn("A lépésnek érintkeznie kell más jelekkel!");
      return false;
    }

    // Lépés végrehajtása
    tabla.lerak(pozicio, aktualisJatekos.getSzimbolum());
    lepesekSzama++;
    logger.info("{} lépett: {}", aktualisJatekos.getNev(), pozicio);

    elsoLepes = false;

    // Győzelem ellenőrzés
    if (tabla.vanNyero(aktualisJatekos.getSzimbolum())) {
      jatekVege = true;
      gyoztes = aktualisJatekos;
      logger.info("{} nyert!", aktualisJatekos.getNev());
      return true;
    }

    // Döntetlen ellenőrzés
    if (tabla.isTele()) {
      jatekVege = true;
      logger.info("Döntetlen!");
      return true;
    }

    // Játékos váltás
    aktualisJatekos = (aktualisJatekos == humanJatekos) ? gepJatekos : humanJatekos;

    return true;
  }

  /**
   * Inicializálja az engine állapotát a tábla aktuális tartalma alapján.
   * Betöltött játék folytatásánál ez biztosítja a helyes szabályok alkalmazását.
   */
  private void initAllapotTablaAlapjan() {
    int xdarab = 0;
    int odarab = 0;
    boolean vanValami = false;

    for (int i = 0; i < tabla.getSorokSzama(); i++) {
      for (int j = 0; j < tabla.getOszlopokSzama(); j++) {
        char c = tabla.getMezo(new Position(i, j));
        if (c == 'X') {
          xdarab++;
          vanValami = true;
        } else if (c == 'O') {
          odarab++;
          vanValami = true;
        }
      }
    }

    // Első lépés akkor, ha teljesen üres a tábla
    this.elsoLepes = !vanValami;
    // Eddigi lépések száma a táblán lévő jelek száma
    this.lepesekSzama = xdarab + odarab;

    // Ki következik?
    if (!elsoLepes) {
      if (xdarab == odarab) {
        this.aktualisJatekos = humanJatekos; // X jön
      } else if (xdarab == odarab + 1) {
        this.aktualisJatekos = gepJatekos; // O jön
      } else {
        logger.warn(
            "Inkonzisztens tábla állapot (X={}, O={}). "
                + "X következik alapértelmezetten.",
            xdarab, odarab);
        this.aktualisJatekos = humanJatekos;
      }
    } else {
      // Üres tábla: X kezd
      this.aktualisJatekos = humanJatekos;
    }

    // Ha már nyerő állapotban van a tábla (betöltéskor), azonnal zárjuk le a játékot
    if (tabla.vanNyero('X')) {
      this.jatekVege = true;
      this.gyoztes = humanJatekos;
      logger.info("Betöltött tábla X győztes állapotban van. A játék véget ért.");
    } else if (tabla.vanNyero('O')) {
      this.jatekVege = true;
      this.gyoztes = gepJatekos;
      logger.info("Betöltött tábla O győztes állapotban van. A játék véget ért.");
    } else if (tabla.isTele()) {
      this.jatekVege = true;
      this.gyoztes = null;
      logger.info("Betöltött tábla tele van. Döntetlen.");
    }
  }
}