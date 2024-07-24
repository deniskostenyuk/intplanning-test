package api;

import api.users.AuthorizedUser;
import api.users.User;
import api.users.UsersList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.users.DataForRequestingUsers.getDataForRequestingUsers;
import static io.restassured.RestAssured.given;

public class UserTest {

    @Test
    public void createUser() {
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

        List<UsersList> usersList = given()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken())
                .body(getDataForRequestingUsers())
                .when()
                .post(getStartUrl() + "api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UsersList.class);
    }

}
