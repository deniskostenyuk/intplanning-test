# intplanning-test

Проект содержит набор автоматизированных тестов для проверки API приложения OIS SmartField

### Требования
* Java 17
* [Maven](https://maven.apache.org/)
* Тесты должны запускаться из корпоративной сети, т.к. доступ к приложению извне закрыт

### Конфигурация
Для корректного запуска тестов необходимо передать следующие параметры при запуске JVM

```
-Dwebdriver.starting.url - стартовый URL, на котором будут запускаться тесты
-Duser.login - логин пользователя для авторизации
-Duser.password - пароль пользователя для авторизации
```
Некоторые локальные переменные хранятся в .env файле, который находится в корневом каталоге
и включен в .gitignore

### Запуск
Для запуска всех тестов выполнить
* `mvn test -Dwebdriver.starting.url=https://YOUR_URL/ -Duser.login=YOUR_LOGIN -Duser.password=YOUR_PASSWORD`

Для запуска отдельного класса, например класса авторизации, выполнить
* `mvn test -Dtest=AuthorizationTest -Dwebdriver.starting.url=https://YOUR_URL/ -Duser.login=YOUR_LOGIN -Duser.password=YOUR_PASSWORD`
