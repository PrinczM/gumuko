package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AiPlayerTest {

  private AiPlayer ai;
  private Board tabla;

  @BeforeEach
  void setUp() {
    ai = new AiPlayer(12345); // Fix seed teszteléshez
    tabla = new Board(10, 10);
  }

  @Test
  void elsoLepesKozepsoMezonVan() {
    // When
    Position lepes = ai.generalLepes(tabla, true);

    // Then
    assertNotNull(lepes);

    // Középső mezők valamelyike
    int kozepSor = 5;
    int kozepOszlop = 5;
    assertTrue(Math.abs(lepes.getSor() - kozepSor) <= 1);
    assertTrue(Math.abs(lepes.getOszlop() - kozepOszlop) <= 1);
  }

  @Test
  void masodikLepesErintkezoMezonVan() {
    // Given - Lerakunk egy jelet
    tabla.lerak(new Position(5, 5), 'X');

    // When
    Position lepes = ai.generalLepes(tabla, false);

    // Then
    assertNotNull(lepes);
    assertTrue(tabla.isUres(lepes));
  }

  @Test
  void mindenhovaNemGeneralFoglaltMezot() {
    // Given - Lerakunk pár jelet
    tabla.lerak(new Position(5, 5), 'X');
    tabla.lerak(new Position(5, 6), 'O');

    // When
    Position lepes = ai.generalLepes(tabla, false);

    // Then
    assertNotNull(lepes);
    assertTrue(tabla.isUres(lepes));
    assertNotEquals(new Position(5, 5), lepes);
    assertNotEquals(new Position(5, 6), lepes);
  }
}