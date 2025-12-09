package projekt.progtech.exception;

/**
 * Kivétel adatbázis műveletek során.
 */
public class AdatbazisException extends JatekException {

  /**
   * Konstruktor üzenettel.
   *
   * @param uzenet a hiba üzenete
   */
  public AdatbazisException(String uzenet) {
    super(uzenet);
  }

  /**
   * Konstruktor üzenettel és okkal.
   *
   * @param uzenet a hiba üzenete
   * @param ok     az eredeti kivétel
   */
  public AdatbazisException(String uzenet, Throwable ok) {
    super(uzenet, ok);
  }
}

