package api;

import api.users.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.*;

import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.users.DataForRequestingUserList.getDataForRequestingUserList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserTest {

    private static UsersList createdUser;
//    private AuthorizedUser authorizedUser;
//
//    @BeforeAll
//    public void setup() {
//        authorizedUser = ;  // Логика авторизации
//    }

    @Test
    @Order(1)
    public void createUser() {
        // авторизуемся и получаем токен
        User user = User.setUser(getStartUrl(), getUserLogin(), getUserPassword());
        AuthorizedUser authorizedUser = given()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then()
                .extract().as(AuthorizedUser.class);
        Assertions.assertTrue(authorizedUser.isSuccess());
        Assertions.assertNotNull(authorizedUser.getAccessToken());

        // формируем body для создания юзера
        String code = String.valueOf(new Date().getTime());
        String userName = "testUser" + code;
        DataForCreatingUser bodyData = new DataForCreatingUser();
        bodyData.setId("");
        bodyData.setObject("user");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("FIO", userName));
        vals.add(new Val("LOGIN", userName));
        vals.add(new Val("PASSWORD", "testUser@" + code));
        vals.add(new Val("DEPARTMENT", null));
        bodyData.setVals(vals);

        // запрос на создание юзера
        given()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken())
                .body(bodyData)
                .when()
                .post(getStartUrl() + "api/Admin/SetFormUser")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия созданного юзера в общем списке юзеров
        List<UsersList> usersList = given()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken())
                .body(getDataForRequestingUserList())
                .when()
                .post(getStartUrl() + "api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UsersList.class);
        boolean userExists = false;
        for (UsersList userData : usersList) {
            System.out.println(userData.getLogin());
            if (userData.getLogin().equals(userName) && userData.getFio().equals(userName)) {
                createdUser = userData;
                userExists = true;
                break;
            }
        }
        Assertions.assertTrue(userExists, "Created user \"" + userName + "\" is not found in the user list");
    }

    @Test
    @Order(2)
    public void deleteUser() {
        // авторизуемся и получаем токен
        User user = User.setUser(getStartUrl(), getUserLogin(), getUserPassword());
        AuthorizedUser authorizedUser = given()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then()
                .extract().as(AuthorizedUser.class);
        Assertions.assertTrue(authorizedUser.isSuccess());
        Assertions.assertNotNull(authorizedUser.getAccessToken());

        // формируем body для удаления юзера
        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("Id", createdUser.getId());  // Убедитесь, что используете правильный тип данных
        bodyData.put("Object", "user");

        // запрос на удаление пользователя
        given()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken())
                .body(bodyData)
                .when()
                .post(getStartUrl() + "api/Registry/DelRec")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка отсутствия юзера в общем списке юзеров
        List<UsersList> usersList = given()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken())
                .body(getDataForRequestingUserList())
                .when()
                .post(getStartUrl() + "api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UsersList.class);
        boolean userExists = false;
        for (UsersList userData : usersList) {
            if (userData.getLogin().equals(createdUser.getLogin()) && userData.getFio().equals(createdUser.getFio())) {
                userExists = true;
                break;
            }
        }
        Assertions.assertFalse(userExists, "Created user \"" + createdUser.getLogin() + "\" is not found in the user list");
    }
}
