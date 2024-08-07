package api;

import api.users.*;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import java.util.*;

import static api.Specs.requestSpecForUsers;
import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.users.DataForRequestingUserList.getDataForRequestingUserList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Пользователи")
public class UserTest {

    private static UsersList createdUser;

    @BeforeAll
    public static void login() {
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

        RestAssured.requestSpecification = requestSpecForUsers(getStartUrl(), authorizedUser);
    }

    @Test
    @Order(1)
    @Owner("Денис Костенюк")
    @DisplayName("Создание пользователя")
    public void createUser() {

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
                .body(bodyData)
                .when()
                .post("api/Admin/SetFormUser")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия созданного юзера в общем списке юзеров
        List<UsersList> usersList = given()
                .body(getDataForRequestingUserList())
                .when()
                .post("api/Registry/DoSearchViewQuery")
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
    @Owner("Денис Костенюк")
    @DisplayName("Редактирование пользователя")
    public void editUserFio() {

        // формируем body для редактирования юзера
        String code = String.valueOf(new Date().getTime());
        String userFio = createdUser.getFio() + code;
        String userEmail = "testmail@mail.com";
        DataForCreatingUser bodyData = new DataForCreatingUser();
        bodyData.setId(createdUser.getId());
        bodyData.setObject("user");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("FIO", userFio));
        vals.add(new Val("EMAIL", userEmail));
        vals.add(new Val("DEPARTMENT", null));
        bodyData.setVals(vals);

        // запрос на создание юзера
        given()
                .body(bodyData)
                .when()
                .post("api/Admin/SetFormUser")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия созданного юзера в общем списке юзеров
        List<UsersList> usersList = given()
                .body(getDataForRequestingUserList())
                .when()
                .post("api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UsersList.class);
        boolean userExists = false;
        for (UsersList userData : usersList) {
            System.out.println(userData.getLogin());
            if (userData.getFio().equals(userFio) && userData.getEmail().equals(userEmail)) {
                createdUser = userData;
                userExists = true;
                break;
            }
        }
        Assertions.assertTrue(userExists, "Created user \"" + userFio + "\" is not found in the user list");
    }

    @Test
    @Order(3)
    @Owner("Денис Костенюк")
    @DisplayName("Удаление пользователя")
    public void deleteUser() {

        // формируем body для удаления юзера
        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("Id", createdUser.getId());
        bodyData.put("Object", "user");

        // запрос на удаление пользователя
        given()
                .body(bodyData)
                .when()
                .post("api/Registry/DelRec")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка отсутствия юзера в общем списке юзеров
        List<UsersList> usersList = given()
                .body(getDataForRequestingUserList())
                .when()
                .post("api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UsersList.class);
        boolean userExists = false;
        for (UsersList userData : usersList) {
            if (userData.getLogin().equals(createdUser.getLogin()) || userData.getFio().equals(createdUser.getFio())) {
                userExists = true;
                break;
            }
        }
        Assertions.assertFalse(userExists, "Created user \"" + createdUser.getLogin() + "\" is not found in the user list");
    }
}
