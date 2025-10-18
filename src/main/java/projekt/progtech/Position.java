package projekt.progtech;

import java.util.Objects;

/**
 * Egy pozíciót reprezentál a játéktáblán.
 * Immutable (nem változtatható) osztály.
 */
public class Position {

  private final int sor;
  private final int oszlop;

  /**
   * Konstruktor - új pozíció létrehozása.
   *
   * @param sor    a sor száma (0-tól indul)
   * @param oszlop az oszlop száma (0-tól indul)
   */
  public Position(int sor, int oszlop) {
    this.sor = sor;
    this.oszlop = oszlop;
  }

  /**
   * Visszaadja a sor számát.
   *
   * @return sor szám
   */
  public int getSor() {
    return sor;
  }

  /**
   * Visszaadja az oszlop számát.
   *
   * @return oszlop szám
   */
  public int getOszlop() {
    return oszlop;
  }

  /**
   * Ellenőrzi, hogy két pozíció egyenlő-e.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return sor == position.sor && oszlop == position.oszlop;
  }

  /**
   * Hash kód generálás (szükséges az equals mellett).
   */
  @Override
  public int hashCode() {
    return Objects.hash(sor, oszlop);
  }

  /**
   * Szöveges reprezentáció (pl. debuggoláshoz).
   */
  @Override
  public String toString() {
    return "(" + sor + "," + oszlop + ")";
  }
}