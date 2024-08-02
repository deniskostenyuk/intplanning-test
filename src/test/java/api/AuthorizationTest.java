package api;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.restassured.AllureRestAssured;
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
        Assertions.assertTrue(authorizedUser.isSuccess());
        Assertions.assertNotNull(authorizedUser.getAccessToken());
    }

    @Test
    @Owner("Денис Костенюк")
    @Description("Проверка неуспешной авторизации с неправильными учетными данными")
    public void unsuccessfulAuthWithInvalidCredentials() {
        User user = User.setUser(getStartUrl(), getUserLogin(), "testPassword");
        AuthorizedUser authorizedUser = given()
                .filter(new AllureRestAssured())
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Введено неправильное имя пользователя и/или пароль.", authorizedUser.getErrorMessage());
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
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Не указано имя пользователя или пароль", authorizedUser.getErrorMessage());
    }

    @Test
    public void unsuccessfulAuthWithBlockedUser() {
        User user = User.setUser(getStartUrl(), "testBlock", "testBlock2@1");
        AuthorizedUser authorizedUser = given()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Пользователь testBlock заблокирован. Для разблокировки обратитесь к администратору ресурса.", authorizedUser.getErrorMessage());
    }

    @Test
    public void unsuccessfulAuthWithDeletedUser() {
        User user = User.setUser(getStartUrl(), "testDelete", "testDelete2@1");
        AuthorizedUser authorizedUser = given()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json")
                .body(user)
                .when()
                .post(getStartUrl() + "core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Пользователь testDelete удалён. Для восстановления обратитесь к администратору ресурса.", authorizedUser.getErrorMessage());
    }
}
