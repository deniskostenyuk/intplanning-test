package api.administration;

import api.administration.users.AuthorizedUser;
import api.administration.users.User;
import api.administration.users.UsersList;
import api.helpers.DataForCreation;
import api.helpers.Val;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.*;

import java.util.*;

import static api.Specs.requestSpecWithAuth;
import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.helpers.RequestingDataList.getDataForRequestingUserList;
import static api.administration.users.User.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Пользователи")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    static {
        JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    }

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

        RestAssured.requestSpecification = requestSpecWithAuth(getStartUrl(), authorizedUser);
    }

    @Test
    @Order(1)
    @Owner("Денис Костенюк")
    @DisplayName("Создание пользователя")
    public void createUser() {

        // формируем body для создания юзера
        String code = String.valueOf(new Date().getTime());
        String userName = "testUser" + code;
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId("");
        bodyData.setObject("user");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("LOGIN", userName));
        vals.add(new Val("PASSWORD", "testUser@" + code));
        if (getStartUrl().equals(URL_LUK_IFIELD) || getStartUrl().equals(URL_LUK_UFAM)) {
            vals.add(new Val("FIO", userName));
        }
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
    public void editUserFioAndEmail() {

        // формируем body для редактирования юзера
        String code = String.valueOf(new Date().getTime());
        String userLogin = createdUser.getLogin() + code;
        String userFio = createdUser.getFio() + code;
        String userEmail = "testmail@mail.com";
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId(createdUser.getId());
        bodyData.setObject("user");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("LOGIN", userLogin));

        if (!getStartUrl().equals(URL_GPN)) {
            vals.add(new Val("FIO", userFio));
            vals.add(new Val("EMAIL", userEmail));
            vals.add(new Val("DEPARTMENT", null));
        }

        bodyData.setVals(vals);

        // запрос на редактирование юзера
        given()
                .body(bodyData)
                .when()
                .post("api/Admin/SetFormUser")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия отредактированного юзера в общем списке юзеров
        List<UsersList> usersList = given()
                .body(getDataForRequestingUserList())
                .when()
                .post("api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UsersList.class);
        boolean userExists = false;
        for (UsersList userData : usersList) {
            System.out.println(userData.getLogin());
            if (userData.getLogin().equals(userLogin)) {
                createdUser = userData;
                userExists = true;
                if (!getStartUrl().equals(URL_GPN)) {
                    userExists = false;
                    if (userData.getFio().equals(userFio) && userData.getEmail().equals(userEmail)) {
                        createdUser = userData;
                        userExists = true;
                        break;
                    }
                }
                break;
            }
        }
        Assertions.assertTrue(userExists, "Edited user \"" + userFio + "\" is not found in the user list");
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
