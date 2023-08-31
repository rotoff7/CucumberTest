package testing.previousTests.dbTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class DBTest extends BaseTest {

    @Test
    @DisplayName("sql test-case#1")
    void test1() {
        String sqlInsert = ("INSERT INTO food (food_name, food_type, food_exotic) VALUES (?, ?, ?);");
//        Запрос с двумя параметрами в точности как в ручном тест-кейсе (можно обойтись только food_name)
        String sqlSelect = ("SELECT * FROM food WHERE food_name = ? AND food_type = ?;");
//        Добавляем товар (шаг 1)
        try (PreparedStatement ps = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, "Нвй фрукт1 тест");
            ps.setString(2, "FRUIT");
            ps.setInt(3, 1);
            ps.executeUpdate();
            // Получае id для удаления строки после кейса
            try (ResultSet resultSet = ps.getGeneratedKeys();) {
                resultSet.last();
                int lastRowId = resultSet.getInt(1);
                // Получаем данные только что добавленного товара
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
                        // Возвращаем изначальное состояние БД, удалив добавленный товар.
                        SqlHelp.delRow(lastRowId, connection);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}