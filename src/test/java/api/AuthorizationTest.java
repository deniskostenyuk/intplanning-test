package api;

import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import api.users.AuthorizedUser;
import api.users.User;

import static api.Specs.requestSpec;
import static api.helpers.Properties.*;
import static io.restassured.RestAssured.given;

@DisplayName("Авторизация")
public class AuthorizationTest {

    @BeforeAll
    public static void setSpec() {
        RestAssured.requestSpecification = requestSpec(getStartUrl());
    }

    @Test
    @Epic("Авторизация")
    @Owner("Денис Костенюк")
    @DisplayName("Проверка успешной авторизации")
    public void successAuth() {
        User user = User.setUser(getStartUrl(), getUserLogin(), getUserPassword());
        AuthorizedUser authorizedUser = given()
                .body(user)
                .when()
                .post("core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertTrue(authorizedUser.isSuccess());
        Assertions.assertNotNull(authorizedUser.getAccessToken());
    }

    @Test
    @Epic("Авторизация")
    @Owner("Денис Костенюк")
    @DisplayName("Проверка неуспешной авторизации с неправильными учетными данными")
    public void unsuccessfulAuthWithInvalidCredentials() {
        User user = User.setUser(getStartUrl(), getUserLogin(), "testPassword");
        AuthorizedUser authorizedUser = given()
                .body(user)
                .when()
                .post("core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Введено неправильное имя пользователя и/или пароль.", authorizedUser.getErrorMessage());
    }

    @Test
    @Epic("Авторизация")
    @Owner("Денис Костенюк")
    @DisplayName("Проверка неуспешной авторизации с пустыми учетными данными")
    public void unsuccessfulAuthWithEmptyCredentials() {
        User user = User.setUser(getStartUrl(), "", "");
        AuthorizedUser authorizedUser = given()
                .body(user)
                .when()
                .post("core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Не указано имя пользователя или пароль", authorizedUser.getErrorMessage());
    }

    @Test
    @Epic("Авторизация")
    @Owner("Денис Костенюк")
    @DisplayName("Проверка неуспешной авторизации у заблокированного пользователя")
    public void unsuccessfulAuthWithBlockedUser() {
        User user = User.setUser(getStartUrl(), "testBlock", "testBlock2@1");
        AuthorizedUser authorizedUser = given()
                .body(user)
                .when()
                .post("core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Пользователь testBlock заблокирован. Для разблокировки обратитесь к администратору ресурса.", authorizedUser.getErrorMessage());
    }

    @Test
    @Epic("Авторизация")
    @Owner("Денис Костенюк")
    @DisplayName("Проверка неуспешной авторизации у удаленного пользователя")
    public void unsuccessfulAuthWithDeletedUser() {
        User user = User.setUser(getStartUrl(), "testDelete", "testDelete2@1");
        AuthorizedUser authorizedUser = given()
                .body(user)
                .when()
                .post("core/auth/login")
                .then().log().all()
                .extract().as(AuthorizedUser.class);
        Assertions.assertFalse(authorizedUser.isSuccess());
        Assertions.assertNull(authorizedUser.getAccessToken());
        Assertions.assertEquals("Пользователь testDelete удалён. Для восстановления обратитесь к администратору ресурса.", authorizedUser.getErrorMessage());
    }
}
