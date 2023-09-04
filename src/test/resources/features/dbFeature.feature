# language: ru

@all
@DataBase
Функция: Добавление товаров в БД

  Предыстория:
    * установка соединения с БД "h2"

  @correct
  @db
  Сценарий: Добавление товара в БД
    * добавить новый товар в бд:
      | column      | value           |
      | FOOD_NAME   | Нвй фрукт1 тест |
      | FOOD_TYPE   | FRUIT           |
      | FOOD_EXOTIC | 1               |
#    * cохранить значение "id" в DataManager
    * получить из таблицы данные о добавленном товаре
#    * сохранить данные выборки в DataManager
    * сверить корректность данных:
      | FOOD_NAME   | Нвй фрукт1 тест |
      | FOOD_TYPE   | FRUIT           |
      | FOOD_EXOTIC | 1               |


#      | column      | standard        | data     |
#      | food_id     | #:generatedId   | #:newID  |
#      | food_name   | Нвй фрукт1 тест | #:name   |
#      | food_type   | FRUIT           | #:type   |
#      | food_exotic | 1               | #:exotic |
#    * удалить запись из таблицы по id = "#:food_id"