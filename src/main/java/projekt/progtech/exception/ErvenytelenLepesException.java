package projekt.progtech.exception;

/**
 * Kivétel érvénytelen lépés esetén.
 */
public class ErvenytelenLepesException extends JatekException {

  private final int sor;
  private final int oszlop;

  /**
   * Konstruktor.
   *
   * @param sor    a sor indexe
   * @param oszlop az oszlop indexe
   * @param uzenet a hiba üzenete
   */
  public ErvenytelenLepesException(int sor, int oszlop, String uzenet) {
    super(uzenet);
    this.sor = sor;
    this.oszlop = oszlop;
  }

  /**
   * Visszaadja a sor indexét.
   *
   * @return sor index
   */
  public int getSor() {
    return sor;
  }

  /**
   * Visszaadja az oszlop indexét.
   *
   * @return oszlop index
   */
  public int getOszlop() {
    return oszlop;
  }
}

