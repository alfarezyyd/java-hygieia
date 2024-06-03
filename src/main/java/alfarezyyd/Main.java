package alfarezyyd;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;


public class Main {
  public static void main(String[] args) {
    AppConnection.initializeConnection();
    Path rawDatasetPath = Path.of("src/main/resources/fifa21.csv");
    try (
        BufferedReader bufferedReader = Files.newBufferedReader(rawDatasetPath);
    ) {
      CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.builder().setHeader().build());
      for (CSVRecord csvRecord : csvParser) {
        try (Connection connection = AppConnection.getConnection()) {
          Integer nationalityId = null;
          PreparedStatement nationalityQueryPreparedStatement = connection.prepareStatement("SELECT * FROM nationalities WHERE nationality = ?");
          nationalityQueryPreparedStatement.setString(1, csvRecord.get("Nationality"));
          ResultSet nationalityResultSet = nationalityQueryPreparedStatement.executeQuery();
          if (nationalityResultSet.next()) {
            nationalityId = nationalityResultSet.getInt("nationality_id");
          } else {
            PreparedStatement nationalityInsertPreparedStatement = connection.prepareStatement("INSERT INTO nationalities (nationality) VALUE  (?)", Statement.RETURN_GENERATED_KEYS);
            nationalityInsertPreparedStatement.setString(1, csvRecord.get("Nationality"));
            nationalityInsertPreparedStatement.executeUpdate();
            nationalityResultSet = nationalityInsertPreparedStatement.getGeneratedKeys();
            if (nationalityResultSet.next()) {
              nationalityId = nationalityResultSet.getInt(1);
            }
          }

          Integer clubId = null;
          PreparedStatement clubQueryPreparedStatement = connection.prepareStatement("SELECT * FROM clubs WHERE club_name = ?");
          clubQueryPreparedStatement.setString(1, csvRecord.get("Club").trim());
          ResultSet clubResultSet = clubQueryPreparedStatement.executeQuery();
          if (clubResultSet.next()) {
            clubId = clubResultSet.getInt("club_id");
          } else {
            PreparedStatement clubInsertPreparedStatement = connection.prepareStatement("INSERT INTO clubs (club_name) VALUE  (?)", Statement.RETURN_GENERATED_KEYS);
            clubInsertPreparedStatement.setString(1, csvRecord.get("Club").trim());
            clubInsertPreparedStatement.executeUpdate();
            clubResultSet = clubInsertPreparedStatement.getGeneratedKeys();
            if (clubResultSet.next()) {
              clubId = clubResultSet.getInt(1);
            }
          }

          Integer playerId = null;
          PreparedStatement playerPreparedStatement = connection.prepareStatement("""
              INSERT INTO players(
              name,
              long_name,
              photo_url,
              player_url,
              nationality_id,
              age,
              ova,
              pot,
              club_id,
              contract,
              height,
              weight,
              preferred_foot,
              best_position,
              joined,
              loan_date_end,
              value,
              wage,
              release_clause,
              total_stats,
              base_stats,
              w_f,
              sm,
              a_w,
              d_w,
              ir,
              hits
              ) VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                               \s""", Statement.RETURN_GENERATED_KEYS);
          playerPreparedStatement.setString(1, csvRecord.get("Name"));
          playerPreparedStatement.setString(2, csvRecord.get("LongName"));
          playerPreparedStatement.setString(3, csvRecord.get("photoUrl"));
          playerPreparedStatement.setString(4, csvRecord.get("playerUrl"));
          playerPreparedStatement.setInt(5, nationalityId);
          playerPreparedStatement.setString(6, csvRecord.get("Age"));
          playerPreparedStatement.setString(7, csvRecord.get("↓OVA"));
          playerPreparedStatement.setString(8, csvRecord.get("POT"));
          playerPreparedStatement.setInt(9, clubId);
          playerPreparedStatement.setString(10, csvRecord.get("Contract"));
          playerPreparedStatement.setFloat(11, CommonHelper.convertFeetIntoCentimeter(csvRecord.get("Height")));
          System.out.println(csvRecord.get("Weight"));

          playerPreparedStatement.setFloat(12, CommonHelper.convertPoundIntoKilogram(csvRecord.get("Weight")));
          playerPreparedStatement.setString(13, csvRecord.get("Preferred Foot"));
          playerPreparedStatement.setString(14, csvRecord.get("Best Position"));
          playerPreparedStatement.setString(15, csvRecord.get("Joined"));
          playerPreparedStatement.setString(16, csvRecord.get("Loan Date End"));
          playerPreparedStatement.setFloat(17, CommonHelper.convertFromCurrencyIntoInteger(csvRecord.get("Value")));
          playerPreparedStatement.setFloat(18, CommonHelper.convertFromCurrencyIntoInteger(csvRecord.get("Wage")));
          System.out.println(csvRecord.get("Release Clause"));
          playerPreparedStatement.setFloat(19, CommonHelper.convertFromCurrencyIntoInteger(csvRecord.get("Release Clause")));
          playerPreparedStatement.setString(20, csvRecord.get("Total Stats"));
          playerPreparedStatement.setString(21, csvRecord.get("Base Stats"));
          playerPreparedStatement.setString(22, csvRecord.get("W/F").replaceAll("★", ""));
          playerPreparedStatement.setString(23, csvRecord.get("SM").replaceAll("★", ""));
          playerPreparedStatement.setString(24, csvRecord.get("A/W"));
          playerPreparedStatement.setString(25, csvRecord.get("D/W"));
          playerPreparedStatement.setString(26, csvRecord.get("IR").replaceAll("★", ""));
          System.out.println(csvRecord.get("Hits"));
          playerPreparedStatement.setFloat(27, CommonHelper.convertUnitIntoInteger(csvRecord.get("Hits").trim()));
          playerPreparedStatement.executeUpdate();
          ResultSet generatedKeys = playerPreparedStatement.getGeneratedKeys();
          if (generatedKeys.next()) {
            playerId = generatedKeys.getInt(1);
          }

          PreparedStatement statisticPreparedStatement = connection.prepareStatement("""
              INSERT INTO statistics(
                player_id,
                attacking,
                crossing,
                finishing,
                heading_accuracy,
                short_passing,
                volleys,
                skill,
                dribbling,
                curve,
                fk_accuracy,
                long_passing,
                ball_control,
                movement,
                acceleration,
                sprint_speed,
                agility,
                reactions,
                balance,
                power,
                shot_power,
                jumping,
                stamina,
                strength,
                long_shots,
                mentality,
                aggression,
                interceptions,
                positioning,
                vision,
                penalties,
                composure,
                defending,
                marking,
                standing_tackle,
                sliding_tackle,
                goalkeeping,
                gk_diving,
                gk_handling,
                gk_kicking,
                gk_positioning,
                gk_reflexes) VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                             \s""");
          statisticPreparedStatement.setInt(1, playerId);
          statisticPreparedStatement.setInt(2, Integer.parseInt(csvRecord.get("Attacking")));
          statisticPreparedStatement.setInt(3, Integer.parseInt(csvRecord.get("Crossing")));
          statisticPreparedStatement.setInt(4, Integer.parseInt(csvRecord.get("Finishing")));
          statisticPreparedStatement.setInt(5, Integer.parseInt(csvRecord.get("Heading Accuracy")));
          statisticPreparedStatement.setInt(6, Integer.parseInt(csvRecord.get("Short Passing")));
          statisticPreparedStatement.setInt(7, Integer.parseInt(csvRecord.get("Volleys")));
          statisticPreparedStatement.setInt(8, Integer.parseInt(csvRecord.get("Skill")));
          statisticPreparedStatement.setInt(9, Integer.parseInt(csvRecord.get("Dribbling")));
          statisticPreparedStatement.setInt(10, Integer.parseInt(csvRecord.get("Curve")));
          statisticPreparedStatement.setInt(11, Integer.parseInt(csvRecord.get("FK Accuracy")));
          statisticPreparedStatement.setInt(12, Integer.parseInt(csvRecord.get("Long Passing")));
          statisticPreparedStatement.setInt(13, Integer.parseInt(csvRecord.get("Ball Control")));
          statisticPreparedStatement.setInt(14, Integer.parseInt(csvRecord.get("Movement")));
          statisticPreparedStatement.setInt(15, Integer.parseInt(csvRecord.get("Acceleration")));
          statisticPreparedStatement.setInt(16, Integer.parseInt(csvRecord.get("Sprint Speed")));
          statisticPreparedStatement.setInt(17, Integer.parseInt(csvRecord.get("Agility")));
          statisticPreparedStatement.setInt(18, Integer.parseInt(csvRecord.get("Reactions")));
          statisticPreparedStatement.setInt(19, Integer.parseInt(csvRecord.get("Balance")));
          statisticPreparedStatement.setInt(20, Integer.parseInt(csvRecord.get("Power")));
          statisticPreparedStatement.setInt(21, Integer.parseInt(csvRecord.get("Shot Power")));
          statisticPreparedStatement.setInt(22, Integer.parseInt(csvRecord.get("Jumping")));
          statisticPreparedStatement.setInt(23, Integer.parseInt(csvRecord.get("Stamina")));
          statisticPreparedStatement.setInt(24, Integer.parseInt(csvRecord.get("Strength")));
          statisticPreparedStatement.setInt(25, Integer.parseInt(csvRecord.get("Long Shots")));
          statisticPreparedStatement.setInt(26, Integer.parseInt(csvRecord.get("Mentality")));
          statisticPreparedStatement.setInt(27, Integer.parseInt(csvRecord.get("Aggression")));
          statisticPreparedStatement.setInt(28, Integer.parseInt(csvRecord.get("Interceptions")));
          statisticPreparedStatement.setInt(29, Integer.parseInt(csvRecord.get("Positioning")));
          statisticPreparedStatement.setInt(30, Integer.parseInt(csvRecord.get("Vision")));
          statisticPreparedStatement.setInt(31, Integer.parseInt(csvRecord.get("Penalties")));
          statisticPreparedStatement.setInt(32, Integer.parseInt(csvRecord.get("Composure")));
          statisticPreparedStatement.setInt(33, Integer.parseInt(csvRecord.get("Defending")));
          statisticPreparedStatement.setInt(34, Integer.parseInt(csvRecord.get("Marking")));
          statisticPreparedStatement.setInt(35, Integer.parseInt(csvRecord.get("Standing Tackle")));
          statisticPreparedStatement.setInt(36, Integer.parseInt(csvRecord.get("Sliding Tackle")));
          statisticPreparedStatement.setInt(37, Integer.parseInt(csvRecord.get("Goalkeeping")));
          statisticPreparedStatement.setInt(38, Integer.parseInt(csvRecord.get("GK Diving")));
          statisticPreparedStatement.setInt(39, Integer.parseInt(csvRecord.get("GK Handling")));
          statisticPreparedStatement.setInt(40, Integer.parseInt(csvRecord.get("GK Kicking")));
          statisticPreparedStatement.setInt(41, Integer.parseInt(csvRecord.get("GK Positioning")));
          statisticPreparedStatement.setInt(42, Integer.parseInt(csvRecord.get("GK Reflexes")));
          statisticPreparedStatement.executeUpdate();
          System.out.println("Data created successfully");
        } catch (SQLException e) {
          throw new RuntimeException("Error when trying interact to database", e);
        }
      }
    } catch (IOException ioException) {
      System.out.println("Error: " + ioException.getMessage());
    }
  }
}