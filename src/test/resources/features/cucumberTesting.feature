# language: ru


@all
@AddGoodsUI
Функция: Добавление товара в список

  Предыстория:
    * открыта страница по адресу "http://localhost:8080/food";

  @correct
  Сценарий: Отображение полей таблицы
    * Колонка "id" отображается
    * Колонка "name" отображается
    * Колонка "type" отображается
    * Колонка "exotic" отображается
    * Кнопка "Добавить" отображается
    * Кнопка "Добавить" синего цвета

  @correct
  Сценарий: Добавление нового товара в список
    * Клик на кнопку "Добавить"
    * Открыто модальное окно
    * Заполнить поле "name" значением "fruitName"
    * Заполнить поле "type" значением "fruitType"
    * Заполнить поле "exotic" значением "fruitExotic"
    * Клик на кнопку "Сохранить"
    * Модальное окно закрыто
    * В таблице отображается товар с параметрами "id" "name" "type" "exotic"
