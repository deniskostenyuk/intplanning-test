package api.administration;

import api.administration.apps.AppsList;
import api.helpers.DataForCreation;
import api.helpers.Val;
import api.administration.users.AuthorizedUser;
import api.administration.users.User;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.*;

import java.util.*;

import static api.Specs.requestSpecWithAuth;
import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Приложения")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppsTest {

    static {
        JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    }

    private static AppsList createdApp;

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
    @DisplayName("Создание приложения с типом Native")
    public void createNativeApp() {

        // формируем body для создания приложения
        String code = String.valueOf(new Date().getTime());
        String appName = "testApp" + code;
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId("");
        bodyData.setObject("application");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("APPCODE", code));
        vals.add(new Val("NAME", appName));
        vals.add(new Val("APP_TYPE", "6"));

        bodyData.setVals(vals);

        // запрос на создание приложения
        given()
                .body(bodyData)
                .when()
                .post("api/Application/SetFormApplication")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия созданного приложения в общем списке приложений
        List<AppsList> appsList = given()
                .when()
                .post("api/Application/MenuItemsForMainMenuFull")
                .then().log().all()
                .extract().body().jsonPath().getList("", AppsList.class);
        boolean appExists = false;
        for (AppsList appData : appsList) {
            if (appData.getName().equals(appName) && appData.getApp_type() == 6.0) {
                createdApp = appData;
                appExists = true;
                break;
            }
        }
        Assertions.assertTrue(appExists, "Created app \"" + appName + "\" is not found in the app list");
    }
    @Test
    @Order(2)
    @Owner("Денис Костенюк")
    @DisplayName("Редактирование приложения")
    public void editAppName() {

        // формируем body для редактирования приложения
        String code = String.valueOf(new Date().getTime());
        String newAppName = createdApp.getName() + "edit";
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId(createdApp.getId());
        bodyData.setObject("application");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("NAME", newAppName));

        bodyData.setVals(vals);

        // запрос на редактирование приложения
        given()
                .body(bodyData)
                .when()
                .post("api/Application/SetFormApplication")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия отредактированного приложения в общем списке приложений
        List<AppsList> appsList = given()
                .when()
                .post("api/Application/MenuItemsForMainMenuFull")
                .then().log().all()
                .extract().body().jsonPath().getList("", AppsList.class);
        boolean appExists = false;
        for (AppsList appData : appsList) {
            if (appData.getName().equals(newAppName) && appData.getApp_type() == 6.0) {
                createdApp = appData;
                appExists = true;
                break;
            }
        }
        Assertions.assertTrue(appExists, "Created app \"" + newAppName + "\" is not found in the app list");
    }

    @Test
    @Order(3)
    @Owner("Денис Костенюк")
    @DisplayName("Удаление приложения")
    public void deleteApp() {

        // запрос на удаление приложения
        given()
                .when()
                .post("api/Application/DelObject/" + createdApp.getId())
                .then().log().all()
                .statusCode(200);

        // проверка отсутствия приложения в общем списке
        List<AppsList> appsList = given()
                .when()
                .post("api/Application/MenuItemsForMainMenuFull")
                .then().log().all()
                .extract().body().jsonPath().getList("", AppsList.class);
        boolean roleExists = false;
        for (AppsList appData : appsList) {
            if (appData.getName().equals(createdApp.getName())) {
                roleExists = true;
                break;
            }
        }
        Assertions.assertFalse(roleExists, "Created app \"" + createdApp.getName() + "\" is not deleted");
    }
}
