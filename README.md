# Дипломный проект

* Файл docker-compose.yml находится в корневом каталоге
* SUT, gate-simulator, application.properties находятся в папке /artifacts
* В файле application.properties указан хост 192.168.99.100 для работы с Windows 10 Домашняя
* Пока что реализована только поддержка MySQL

### Как запускать
1. В корневом каталоге запустить контейнеры: MySQL, Node.js, Postgres
    ```
    docker-compose up -d
    ```

2. В каталоге /artifacts запустить SUT
    ```
    java -jar aqa-shop.jar
    ```

3. В корневом каталоге запустить тесты
    ```
    gradlew test
    ```