package projekt.progtech;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import projekt.progtech.model.Player;

class PlayerTest {

  @Test
  void konstruktorJolBeallitjaAzErtekeket() {
    // Given & When
    Player jatekos = new Player("Béla", 'X');

    // Then
    assertEquals("Béla", jatekos.getNev());
    assertEquals('X', jatekos.getSzimbolum());
  }

  @Test
  void isXigazatAdVisszaHaXjatekos() {
    // Given
    Player jatekos = new Player("Béla", 'X');

    // When & Then
    assertTrue(jatekos.isX());
    assertFalse(jatekos.isO());
  }

  @Test
  void isOigazatAdVisszaHaOjatekos() {
    // Given
    Player jatekos = new Player("Gép", 'O');

    // When & Then
    assertTrue(jatekos.isO());
    assertFalse(jatekos.isX());
  }

  @Test
  void equalsIgazatAdVisszaHaKetJatekosAzonos() {
    // Given
    Player j1 = new Player("Béla", 'X');
    Player j2 = new Player("Béla", 'X');

    // When & Then
    assertEquals(j1, j2);
  }
  @Test
  void equalsHamisatAdVissza_HaNullal() {
    // Given
    Player jatekos = new Player("Béla", 'X');

    // When & Then
    assertNotEquals(null, jatekos);
  }

  @Test
  void hashCodeMegegyezik_HaAKetJatekosAzonos() {
    // Given
    Player j1 = new Player("Béla", 'X');
    Player j2 = new Player("Béla", 'X');

    // When & Then
    assertEquals(j1.hashCode(), j2.hashCode());
  }
}