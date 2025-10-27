package projekt.progtech;

/**
 * A játéktáblát reprezentálja.
 */
public class Board {

  private final int sorokSzama;
  private final int oszlopokSzama;
  private final char[][] tabla;

  /**
   * Konstruktor - új üres tábla létrehozása.
   *
   * @param sorokSzama    sorok száma (N)
   * @param oszlopokSzama oszlopok száma (M)
   */
  public Board(int sorokSzama, int oszlopokSzama) {
    this.sorokSzama = sorokSzama;
    this.oszlopokSzama = oszlopokSzama;
    this.tabla = new char[sorokSzama][oszlopokSzama];

    // Tábla inicializálása üres mezőkkel
    for (int i = 0; i < sorokSzama; i++) {
      for (int j = 0; j < oszlopokSzama; j++) {
        tabla[i][j] = '.';
      }
    }
  }

  /**
   * Visszaadja a sorok számát.
   *
   * @return sorok száma
   */
  public int getSorokSzama() {
    return sorokSzama;
  }

  /**
   * Visszaadja az oszlopok számát.
   *
   * @return oszlopok száma
   */
  public int getOszlopokSzama() {
    return oszlopokSzama;
  }

  /**
   * Visszaadja, hogy mi van egy adott pozíción.
   *
   * @param pozicio a vizsgált pozíció
   * @return karakter ('.', 'X', vagy 'O')
   */
  public char getMezo(Position pozicio) {
    return tabla[pozicio.getSor()][pozicio.getOszlop()];
  }

  /**
   * Ellenőrzi, hogy egy pozíció üres-e.
   *
   * @param pozicio a vizsgált pozíció
   * @return true, ha üres
   */
  public boolean isUres(Position pozicio) {
    return getMezo(pozicio) == '.';
  }

  /**
   * Ellenőrzi, hogy egy pozíció érvényes-e (a táblán belül van).
   *
   * @param pozicio a vizsgált pozíció
   * @return true, ha érvényes
   */
  public boolean isErvenyesPozicio(Position pozicio) {
    int sor = pozicio.getSor();
    int oszlop = pozicio.getOszlop();
    return sor >= 0 && sor < sorokSzama && oszlop >= 0 && oszlop < oszlopokSzama;
  }

  /**
   * Lerak egy jelet a táblára.
   *
   * @param pozicio   ahova rakjuk
   * @param szimbolum amit lerakunk (X vagy O)
   * @return true, ha sikeres volt
   */
  public boolean lerak(Position pozicio, char szimbolum) {
    if (!isErvenyesPozicio(pozicio)) {
      return false; // Nem érvényes pozíció
    }
    if (!isUres(pozicio)) {
      return false; // Már foglalt
    }

    tabla[pozicio.getSor()][pozicio.getOszlop()] = szimbolum;
    return true;
  }

  /**
   * Tábla szöveges megjelenítése.
   *
   * @return a tábla string formában
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    // Fejléc (oszlop számok)
    sb.append("   ");
    for (int j = 0; j < oszlopokSzama; j++) {
      sb.append((char) ('A' + j)).append(" ");
    }
    sb.append("\n");

    // Sorok
    for (int i = 0; i < sorokSzama; i++) {
      sb.append(String.format("%2d ", i + 1)); // Sor szám
      for (int j = 0; j < oszlopokSzama; j++) {
        sb.append(tabla[i][j]).append(" ");
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  /**
   * Ellenőrzi, hogy van-e nyerő a táblán (5 egymás mellett).
   *
   * @param szimbolum melyik jelet keressük (X vagy O)
   * @return true, ha van 5 egymás mellett
   */
  public boolean vanNyero(char szimbolum) {
    // Végigmegyünk minden mezőn
    for (int sor = 0; sor < sorokSzama; sor++) {
      for (int oszlop = 0; oszlop < oszlopokSzama; oszlop++) {
        if (tabla[sor][oszlop] == szimbolum) {
          // Nézzük meg, hogy innen indulva van-e 5 egymás mellett
          if (vizsgalIrany(sor, oszlop, 0, 1, szimbolum)) {
            return true; // Vízszintes
          }
          if (vizsgalIrany(sor, oszlop, 1, 0, szimbolum)) {
            return true; // Függőleges
          }
          if (vizsgalIrany(sor, oszlop, 1, 1, szimbolum)) {
            return true; // Átló (jobbra le)
          }
          if (vizsgalIrany(sor, oszlop, 1, -1, szimbolum)) {
            return true; // Átló (balra le)
          }
        }
      }
    }
    return false;
  }

  /**
   * Segédfüggvény - egy irányba néz 5-öt.
   *
   * @param kezdoSor    honnan indulunk
   * @param kezdoOszlop honnan indulunk
   * @param sorIrany    sor irányú lépés (-1, 0, vagy 1)
   * @param oszlopIrany oszlop irányú lépés (-1, 0, vagy 1)
   * @param szimbolum   mit keresünk
   * @return true, ha van 5 egymás mellett
   */
  private boolean vizsgalIrany(int kezdoSor, int kezdoOszlop,
                               int sorIrany, int oszlopIrany,
                               char szimbolum) {
    int nyeroHossz = 5;
    int db = 0;
    int sor = kezdoSor;
    int oszlop = kezdoOszlop;

    // Nézzünk 5-öt ebben az irányban
    for (int i = 0; i < nyeroHossz; i++) {
      // Kilógunk a tábláról?
      if (sor < 0 || sor >= sorokSzama || oszlop < 0 || oszlop >= oszlopokSzama) {
        return false;
      }

      // Ez a mező jó?
      if (tabla[sor][oszlop] == szimbolum) {
        db++;
      } else {
        return false;
      }

      // Lépünk egyet az irányba
      sor += sorIrany;
      oszlop += oszlopIrany;
    }

    return db == nyeroHossz;
  }

  /**
   * Ellenőrzi, hogy tele van-e a tábla (döntetlen).
   *
   * @return true, ha nincs több üres mező
   */
  public boolean isTele() {
    for (int i = 0; i < sorokSzama; i++) {
      for (int j = 0; j < oszlopokSzama; j++) {
        if (tabla[i][j] == '.') {
          return false; // Van még üres
        }
      }
    }
    return true; // Minden foglalt
  }
}