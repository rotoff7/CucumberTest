package testing.previousTests.dbTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelp extends BaseTest {
    static void delRow(int id, Connection connect) {
        String sqlRequest = "delete from FOOD where food_id = (?)";
        try (PreparedStatement ps = connect.prepareStatement(sqlRequest);) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
