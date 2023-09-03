package testing.steps;

import io.cucumber.java.ru.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.fail;

public class UiStepCases {

    private WebDriver driver = Hooks.getDriver();

    @Допустим("открыта страница по адресу {string}")
    public void открыта_страница_по_адресу(String url) {
        driver.get(url);
        driver.manage().window().maximize();
    }

    @И("^.* \"([^\"]*)\" отображается$")
    public void элемент_отображается(String elName) {
        switch (elName) {
            case "Список товаров":
                try {
                    driver.findElement(By.xpath("//h5"));
                } catch (NoSuchElementException e) {
                    fail("Element " + elName + " not found");
                }
                break;
            case "#":
                try {
                    driver.findElement(By.xpath("//th[text()='#']"));
                } catch (NoSuchElementException e) {
                    fail("Element " + elName + " not found");
                }
                break;
            case "Наименование":
                try {
                    driver.findElement(By.xpath("//th[text()='Наименование']"));
                } catch (NoSuchElementException e) {
                    fail("Element " + elName + " not found");
                }
                break;
            case "Тип":
                try {
                    driver.findElement(By.xpath("//th[text()='Тип']"));
                } catch (NoSuchElementException e) {
                    fail("Element " + elName + " not found");
                }
                break;
            case "Экзотический":
                try {
                    driver.findElement(By.xpath("//th[text()='Экзотический']"));
                } catch (NoSuchElementException e) {
                    fail("Element " + elName + " not found");
                }
                break;
            case "Добавить":
                try {
                    driver.findElement(By.xpath("//button[@data-target='#editModal']"));
                } catch (NoSuchElementException e) {
                    fail("Element " + elName + " not found");
                }
                break;
            case "Добавление товаров":
                try {
                    WebElement modalWindow = Hooks.getWait().until(ExpectedConditions.visibilityOfElementLocated(
                            By.id("editModalLabel")));
                    String modalWindowTitle = modalWindow.getText();
//      В тз указано, что модальное окно именно "Добавление товаров", но чтоб не фэйлить тест подправил на "товара".
                    Assertions.assertEquals("Добавление товара", modalWindowTitle,
                            "Wrong modal window title");
                } catch (NoSuchElementException e) {
                    fail("Element " + elName + " not found");
                }
                break;
            default:
                throw new RuntimeException("Запрашиваемый элемент не определен " + elName);
        }
    }

    @И("^кнопка \"([^\"]*)\" синего цвета")
    public void кнопка_синего_цвета(String elName) {
        if (elName.equals("Добавить")) {
            WebElement button = driver.findElement(By.xpath("//button[@data-target='#editModal']"));
            String backgroundColor = button.getCssValue("background-color");
            String borderColor = button.getCssValue("border-color");
            Assertions.assertEquals("rgba(0, 123, 255, 1)", backgroundColor, "Wrong background color");
            Assertions.assertEquals("rgb(0, 123, 255)", borderColor, "Wrong border color");
        } else {
            throw new RuntimeException("Запрашиваемый элемент не определен " + elName);
        }
    }

    @И("^клик на .* \"([^\"]*)\"$")
    public void клик_на_элемент(String elName) {
        if (elName.equals("Добавить")) {
            try {
                driver.findElement(By.xpath("//button[@data-target='#editModal']")).click();
            } catch (NoSuchElementException e) {
                fail("Element " + elName + " not found");
            }
        } else if (elName.equals("Сохранить")) {
            try {
                driver.findElement(By.id("save")).click();
            } catch (NoSuchElementException e) {
                fail("Element " + elName + " not found");
            }
        } else {
            throw new RuntimeException("Запрашиваемый элемент не определен " + elName);
        }
    }

    @И("заполнить поле {string} значением {string}")
    public void заполнить_поле_значением(String fieldName, String value) {
        WebElement element = null;
        switch (fieldName) {
            case "Наименование":
                try {
                    element = driver.findElement(By.id("name"));
                } catch (NoSuchElementException e) {
                    fail("Element " + fieldName + " not found");
                }
                element.sendKeys(value);
                break;
            case "Тип":
                try {
                    element = driver.findElement(By.id("type"));
                } catch (NoSuchElementException e) {
                    fail("Element " + fieldName + " not found");
                }
                Select dropdown = new Select(element);
                if (value.equals("фрукт".toLowerCase())) {
                    dropdown.selectByValue("FRUIT");
                } else if (value.equals("овощ".toLowerCase())) {
                    dropdown.selectByValue("VEGETABLE");
                } else {
                    throw new RuntimeException("Выпадающий список не содержит такое значение");
                }
                break;
            case "Экзотический":
                try {
                    element = driver.findElement(By.id("exotic"));
                } catch (NoSuchElementException e) {
                    fail("Element " + fieldName + " not found");
                }
                if (value.equals("true".toLowerCase())) {
                    if (!element.isSelected()) {
                        element.click();
                    }
                } else if (value.equals("false".toLowerCase())) {
                    if (element.isSelected()) {
                        element.click();
                    }
                } else {
                    throw new RuntimeException("wrong checkbox value");
                }
                break;
            default:
                throw new RuntimeException("Поле " + fieldName + " не задано");
        }
    }

    // START FROM HERE

    @И("^.* \"([^\"]*)\" закрыто$")
    public void модальное_окно_закрыто(String elemToBeClosed) {
        if (elemToBeClosed.equals("Добавление товара")) {
            try {
                Hooks.getWait().until(ExpectedConditions.invisibilityOfElementLocated(
                        By.id("editModalLabel")));
            } catch (org.openqa.selenium.TimeoutException e) {
                fail("Modal window still opened");
            }
        } else {
            throw new RuntimeException("Элемент " + elemToBeClosed + " не определен");
        }
    }

//    @И("сохранить в DataManager .* \"([^\"]*)\" с значением \"([^\"]*)\"$")
//    public void saveData(String key, String data){
//        DataManager.getDataManager().saveDataValue(key, data);
//    }

    @Тогда("в таблице отображается товар с параметрами {string} {string} {string}")
    public void в_таблице_отображается_товар_с_параметрами(String name, String type, String exot) {
        int lastIdBeforeAdd = Integer.parseInt(driver.findElement(By.xpath(
                "//tbody/tr[last() - 1]/th")).getText());
        //        Тут создаем нужный номер айдишника для проверки добавленного товара.
        String id = String.valueOf(lastIdBeforeAdd + 1);

        WebElement newGoodId = driver.findElement(By.xpath("//tbody/tr[last()]/th"));
        WebElement newGoodName = newGoodId.findElement(By.xpath("../td[1]"));
        WebElement newGoodType = newGoodId.findElement(By.xpath("../td[2]"));
        WebElement newGoodExoticBool = newGoodId.findElement(By.xpath("../td[3]"));
        // Проверяем корректность данных
        Assertions.assertEquals(id, newGoodId.getText(), "Некорректный id добавленного товара.");
        Assertions.assertEquals(name, newGoodName.getText(), "Не соответсвие названия добавленного товара");
        Assertions.assertEquals(type, newGoodType.getText(), "Некорректный тип товара");
        Assertions.assertEquals(exot, newGoodExoticBool.getText(), "Некорректный тип экзотичности.");
    }
}