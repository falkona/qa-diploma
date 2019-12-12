# Дипломный проект

### Документация

[План автоматизации тестирования](https://github.com/falkona/qa-diploma/blob/master/docs/Plan.md)

[Отчет по итогам тестирования](https://github.com/falkona/qa-diploma/blob/master/docs/Report.md)

[Отчет по итогам автоматизации](https://github.com/falkona/qa-diploma/blob/master/docs/Summary.md)


* Файлы docker-compose находится в корневом каталоге
* SUT, gate-simulator, application.properties находятся в папке [/artifacts](https://github.com/falkona/qa-diploma/tree/master/artifacts)
* В файле [application.properties](https://github.com/falkona/qa-diploma/blob/master/artifacts/application.properties) указан хост 192.168.99.100 для работы с Windows 10 Домашняя
* Реализована поддержка MySQL и Postgres

### Как запускать

#### Для работы с MySQL
1. Запустить контейнеры: MySQL, Node.js
    ```
    docker-compose -f docker-compose-ms.yml up -d 
    ```

2. Запустить SUT
    ```
    java -Dspring.datasource.url=jdbc:mysql://192.168.99.100:3306/app -jar aqa-shop.jar
    ```

3. Запустить тесты
    ```
    gradlew test -Dtest.db.url=jdbc:mysql://192.168.99.100:3306/app
    ```
   
#### Для работы с Postgres
1. Запустить контейнеры: Postgres, Node.js
    ```
    docker-compose -f docker-compose-ps.yml up -d  
    ```

2. Запустить SUT
    ```
    java -Dspring.datasource.url=jdbc:postgresql://192.168.99.100:5432/app -jar aqa-shop.jar
    ```

3. Запустить тесты
    ```
    gradlew test -Dtest.db.url=jdbc:postgresql://192.168.99.100:5432/app
    ```
   
