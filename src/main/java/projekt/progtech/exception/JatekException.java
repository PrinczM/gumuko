package projekt.progtech.exception;

/**
 * Alap kivétel osztály a játékhoz kapcsolódó hibákhoz.
 */
public class JatekException extends RuntimeException {

  /**
   * Konstruktor üzenettel.
   *
   * @param uzenet a hiba üzenete
   */
  public JatekException(String uzenet) {
    super(uzenet);
  }

  /**
   * Konstruktor üzenettel és okkal.
   *
   * @param uzenet a hiba üzenete
   * @param ok     az eredeti kivétel
   */
  public JatekException(String uzenet, Throwable ok) {
    super(uzenet, ok);
  }
}

