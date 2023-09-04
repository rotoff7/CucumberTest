# language: ru

@all
@db
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
    * получить из таблицы данные о добавленном товаре
    * сверить корректность данных:
      | FOOD_NAME   | Нвй фрукт1 тест |
      | FOOD_TYPE   | FRUIT           |
      | FOOD_EXOTIC | 1               |
    * удалить добавленный товар из таблицы по "#:FOOD_ID"


  #    * cохранить значение "id" в DataManager
  #    * сохранить данные выборки в DataManager

#      | column      | standard        | data     |
#      | food_id     | #:generatedId   | #:newID  |
#      | food_name   | Нвй фрукт1 тест | #:name   |
#      | food_type   | FRUIT           | #:type   |
#      | food_exotic | 1               | #:exotic |
#    * удалить запись из таблицы по id = "#:food_id"