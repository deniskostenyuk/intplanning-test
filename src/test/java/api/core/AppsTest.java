package api.core;

import api.helpers.DataForCreation;
import api.helpers.Val;
import api.users.AuthorizedUser;
import api.users.User;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        // формируем body для создания роли
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

        // запрос на создание роли
        given()
                .body(bodyData)
                .when()
                .post("api/Application/SetFormApplication")
                .then().log().all()
                .body("Saved", equalTo(true));

//        // проверка наличия созданной роли в общем списке ролей
//        List<RolesList> rolesList = given()
//                .body(getDataForRequestingRolesList())
//                .when()
//                .post("api/Registry/DoSearchViewQuery")
//                .then().log().all()
//                .extract().body().jsonPath().getList("data", RolesList.class);
//        boolean roleExists = false;
//        for (RolesList roleData : rolesList) {
//            if (roleData.getName().equals(roleName) && roleData.getObjTypeName().equals("Системная")) {
//                createdRole = roleData;
//                roleExists = true;
//                break;
//            }
//        }
//        Assertions.assertTrue(roleExists, "Created role \"" + roleName + "\" is not found in the role list");
    }



}
