package api;

import api.users.AuthorizedUser;
import api.users.User;
import api.users.UsersList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static io.restassured.RestAssured.given;

public class UserTest {

    @Test
    public void createUser() {
//        User user = User.setUser(getStartUrl(), getUserLogin(), getUserPassword());
//        List<UsersList> usersList = given()
//                .headers()


//        AuthorizedUser authorizedUser = given()
//                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
//                .body(user)
//                .when()
//                .post(getStartUrl() + "core/auth/login")
//                .then().log().all()
//                .extract().as(AuthorizedUser.class);
//        Assertions.assertTrue(authorizedUser.isSuccess());
//        Assertions.assertNotNull(authorizedUser.getAccessToken());
    }

}
