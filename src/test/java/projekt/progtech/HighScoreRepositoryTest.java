package projekt.progtech;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HighScoreRepositoryTest {

  private HighScoreRepository repo;

  @BeforeEach
  void setUp() {
    repo = new HighScoreRepository("mem:testdb;DB_CLOSE_DELAY=-1");
    repo.torol();
  }

  @Test
  void mentesEsTopRendezesMukodik() {
    repo.ment(new HighScore("Alice", HighScore.Eredmeny.WIN, 10, 10, 12, LocalDateTime.now()));
    repo.ment(new HighScore("Bob", HighScore.Eredmeny.DRAW, 10, 10, 11, LocalDateTime.now()));
    repo.ment(new HighScore("Carol", HighScore.Eredmeny.LOSS, 10, 10, 9, LocalDateTime.now()));
    repo.ment(new HighScore("Dave", HighScore.Eredmeny.WIN, 10, 10, 9, LocalDateTime.now()));

    List<HighScore> top = repo.lekerTop(10);
    assertFalse(top.isEmpty());
    // WINs first, fewer moves earlier
    assertEquals("Dave", top.get(0).getJatekosNev());
    assertEquals("Alice", top.get(1).getJatekosNev());
    // DRAW then LOSS
    assertEquals(HighScore.Eredmeny.DRAW, top.get(2).getEredmeny());
    assertEquals(HighScore.Eredmeny.LOSS, top.get(3).getEredmeny());
  }

  @Test
  void topLimitAlkalmazva() {
    for (int i = 0; i < 5; i++) {
      repo.ment(new HighScore("P" + i, HighScore.Eredmeny.WIN, 5, 5, 10 + i, LocalDateTime.now()));
    }
    List<HighScore> top = repo.lekerTop(3);
    assertEquals(3, top.size());
  }
}
