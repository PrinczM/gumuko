package projekt.progtech;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import projekt.progtech.model.Position;

/**
 * Position osztály unit tesztjei.
 */
class PositionTest {

  @Test
  void konstruktorJolBeallitjaAzErtekeket() {
    // Given - Adottak
    int sor = 5;
    int oszlop = 3;

    // When - Amikor
    Position pozicio = new Position(sor, oszlop);

    // Then - Akkor
    assertEquals(sor, pozicio.getSor());
    assertEquals(oszlop, pozicio.getOszlop());
  }

  @Test
  void equalsIgazatAdVisszaHaKetPozicioMegegyezik() {
    // Given
    Position poz1 = new Position(2, 3);
    Position poz2 = new Position(2, 3);

    // When & Then
    assertEquals(poz1, poz2);
  }

  @Test
  void equalsHamisatAdVisszaHaKetPozicioKulonbozo() {
    // Given
    Position poz1 = new Position(2, 3);
    Position poz2 = new Position(2, 4);

    // When & Then
    assertNotEquals(poz1, poz2);
  }

  @Test
  void toStringMukodik() {
    // Given
    Position poz = new Position(1, 2);

    // When
    String eredmeny = poz.toString();

    // Then
    assertTrue(eredmeny.contains("1"));
    assertTrue(eredmeny.contains("2"));
  }
  @Test
  void equalsHamisatAdVisszaHaNullal() {
    // Given
    Position poz = new Position(2, 3);

    // When & Then
    assertNotEquals(null, poz);
  }

  @Test
  void equalsHamisatAdVisszaMasTipussal() {
    // Given
    Position poz = new Position(2, 3);
    String masTipus = "nem Position";

    // When & Then
    assertNotEquals(masTipus, poz);
  }

  @Test
  void hashCodeMegegyezikHaAKetPozicioMegegyezik() {
    // Given
    Position poz1 = new Position(2, 3);
    Position poz2 = new Position(2, 3);

    // When & Then
    assertEquals(poz1.hashCode(), poz2.hashCode());
  }
}