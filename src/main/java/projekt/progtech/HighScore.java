package projekt.progtech;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * High score bejegyzés modell.
 */
public class HighScore {

  /**
   * Eredmény típusa.
   */
  public enum Eredmeny { WIN, LOSS, DRAW }

  private final String jatekosNev;
  private final Eredmeny eredmeny;
  private final int sorok;
  private final int oszlopok;
  private final int lepesekSzama;
  private final LocalDateTime idopont;

  /**
   * HighScore konstruktor.
   *
   * @param jatekosNev játékos neve
   * @param eredmeny eredmény (WIN/LOSS/DRAW)
   * @param sorok tábla sorok száma
   * @param oszlopok tábla oszlopok száma
   * @param lepesekSzama végrehajtott lépések száma
   * @param idopont bejegyzés időpontja
   */
  public HighScore(String jatekosNev, Eredmeny eredmeny,
                   int sorok, int oszlopok,
                   int lepesekSzama, LocalDateTime idopont) {
    this.jatekosNev = jatekosNev;
    this.eredmeny = eredmeny;
    this.sorok = sorok;
    this.oszlopok = oszlopok;
    this.lepesekSzama = lepesekSzama;
    this.idopont = idopont;
  }

  public String getJatekosNev() {
    return jatekosNev;
  }

  public Eredmeny getEredmeny() {
    return eredmeny;
  }

  public int getSorok() {
    return sorok;
  }

  public int getOszlopok() {
    return oszlopok;
  }

  public int getLepesekSzama() {
    return lepesekSzama;
  }

  public LocalDateTime getIdopont() {
    return idopont;
  }

  @Override
  public String toString() {
    return jatekosNev + ": " + eredmeny
        + " on " + sorok + "x" + oszlopok
        + ", moves=" + lepesekSzama
        + ", at=" + idopont;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HighScore highScore = (HighScore) o;
    return sorok == highScore.sorok
        && oszlopok == highScore.oszlopok
        && lepesekSzama == highScore.lepesekSzama
        && Objects.equals(jatekosNev, highScore.jatekosNev)
        && eredmeny == highScore.eredmeny
        && Objects.equals(idopont, highScore.idopont);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jatekosNev, eredmeny, sorok, oszlopok, lepesekSzama, idopont);
  }
}
