package testing.utils;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class UtilClass {

    public static String sqlInsert = "INSERT INTO food (food_name, food_type, food_exotic) VALUES (?, ?, ?);";
    public static String sqlSelect = "SELECT * FROM food WHERE food_name = ? AND food_type = ?;";
    public static String sqlDelete = "DELETE FROM food WHERE FOOD_ID = (?)";
    static Connection connection;


    public static DataSource getH2DataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost:9092/mem:testdb");
        dataSource.setUser("user");
        dataSource.setPassword("pass");
        return dataSource;
    }

    public static Connection connect(String bdType) {
        if (bdType.equals("H2".toLowerCase())){
            DataSource source = UtilClass.getH2DataSource();
            try {
                connection = source.getConnection();
                return connection;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("База данных " + bdType + " не определена");
        }
    }
}
