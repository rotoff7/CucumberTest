package testing.steps;

import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import testing.data.DataManager;
import testing.utils.UtilClass;

import java.sql.*;
import java.util.Map;
import java.util.Set;

public class DbCaseSteps {
    private Connection connection;
    @И("^установка соединения с БД \"([^\"]*)\"$")
    public void connect(String bdType) {
        connection = UtilClass.connect(bdType);
    }

    @И("^добавить новый товар в бд:$")
    public void добавитьНовыйТоварВБд(Map<String, String> data) {
        String foodName = data.get("FOOD_NAME");
        String foodType = data.get("FOOD_TYPE");
        int foodExotic = Integer.parseInt(data.get("FOOD_EXOTIC"));
        try (PreparedStatement ps = connection.prepareStatement(UtilClass.sqlInsert, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, foodName);
            ps.setString(2, foodType);
            ps.setInt(3, foodExotic);
            ps.executeUpdate();

            try (ResultSet resultSet = ps.getGeneratedKeys();) {
                resultSet.last();
                int lastRowId = resultSet.getInt(1);
                DataManager.getDataManager().saveDataValue("FOOD_ID", String.valueOf(lastRowId));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    @И("получить из таблицы данные о добавленном товаре")
    public void новая_запись_данные(){
        try (PreparedStatement ps = connection.prepareStatement(UtilClass.sqlSelect)) {
            ps.setString(1, "Нвй фрукт1 тест");
            ps.setString(2, "FRUIT");
            try (ResultSet rs = ps.executeQuery()) {
                rs.last();
                DataManager.getDataManager().saveDataValue("FOOD_NAME", rs.getString("FOOD_NAME"));
                DataManager.getDataManager().saveDataValue("FOOD_TYPE", rs.getString("FOOD_TYPE"));
                DataManager.getDataManager().saveDataValue("FOOD_EXOTIC", rs.getString("FOOD_EXOTIC"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @И("^сверить корректность данных:$")
    public void сверка_данных(Map<String,String> standard){
        Map<String, String> data = DataManager.getDataManager().getMap();
        Set<String> setkey = standard.keySet();
        for (String key : setkey){
            Assertions.assertEquals(standard.get(key), data.get(key),"Assertions fail");
        }
    }
    @И("^удалить добавленный товар из таблицы по \"([^\"]*)\"$")
    public void удалить_товар_по_id(String key){
        int id;
        if (key.startsWith("#:")){
            String idKey = key.replace("#:", "");
            id = Integer.parseInt(DataManager.getDataManager().getDataValue(idKey));
        } else {
            id = Integer.parseInt(key);
        }
        try (PreparedStatement ps = connection.prepareStatement(UtilClass.sqlDelete);){
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}