package projekt.progtech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Egyszerű H2 alapú HighScore repository.
 */
public class HighScoreRepository {

  private static final Logger logger = LoggerFactory.getLogger(HighScoreRepository.class);

  private final String jdbcUrl;

  /**
   * Konstruktor.
   *
   * @param path H2 adatbázis elérési út vagy teljes JDBC URL
   */
  public HighScoreRepository(String path) {
    // Ha teljes JDBC URL-t kapunk, használjuk változtatás nélkül
    if (path != null && path.startsWith("jdbc:h2:")) {
      this.jdbcUrl = path;
    } else if (path != null && path.startsWith("mem:")) {
      // In-memory adatbázis esetén nem használható az AUTO_SERVER
      this.jdbcUrl = "jdbc:h2:" + path;
    } else {
      // Fájlos adatbázis (alap): engedélyezzük az AUTO_SERVER-t
      String filePath = (path == null || path.isBlank()) ? "./highscore" : path;
      // H2 újabb verzióiban a file: előtag ajánlott
      if (!filePath.startsWith("file:")) {
        filePath = "file:" + filePath;
      }
      this.jdbcUrl = "jdbc:h2:" + filePath + ";AUTO_SERVER=TRUE";
    }
    initSchema();
  }

  private void initSchema() {
    String ddl = "CREATE TABLE IF NOT EXISTS highscore (" +
        "id IDENTITY PRIMARY KEY, " +
        "player VARCHAR(100) NOT NULL, " +
        "result VARCHAR(10) NOT NULL, " +
        "rows INT NOT NULL, " +
        "cols INT NOT NULL, " +
        "moves INT NOT NULL, " +
        "created_at TIMESTAMP NOT NULL)";

    try (Connection c = DriverManager.getConnection(jdbcUrl);
         Statement st = c.createStatement()) {
      st.execute(ddl);
      logger.info("HighScore tábla ellenőrizve/létrehozva");
    } catch (SQLException e) {
      logger.error("Schema init hiba: {}", e.getMessage());
    }
  }

  /**
   * Bejegyzés mentése.
   */
  public void ment(HighScore score) {
    String dml = "INSERT INTO highscore(player, result, rows, cols, moves, created_at) " +
        "VALUES(?,?,?,?,?,?)";
    try (Connection c = DriverManager.getConnection(jdbcUrl);
         PreparedStatement ps = c.prepareStatement(dml)) {
      ps.setString(1, score.getJatekosNev());
      ps.setString(2, score.getEredmeny().name());
      ps.setInt(3, score.getSorok());
      ps.setInt(4, score.getOszlopok());
      ps.setInt(5, score.getLepesekSzama());
      ps.setObject(6, java.sql.Timestamp.valueOf(score.getIdopont()));
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.error("HighScore mentési hiba: {}", e.getMessage());
    }
  }

  /**
   * Legjobb N bejegyzés visszaadása (elsősorban győzelmek, majd kevesebb lépés, majd újabb).
   */
  public List<HighScore> lekerTop(int limit) {
    String sql = "SELECT player, result, rows, cols, moves, created_at " +
        "FROM highscore ORDER BY " +
        "CASE result WHEN 'WIN' THEN 0 WHEN 'DRAW' THEN 1 ELSE 2 END, " +
        "moves ASC, created_at ASC LIMIT ?";

    List<HighScore> list = new ArrayList<>();
    try (Connection c = DriverManager.getConnection(jdbcUrl);
         PreparedStatement ps = c.prepareStatement(sql)) {
      ps.setInt(1, limit);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          list.add(new HighScore(
              rs.getString(1),
              HighScore.Eredmeny.valueOf(rs.getString(2)),
              rs.getInt(3),
              rs.getInt(4),
              rs.getInt(5),
              rs.getTimestamp(6).toLocalDateTime()
          ));
        }
      }
    } catch (SQLException e) {
      logger.error("HighScore lekérdezési hiba: {}", e.getMessage());
    }
    return list;
  }

  /**
   * Tábla törlése (tesztekhez).
   */
  public void torol() {
    try (Connection c = DriverManager.getConnection(jdbcUrl);
         Statement st = c.createStatement()) {
      st.execute("TRUNCATE TABLE highscore");
    } catch (SQLException e) {
      logger.error("HighScore törlési hiba: {}", e.getMessage());
    }
  }
}
