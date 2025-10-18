package projekt.progtech;

import java.util.Objects;

/**
 * Egy játékost reprezentál.
 */
public class Player {

  private final String nev;
  private final char szimbolum;

  /**
   * Konstruktor.
   *
   * @param nev       játékos neve
   * @param szimbolum játékos szimbóluma (X vagy O)
   */
  public Player(String nev, char szimbolum) {
    this.nev = nev;
    this.szimbolum = szimbolum;
  }

  /**
   * Visszaadja a játékos nevét.
   *
   * @return név
   */
  public String getNev() {
    return nev;
  }

  /**
   * Visszaadja a játékos szimbólumát.
   *
   * @return szimbólum
   */
  public char getSzimbolum() {
    return szimbolum;
  }

  /**
   * Ellenőrzi, hogy ez az X játékos-e.
   *
   * @return true, ha X
   */
  public boolean isX() {
    return szimbolum == 'X';
  }

  /**
   * Ellenőrzi, hogy ez az O játékos-e.
   *
   * @return true, ha O
   */
  public boolean isO() {
    return szimbolum == 'O';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return szimbolum == player.szimbolum && Objects.equals(nev, player.nev);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nev, szimbolum);
  }

  @Override
  public String toString() {
    return nev + " (" + szimbolum + ")";
  }
}