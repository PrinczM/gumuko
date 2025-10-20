package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileManagerTest {

  private FileManager fileManager;
  private Board tabla;
  private String testFajl = "test_tabla.txt";

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
      fajl.delete();
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
    Board eredmeny = fileManager.betolt("nemletezo.txt");

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