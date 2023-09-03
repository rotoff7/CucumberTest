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
    private String sqlSelect = "SELECT * FROM food WHERE food_name = ? AND food_type = ?;";

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
            ps.executeUpdate();

            try (ResultSet resultSet = ps.getGeneratedKeys();) {
                resultSet.last();
                int lastRowId = resultSet.getInt(1);
                DataManager.getDataManager().saveDataValue("generatedId", String.valueOf(lastRowId));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @И("^сверить корректность данных:$")
    public void сверка_данных(Map<String,String> standard){
        List<String> data = new ArrayList<>(DataManager.getDataManager().getMap().values());
        int index = 0;
        System.out.println(data);
        for (String value : standard.values()){
            if (value.startsWith("#:")){
                value = DataManager.getDataManager().getDataValue("generatedId");
            }
            Assertions.assertEquals(value, data.get(index), "Assertion fail");
            index+=1;
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