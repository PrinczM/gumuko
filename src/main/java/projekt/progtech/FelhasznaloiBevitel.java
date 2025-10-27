package projekt.progtech;

/**
 * Felhasználói bevitel modellje: vagy egy lépés pozíció, vagy mentési kérés.
 */
public class FelhasznaloiBevitel {

  private final boolean mentesKerese;
  private final Position pozicio;

  /**
   * Konstruktor.
   *
   * @param mentesKerese igaz, ha a felhasználó mentést kér
   * @param pozicio a választott pozíció (ha nem mentést kér)
   */
  public FelhasznaloiBevitel(boolean mentesKerese, Position pozicio) {
    this.mentesKerese = mentesKerese;
    this.pozicio = pozicio;
  }

  /**
   * Visszaadja, hogy a felhasználó mentést kért-e.
   */
  public boolean isMentesKerese() {
    return mentesKerese;
  }

  /**
   * Visszaadja a választott pozíciót (mentés esetén null).
   */
  public Position getPozicio() {
    return pozicio;
  }
}

