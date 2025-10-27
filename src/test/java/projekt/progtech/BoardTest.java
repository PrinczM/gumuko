package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

  private Board tabla;

  @BeforeEach
  void setUp() {
    // Minden teszt előtt csinálunk egy új 10x10-es táblát
    tabla = new Board(10, 10);
  }

  @Test
  void ujTablaUres() {
    // Given & When
    Position kozep = new Position(5, 5);

    // Then
    assertTrue(tabla.isUres(kozep));
    assertEquals('.', tabla.getMezo(kozep));
  }

  @Test
  void lerakasUtanNemUres() {
    // Given
    Position poz = new Position(5, 5);

    // When
    boolean siker = tabla.lerak(poz, 'X');

    // Then
    assertTrue(siker);
    assertFalse(tabla.isUres(poz));
    assertEquals('X', tabla.getMezo(poz));
  }

  @Test
  void nemLehetKetszerLerakniUgyanarra() {
    // Given
    Position poz = new Position(3, 3);
    tabla.lerak(poz, 'X');

    // When
    boolean masodikProba = tabla.lerak(poz, 'O');

    // Then
    assertFalse(masodikProba);
    assertEquals('X', tabla.getMezo(poz)); // Még mindig X van rajta
  }

  @Test
  void ervenytelenPozicioNemLehetLerakni() {
    // Given
    Position kint = new Position(-1, 5);

    // When
    boolean eredmeny = tabla.lerak(kint, 'X');

    // Then
    assertFalse(eredmeny);
  }

  @Test
  void toStringTartalmazOszlopFeliratot() {
    // When
    String eredmeny = tabla.toString();

    // Then
    assertTrue(eredmeny.contains("A"));
    assertTrue(eredmeny.contains("B"));
  }

  @Test
  void vizszintesNyeresFelismeri() {
    // Given - Lerakunk 5 X-et vízszintesen
    tabla.lerak(new Position(5, 1), 'X');
    tabla.lerak(new Position(5, 2), 'X');
    tabla.lerak(new Position(5, 3), 'X');
    tabla.lerak(new Position(5, 4), 'X');
    tabla.lerak(new Position(5, 5), 'X');

    // When & Then
    assertTrue(tabla.vanNyero('X'));
    assertFalse(tabla.vanNyero('O'));
  }

  @Test
  void fuggolegesNyeresFelismeri() {
    // Given - Lerakunk 5 O-t függőlegesen
    tabla.lerak(new Position(1, 5), 'O');
    tabla.lerak(new Position(2, 5), 'O');
    tabla.lerak(new Position(3, 5), 'O');
    tabla.lerak(new Position(4, 5), 'O');
    tabla.lerak(new Position(5, 5), 'O');

    // When & Then
    assertTrue(tabla.vanNyero('O'));
  }

  @Test
  void atlosNyeresFelismeri() {
    // Given - Lerakunk 5 X-et átlósan
    tabla.lerak(new Position(1, 1), 'X');
    tabla.lerak(new Position(2, 2), 'X');
    tabla.lerak(new Position(3, 3), 'X');
    tabla.lerak(new Position(4, 4), 'X');
    tabla.lerak(new Position(5, 5), 'X');

    // When & Then
    assertTrue(tabla.vanNyero('X'));
  }

  @Test
  void negyEgymasMellettMegNemNyero() {
    // Given - Csak 4 van egymás mellett
    tabla.lerak(new Position(5, 2), 'X');
    tabla.lerak(new Position(5, 3), 'X');
    tabla.lerak(new Position(5, 4), 'X');
    tabla.lerak(new Position(5, 5), 'X');

    // When & Then
    assertFalse(tabla.vanNyero('X'));
  }

  @Test
  void uresTablaNemTele() {
    // When & Then
    assertFalse(tabla.isTele());
  }
}