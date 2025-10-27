package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import projekt.progtech.model.Board;
import projekt.progtech.model.Position;
import projekt.progtech.persistence.FileManager;

class FileManagerTest {

  private FileManager fileManager;
  private Board tabla;
  private final String testFajl = "test_tabla.xml";

  @BeforeEach
  void setUp() {
    fileManager = new FileManager();
    tabla = new Board(5, 5);
  }

  @AfterEach
  void cleanup() {
    // Teszt után törölni kell a létrehozott fájlt
    File fajl = new File(testFajl);
    if (fajl.exists()) {
      boolean deleted = fajl.delete();
      if (!deleted) {
        fajl.deleteOnExit();
      }
    }
  }

  @Test
  void mentesEsBetoltesHelyesenMukodik() {
    // Given - Lerakunk pár jelet
    tabla.lerak(new Position(0, 0), 'X');
    tabla.lerak(new Position(1, 1), 'O');
    tabla.lerak(new Position(2, 2), 'X');

    // When - Mentés
    boolean mentesSiker = fileManager.ment(tabla, testFajl);
    assertTrue(mentesSiker);

    // When - Betöltés
    Board betoltott = fileManager.betolt(testFajl);

    // Then
    assertNotNull(betoltott);
    assertEquals('X', betoltott.getMezo(new Position(0, 0)));
    assertEquals('O', betoltott.getMezo(new Position(1, 1)));
    assertEquals('X', betoltott.getMezo(new Position(2, 2)));
    assertEquals('.', betoltott.getMezo(new Position(3, 3)));
  }

  @Test
  void nemLetezoFajlBetolteseNullVisszaadasa() {
    // When
    Board eredmeny = fileManager.betolt("nemletezo.xml");

    // Then
    assertNull(eredmeny);
  }

  @Test
  void uresTablaMenthetoEsBetoltheto() {
    // Given - Üres tábla
    Board uresTabla = new Board(3, 3);

    // When
    fileManager.ment(uresTabla, testFajl);
    Board betoltott = fileManager.betolt(testFajl);

    // Then
    assertNotNull(betoltott);
    assertTrue(betoltott.isUres(new Position(0, 0)));
    assertTrue(betoltott.isUres(new Position(1, 1)));
  }
}