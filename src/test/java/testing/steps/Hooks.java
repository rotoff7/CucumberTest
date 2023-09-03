package testing.steps;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

public class Hooks {

    static WebDriver driver;
    static WebDriverWait wait;

    static Connection connection;
    @Before("@ui") // В BeforeALl нельзя установить тег
    public void beforeTest(){
        ChromeOptions co = new ChromeOptions();
        co.setBinary("E:\\TestChrome\\chrome-win64\\chrome.exe");

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver(co);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

//    @Before("db")
//    public void connect() {
//        DataSource source = getDataSource();
//        try {
//            connection = source.getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @After("@ui")
    public void afterTest(){
        driver.close();
    }

    static WebDriver getDriver() {
        return driver;
    }
    static WebDriverWait getWait(){
        return wait;
    }
}
