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
      | food_name   | Нвй фрукт1 тест |
      | food_type   | FRUIT           |
      | food_exotic | 1               |
#    * cохранить значение "id" в DataManager
    * получить из таблицы данные о добавленном товаре
#    * сохранить данные выборки в DataManager
    * сверить корректность данных:
      | column      | standard        |
      | food_id     | #:generatedId   |
      | food_name   | Нвй фрукт1 тест |
      | food_type   | FRUIT           |
      | food_exotic | 1               |

#      | column      | standard        | data     |
#      | food_id     | #:generatedId   | #:newID  |
#      | food_name   | Нвй фрукт1 тест | #:name   |
#      | food_type   | FRUIT           | #:type   |
#      | food_exotic | 1               | #:exotic |
    * удалить запись из таблицы по id = "#:food_id"