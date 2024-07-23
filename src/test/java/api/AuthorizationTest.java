package api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import api.users.AuthorizedUser;
import api.users.User;

import static api.helpers.Properties.*;
import static io.restassured.RestAssured.given;

public class AuthorizationTest {

    @Test
    public void successAuth() {
        User user = User.setUser(getStartUrl(), getUserLogin(), getUserPassword());
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

    @Test
    public void unsuccessfulAuthWithInvalidCredentials() {
        User user = User.setUser(getStartUrl(), getUserLogin(), "testPassword");
        AuthorizedUser authorizedUser = given()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess);
        Assertions.assertNull(authorizedUser.accessToken);
        Assertions.assertEquals("Введено неправильное имя пользователя и/или пароль.", authorizedUser.errorMessage);
    }

    @Test
    public void unsuccessfulAuthWithEmptyCredentials() {
        User user = User.setUser(getStartUrl(), "", "");
        AuthorizedUser authorizedUser = given()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess);
        Assertions.assertNull(authorizedUser.accessToken);
        Assertions.assertEquals("Не указано имя пользователя или пароль", authorizedUser.errorMessage);
    }
}
