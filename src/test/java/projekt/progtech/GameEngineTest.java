package projekt.progtech;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameEngineTest {

  private GameEngine engine;
  private Board tabla;

  @BeforeEach
  void setUp() {
    tabla = new Board(10, 10);
    engine = new GameEngine(tabla, "Teszt Játékos");
  }

  @Test
  void ujJatekbanHumanJatekosKezd() {
    // When & Then
    assertEquals('X', engine.getAktualisJatekos().getSzimbolum());
    assertFalse(engine.isJatekVege());
  }

  @Test
  void elsoLepesKozepsoMezonSikeres() {
    // Given
    Position kozep = new Position(5, 5);

    // When
    boolean siker = engine.lepes(kozep);

    // Then
    assertTrue(siker);
    assertEquals('X', tabla.getMezo(kozep));
  }

  @Test
  void elsoLepesNemKozepsoMezonSikertelen() {
    // Given
    Position sarok = new Position(0, 0);

    // When
    boolean siker = engine.lepes(sarok);

    // Then
    assertFalse(siker);
    assertTrue(tabla.isUres(sarok));
  }

  @Test
  void masodikLepesErintkezoMezonSikeres() {
    // Given
    engine.lepes(new Position(5, 5)); // Első lépés

    // When - Szomszédos mező
    boolean siker = engine.lepes(new Position(5, 6));

    // Then
    assertTrue(siker);
  }

  @Test
  void masodikLepesNemErintkezoMezonSikertelen() {
    // Given
    engine.lepes(new Position(5, 5)); // Első lépés

    // When - Távoli mező
    boolean siker = engine.lepes(new Position(0, 0));

    // Then
    assertFalse(siker);
  }

  @Test
  void foglaltMezoNemLehetLerakni() {
    // Given
    Position poz = new Position(5, 5);
    engine.lepes(poz);

    // When - Ugyanoda próbálunk lépni
    boolean siker = engine.lepes(poz);

    // Then
    assertFalse(siker);
  }

  @Test
  void negyEgymasMellettGyozelmeSzamit() {
    // Update: most 5 egymás mellett szükséges a győzelemhez
    engine.lepes(new Position(5, 5)); // X
    engine.lepes(new Position(6, 5)); // O
    engine.lepes(new Position(5, 4)); // X
    engine.lepes(new Position(6, 6)); // O
    engine.lepes(new Position(5, 3)); // X
    engine.lepes(new Position(7, 6)); // O
    engine.lepes(new Position(5, 6)); // X
    engine.lepes(new Position(7, 5)); // O
    engine.lepes(new Position(5, 2)); // X - 5 vízszintesen: 2..6

    assertTrue(engine.isJatekVege());
    assertNotNull(engine.getGyoztes());
    assertEquals('X', engine.getGyoztes().getSzimbolum());
  }

  @Test
  void jatekVegeUtanNemLehetTovabbLepni() {
    // Given - Előbb hozzunk létre 5 egymás melletti X-et
    engine.lepes(new Position(5, 5)); // X
    engine.lepes(new Position(6, 5)); // O
    engine.lepes(new Position(5, 4)); // X
    engine.lepes(new Position(6, 6)); // O
    engine.lepes(new Position(5, 3)); // X
    engine.lepes(new Position(7, 6)); // O
    engine.lepes(new Position(5, 6)); // X
    engine.lepes(new Position(7, 5)); // O
    engine.lepes(new Position(5, 2)); // X - 5 vízszintesen kész (2..6)

    assertTrue(engine.isJatekVege());

    // When - Még egy lépést próbálunk
    boolean siker = engine.lepes(new Position(4, 5));

    // Then
    assertFalse(siker);
  }
  @Test
  void atloBalraLeNyeresFelismeri() {
    // Update: 5 átlósan balra le
    engine.lepes(new Position(5, 5)); // X
    engine.lepes(new Position(6, 5)); // O
    engine.lepes(new Position(4, 6)); // X
    engine.lepes(new Position(7, 5)); // O
    engine.lepes(new Position(6, 4)); // X
    engine.lepes(new Position(8, 5)); // O
    engine.lepes(new Position(3, 7)); // X
    engine.lepes(new Position(2, 5)); // O
    engine.lepes(new Position(7, 3)); // X

    assertFalse(engine.isJatekVege());

    engine.lepes(new Position(2, 8)); // X - 5 átló: (7,3),(6,4),(5,5),(4,6),(3,7) már megvan, bővítés
    assertTrue(engine.isJatekVege());
    assertEquals('X', engine.getGyoztes().getSzimbolum());
  }

  @Test
  void tablaSzelereNemLehetLerakni() {
    // Given
    engine.lepes(new Position(5, 5)); // Első lépés

    // When - Tábla szélén kívül
    Position kint = new Position(-1, 5);
    boolean siker = engine.lepes(kint);

    // Then
    assertFalse(siker);
  }

  @Test
  void jatekosValtasHelyesenMukodik() {
    // Given & When
    Player elsoJatekos = engine.getAktualisJatekos();
    engine.lepes(new Position(5, 5));
    Player masodikJatekos = engine.getAktualisJatekos();

    // Then
    assertNotEquals(elsoJatekos, masodikJatekos);
  }
  @Test
  void betoltottTablaEsetenNemKellKozepreLepni_EsXJonHaAzonosDarab() {
    // Given: betöltött állapotot szimulálunk (X és O darab egyenlő)
    Board t = new Board(10, 10);
    t.lerak(new Position(5, 5), 'X');
    t.lerak(new Position(5, 6), 'O');

    GameEngine e = new GameEngine(t, "Teszt Játékos");

    // When: X következik (azonos darab), lépünk nem középre, de érintkező helyre
    Position nemKozep = new Position(5, 4); // X mellé
    boolean siker = e.lepes(nemKozep);

    // Then
    assertTrue(siker);
    assertEquals('X', t.getMezo(nemKozep));
  }

  @Test
  void betoltottTablaEsetenOJonHaXDbEggyelTobb() {
    // Given: X-ből eggyel több, így O következik
    Board t = new Board(10, 10);
    t.lerak(new Position(5, 5), 'X');

    GameEngine e = new GameEngine(t, "Teszt Játékos");

    // When: O lép (AI játékos szimbóluma), nem középre, de érintkező helyre
    Position oPoz = new Position(5, 6); // X mellé
    boolean siker = e.lepes(oPoz);

    // Then
    assertTrue(siker);
    assertEquals('O', t.getMezo(oPoz));
  }
}

