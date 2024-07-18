# intplanning-test

Проект содержит набор автоматизированных тестов для проверки API приложения OIS SmartField

### Требования
* Java 18
* [Maven](https://maven.apache.org/)
* Тесты должны запускаться из корпоративной сети, т.к. доступ к приложению извне закрыт

### Конфигурация
Для корректного запуска тестов необходимо передать следующие параметры при запуске JVM

```
-Dwebdriver.starting.url - стартовый URL, на котором будут запускаться тесты
-DuserLogin - логин пользователя для авторизации
-DuserPassword - пароль пользователя для авторизации
```

### Запуск
Для запуска всех тестов выполнить
* `mvn test -Dwebdriver.starting.url=https://YOUR_URL/ -DuserLogin=YOUR_LOGIN -DuserPassword=YOUR_PASSWORD`

Для запуска отдельного класса выполнить
* `mvn test -Dtest=AuthorizationTest -Dwebdriver.starting.url=https://YOUR_URL/ -DuserLogin=YOUR_LOGIN -DuserPassword=YOUR_PASSWORD`
