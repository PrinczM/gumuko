package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * ConsoleUI egyszerű tesztjei.
 * A konzol interakciót nehéz unit tesztekkel lefedni.
 */
class ConsoleUiTest {

  @Test
  void consoleUiLetrehozas() {
    // Given & When
    ConsoleUi ui = new ConsoleUi();

    // Then
    assertNotNull(ui);
  }

  @Test
  void tablaKiirasaNemDobKivetelt() {
    // Given
    ConsoleUi ui = new ConsoleUi();
    Board tabla = new Board(5, 5);

    // When & Then - nem dob kivételt
    assertDoesNotThrow(() -> ui.kiirTabla(tabla));
  }

  @Test
  void udvozlesNemDobKivetelt() {
    // Given
    ConsoleUi ui = new ConsoleUi();

    // When & Then
    assertDoesNotThrow(ui::udvozles);
  }
}