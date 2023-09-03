package testing.steps;

import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Assertions;
import testing.data.DataManager;
import testing.utils.UtilClass;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbCaseSteps {

    private Connection connection;
    private String sqlInsert = "INSERT INTO food (food_name, food_type, food_exotic) VALUES (?, ?, ?);";
    private String sqlSelect = ("SELECT * FROM food WHERE food_name = ? AND food_type = ?;");

    @И("^установка соединения с БД \"([^\"]*)\"$")
    public void connect(String bdType) {
        if (bdType.equals("H2".toLowerCase())){
            DataSource source = UtilClass.getH2DataSource();
            try {
                connection = source.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("База данных " + bdType + " не определена");
        }
    }
    @И("^добавить новый товар в бд:$")
    public void добавитьНовыйТоварВБд(Map<String, String> data) {
        String foodName = data.get("food_name");
        String foodType = data.get("food_type");
        int foodExotic = Integer.parseInt(data.get("food_exotic"));
        try (PreparedStatement ps = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, foodName);
            ps.setString(2, foodType);
            ps.setInt(3, foodExotic);

            try (ResultSet resultSet = ps.getGeneratedKeys();) {
                resultSet.last();
                int RowId = resultSet.getInt(1);
                DataManager.getDataManager().saveDataValue("generatedId", String.valueOf(RowId));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        // Код для добавления нового товара в базу данных
        // Используйте переменные foodName, foodType и foodExotic для сохранения значений в базе данных
    }

    @И("получить из таблицы данные о добавленном товаре")
    public void новая_запись(){
        try (PreparedStatement ps = connection.prepareStatement(sqlSelect)) {
            ps.setString(1, "Нвй фрукт1 тест");
            ps.setString(2, "FRUIT");
            try (ResultSet rs = ps.executeQuery()) {
                rs.last();
                DataManager.getDataManager().saveDataValue("name", rs.getString("food_name"));
                DataManager.getDataManager().saveDataValue("type", rs.getString("food_type"));
                DataManager.getDataManager().saveDataValue("exotic", rs.getString("food_exotic"));
//                        Проверяем корректность данных. Надеюсь тут правильность установки id ассертить не надо.
//                Assertions.assertEquals("Нвй фрукт1 тест", name, "Wrong name");
//                Assertions.assertEquals("FRUIT", type, "Wrong type");
//                Assertions.assertEquals(1, exotic, "Wrong exotic type");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @И("сверить корректность данных: ")
    public void сверка_данных(Map<String,String> standard){
        List<String> data = new ArrayList<>(DataManager.getDataManager().getMap().values());
        for (int i = 0; i < data.size(); i++) {

        }
        for (String value : standard.values()){
//            if (value.startsWith("#:")){
//                String id = DataManager.getDataManager().getDataValue("generatedId");
//            }
            Assertions.assertEquals(value, data);
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

}