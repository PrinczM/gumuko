package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import projekt.progtech.exception.AdatbazisException;
import projekt.progtech.exception.ErvenytelenLepesException;
import projekt.progtech.exception.JatekException;

/**
 * Exception osztályok tesztjei.
 */
class ExceptionTest {

  @Test
  void jatekExceptionUzenettelMukodik() {
    // Given
    String uzenet = "Teszt hiba";

    // When
    JatekException ex = new JatekException(uzenet);

    // Then
    assertEquals(uzenet, ex.getMessage());
  }

  @Test
  void jatekExceptionUzenettelEsOkkalMukodik() {
    // Given
    String uzenet = "Teszt hiba";
    Throwable ok = new RuntimeException("Eredeti hiba");

    // When
    JatekException ex = new JatekException(uzenet, ok);

    // Then
    assertEquals(uzenet, ex.getMessage());
    assertNotNull(ex.getCause());
    assertEquals("Eredeti hiba", ex.getCause().getMessage());
  }

  @Test
  void ervenytelenLepesExceptionTartalmazPoziciot() {
    // Given
    int sor = 5;
    int oszlop = 3;
    String uzenet = "Érvénytelen lépés";

    // When
    ErvenytelenLepesException ex = new ErvenytelenLepesException(sor, oszlop, uzenet);

    // Then
    assertEquals(sor, ex.getSor());
    assertEquals(oszlop, ex.getOszlop());
    assertEquals(uzenet, ex.getMessage());
  }

  @Test
  void adatbazisExceptionUzenettelMukodik() {
    // Given
    String uzenet = "Adatbázis hiba";

    // When
    AdatbazisException ex = new AdatbazisException(uzenet);

    // Then
    assertEquals(uzenet, ex.getMessage());
  }

  @Test
  void adatbazisExceptionUzenettelEsOkkalMukodik() {
    // Given
    String uzenet = "Adatbázis hiba";
    Throwable ok = new RuntimeException("SQL hiba");

    // When
    AdatbazisException ex = new AdatbazisException(uzenet, ok);

    // Then
    assertEquals(uzenet, ex.getMessage());
    assertNotNull(ex.getCause());
  }
}

