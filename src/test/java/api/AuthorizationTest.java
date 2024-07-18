package api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import api.users.AuthorizedUser;
import api.users.User;

import static api.helpers.Properties.getStartUrl;
import static io.restassured.RestAssured.given;

public class AuthorizationTest {

    @Test
    public void successAuth() {
        User user = User.setUser(getStartUrl());
        AuthorizedUser authorizedUser = given()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertTrue(authorizedUser.isSuccess);
        Assertions.assertNotNull(authorizedUser.accessToken);
    }
}
