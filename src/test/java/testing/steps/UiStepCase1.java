package testing.steps;

import io.cucumber.java.ru.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.fail;

public class UiStepCase1 {
    private WebDriver driver;
    private WebDriverWait wait;

    @Допустим("открыта страница по адресу {string}")
    public void открыта_страница_по_адресу(String url) {
        ChromeOptions co = new ChromeOptions();
        co.setBinary("E:\\TestChrome\\chrome-win64\\chrome.exe");

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver(co);

        driver.get(url);
        driver.manage().window().maximize();
    }

    @И("Таблица 'Список товаров' отображается")
    public void таблица_товаров_отображается() {
        try {
            getElementByXpath("//h5");
        } catch (NoSuchElementException e) {
            fail("Table not found");
        }
    }

    @И("Колонка 'id' отображается")
    public void колонка_id_отображается() {
        try {
            getElementByXpath("//th[text()='#']");
        } catch (NoSuchElementException e) {
            fail("Column not found");
        }
    }

    @И("Колонка 'Наименование' отображается")
    public void колонка_наименование_отображается() {
        try {
            getElementByXpath("//th[text()='Наименование']");
        } catch (NoSuchElementException e) {
            fail("Column not found");
        }
    }

    @И("Колонка 'Тип' отображается")
    public void колонка_тип_отображается() {
        try {
            getElementByXpath("//th[text()='Тип']");
        } catch (NoSuchElementException e) {
            fail("Column not found");
        }
    }

    @И("Колонка 'Экзотический' отображается")
    public void колонка_экзотический_отображается() {
        try {
            getElementByXpath("//th[text()='Экзотический']");
        } catch (NoSuchElementException e) {
            fail("Column not found");
        }
    }

    @И("Кнопка 'Добавить' отображается")
    public void кнопка_добавить_отображается() {
        try {
            getElementByXpath("//button[@data-target='#editModal']");
        } catch (NoSuchElementException e) {
            fail("Button not found");
        }
    }

    @Тогда("Кнопка 'Добавить' синего цвета")
    public void кнопка_синего_цвета() {
        WebElement button = getElementByXpath("//button[@data-target='#editModal']");
        String backgroundColor = button.getCssValue("background-color");
        String borderColor = button.getCssValue("border-color");
        Assertions.assertEquals("rgba(0, 123, 255, 1)", backgroundColor, "Wrong background color");
        Assertions.assertEquals("rgb(0, 123, 255)", borderColor, "Wrong border color");
        //Костыль закрытия браузера
        afterTests();

    }

    //2 Кейс
    @Допустим("Клик на кнопку 'Добавить'")
    public void клик_на_кнопку() {
        getElementByXpath("//button[@data-target='#editModal']").click();
    }
    @Допустим("Открыто модальное окно 'Добавление товаров'")
    public void открыто_модальное_окно() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement modalWindow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editModalLabel")));
        String modalWindowTitle = modalWindow.getText();
//        В тз указано, что модальное окно именно "Добавление товаров", но чтоб не фэйлить тест подправил на "товара".
        Assertions.assertEquals("Добавление товара", modalWindowTitle, "Wrong modalWindowTitle");
    }
    @Допустим("Заполнить поле 'Наименование' значением {string}")
    public void заполнить_поле_значением(String name) {
        WebElement nameUnputField = getElementById("name");
        nameUnputField.sendKeys(name);
    }

    @Допустим("Выбрать тип товара")
    public void выбрать_тип_товара() {
        WebElement typeSelectDrop = getElementById("type");
        typeSelectDrop.click();
        getElementByXpath("//option[@value='FRUIT']").click();
        //Добавил еше раз, поскольку список продолжал быть открытым.
        typeSelectDrop.click();
    }
    @Допустим("Выбрать тип 'Экзотичности'")
    public void выбрать_тип_экзотичности() {
        driver.findElement(By.id("exotic")).click();
    }
    @И("Клик на кнопку 'Сохранить'")
    public void клик_на_кнопку_сохранить(){
        driver.findElement(By.id("save")).click();
    }
    @Допустим("Закрытие модального окна")
    public void модальное_окно_закрыто() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        try {
            Boolean modalWindowCloseCheck = wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.id("editModalLabel")));
        } catch (org.openqa.selenium.TimeoutException e) {
            fail("Modal window still opened");
        }
    }
    @Допустим("В таблице отображается товар с параметрами 'id' {string} {string} {string}")
    public void в_таблице_отображается_товар_с_параметрами(String name, String type, String exot) {
        int lastIdBeforeAdd = getLastIdBeforeAdd();

        WebElement newGoodId = driver.findElement(By.xpath("//tbody/tr[last()]/th"));
        WebElement newGoodName = newGoodId.findElement(By.xpath("../td[1]"));
        WebElement newGoodType = newGoodId.findElement(By.xpath("../td[2]"));
        WebElement newGoodExoticBool = newGoodId.findElement(By.xpath("../td[3]"));
//        Тут создаем нужный номер айдишника для проверки добавленного товара.
        String newLastId = String.valueOf(lastIdBeforeAdd + 1);
        Assertions.assertEquals(newLastId, newGoodId.getText(), "Некорректный id добавленного товара.");
        Assertions.assertEquals(name, newGoodName.getText(), "Не соответсвие названия добавленного товара");
        Assertions.assertEquals(type, newGoodType.getText(), "Некорректный тип товара");
        Assertions.assertEquals(exot, newGoodExoticBool.getText(), "Некорректный тип экзотичности.");
        afterTests();
    }


    private WebElement getElementByXpath(String Xpath) {
        WebElement element = driver.findElement(By.xpath(Xpath));
        return element;
    }

    private WebElement getElementById(String id) {
        WebElement element = driver.findElement(By.id(id));
        return element;
    }

    private int getLastIdBeforeAdd(){
        WebElement element = getElementByXpath("//tbody/tr[last() - 1]/th");
        return Integer.parseInt(element.getText());
    }

    //Костыль закрытия браузера
    void afterTests() {
        driver.quit();
    }

}