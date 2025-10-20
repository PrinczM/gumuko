package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    // Given - Lerakunk 4 X-et egymás mellé
    engine.lepes(new Position(5, 5)); // X
    engine.lepes(new Position(6, 5)); // O (gép)
    engine.lepes(new Position(5, 4)); // X
    engine.lepes(new Position(6, 6)); // O
    engine.lepes(new Position(5, 3)); // X
    engine.lepes(new Position(7, 6)); // O
    engine.lepes(new Position(5, 6)); // X - GYŐZELEM!

    // Then
    assertTrue(engine.isJatekVege());
    assertNotNull(engine.getGyoztes());
    assertEquals('X', engine.getGyoztes().getSzimbolum());
  }

  @Test
  void jatekVegeUtanNemLehetTovabbLepni() {
    // Given - Játék véget ér
    engine.lepes(new Position(5, 5)); // X
    engine.lepes(new Position(6, 5)); // O
    engine.lepes(new Position(5, 4)); // X
    engine.lepes(new Position(6, 6)); // O
    engine.lepes(new Position(5, 3)); // X
    engine.lepes(new Position(7, 6)); // O
    engine.lepes(new Position(5, 6)); // X - GYŐZELEM!

    // When - Még egy lépést próbálunk
    boolean siker = engine.lepes(new Position(4, 5));

    // Then
    assertFalse(siker);
  }
}