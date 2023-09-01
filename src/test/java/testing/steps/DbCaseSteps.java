package testing.steps;

import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.*;

public class DbCaseSteps {

    private Connection connection;
    private String sqlInsert = "INSERT INTO food (food_name, food_type, food_exotic) VALUES (?, ?, ?);";
    private String sqlSelect = ("SELECT * FROM food WHERE food_name = ? AND food_type = ?;");

    @И("Установлено соединение с БД")
    public void connect() {
        DataSource source = getDataSource();
        try {
            connection = source.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Допустим("Добавить новый товар в бд")
    public void выполнить_запрос() {
        try (PreparedStatement ps = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, "Нвй фрукт1 тест");
            ps.setString(2, "FRUIT");
            ps.setInt(3, 1);
            ps.executeUpdate();
            // Получае id для удаления строки после кейса
            try (ResultSet resultSet = ps.getGeneratedKeys();) {
                resultSet.last();
                int lastRowId = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @И("В бд появилась новая запись")
    public void новая_запись(){
        try (PreparedStatement ps2 = connection.prepareStatement(sqlSelect)) {
            ps2.setString(1, "Нвй фрукт1 тест");
            ps2.setString(2, "FRUIT");
            try (ResultSet rs = ps2.executeQuery()) {
                rs.last();
                String name = rs.getString("food_name");
                String type = rs.getString("food_type");
                int exotic = rs.getInt("food_exotic");
//                        Проверяем корректность данных. Надеюсь тут правильность установки id ассертить не надо.
                Assertions.assertEquals("Нвй фрукт1 тест", name, "Wrong name");
                Assertions.assertEquals("FRUIT", type, "Wrong type");
                Assertions.assertEquals(1, exotic, "Wrong exotic type");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void delRow(int id, Connection connect) {
        String sqlRequest = "delete from FOOD where food_id = (?)";
        try (PreparedStatement ps = connect.prepareStatement(sqlRequest);) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource getDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost:9092/mem:testdb");
        dataSource.setUser("user");
        dataSource.setPassword("pass");
        return dataSource;
    }
}