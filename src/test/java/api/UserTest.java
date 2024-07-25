package api;

import api.users.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.users.DataForRequestingUsers.getDataForRequestingUsers;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserTest {

    @Test
    public void createUser() {
        //авторизуемся и получаем токен
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

        //формируем body для создания юзера
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

        //запрос на создание юзера
        given()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken())
                .body(bodyData)
                .when()
                .post(getStartUrl() + "api/Admin/SetFormUser")
                .then().log().all()
                .body("Saved", equalTo(true));

        //проверка наличия созданного юзера в общем списке юзеров
        List<UsersList> usersList = given()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken())
                .body(getDataForRequestingUsers())
                .when()
                .post(getStartUrl() + "api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UsersList.class);
        boolean userExists = false;
        for (UsersList userData : usersList) {
            System.out.println(userData.getLogin());
            if (userData.getLogin().equals(userName) && userData.getFio().equals(userName)) {
                userExists = true;
                break;
            }
        }
        Assertions.assertTrue(userExists, "Created user \"" + userName + "\" is not found in the user list");
    }
}
