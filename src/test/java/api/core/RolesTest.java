package api.core;

import api.helpers.DataForCreation;
import api.helpers.Val;
import api.roles.RolesList;
import api.users.AuthorizedUser;
import api.users.User;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.*;

import java.util.*;

import static api.Specs.requestSpecWithAuth;
import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.helpers.RequestingDataList.getDataForRequestingRolesList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Роли")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RolesTest {

    static {
        JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    }

    private static RolesList createdRole;

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
    @DisplayName("Создание роли с системным типом")
    public void createSystemRole() {

        // формируем body для создания роли
        String code = String.valueOf(new Date().getTime());
        String roleName = "testRole" + code;
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId("");
        bodyData.setObject("role");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("CODE", code));
        vals.add(new Val("NAME", roleName));
        vals.add(new Val("OBJ_TYPE", "-1"));

        bodyData.setVals(vals);

        // запрос на создание роли
        given()
                .body(bodyData)
                .when()
                .post("api/Registry/SaveRec")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия созданной роли в общем списке ролей
        List<RolesList> rolesList = given()
                .body(getDataForRequestingRolesList())
                .when()
                .post("api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", RolesList.class);
        boolean roleExists = false;
        for (RolesList roleData : rolesList) {
            if (roleData.getName().equals(roleName) && roleData.getObjTypeName().equals("Системная")) {
                createdRole = roleData;
                roleExists = true;
                break;
            }
        }
        Assertions.assertTrue(roleExists, "Created role \"" + roleName + "\" is not found in the role list");
    }

    @Test
    @Order(2)
    @Owner("Денис Костенюк")
    @DisplayName("Редактирование роли")
    public void editRoleNameAndDescription() {

        // формируем body для редактирования роли
        String code = String.valueOf(new Date().getTime());
        String newRoleName = createdRole.getName() + "edit";
        String description = "test edit " + code;
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId(createdRole.getId());
        bodyData.setObject("role");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("NAME", newRoleName));
        vals.add(new Val("DESCRIPTION", description));

        bodyData.setVals(vals);

        // запрос на редактирование роли
        given()
                .body(bodyData)
                .when()
                .post("api/Registry/SaveRec")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия отредактированной роли в общем списке ролей
        List<RolesList> rolesList = given()
                .body(getDataForRequestingRolesList())
                .when()
                .post("api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", RolesList.class);
        boolean roleExists = false;
        for (RolesList roleData : rolesList) {
            System.out.println(roleData.getName());
            if (roleData.getName().equals(newRoleName) && roleData.getDescription().equals(description)) {
                createdRole = roleData;
                roleExists = true;
                break;
            }
        }
        Assertions.assertTrue(roleExists, "Edited role \"" + newRoleName + "\" is not found in the role list");
    }

    @Test
    @Order(3)
    @Owner("Денис Костенюк")
    @DisplayName("Удаление роли")
    public void deleteRole() {

        // формируем body для удаления роли
        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("Id", createdRole.getId());
        bodyData.put("Object", "role");

        // запрос на удаление роли
        given()
                .body(bodyData)
                .when()
                .post("api/Registry/DelRec")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка отсутствия роли в общем списке ролей
        List<RolesList> rolesList = given()
                .body(getDataForRequestingRolesList())
                .when()
                .post("api/Registry/DoSearchViewQuery")
                .then().log().all()
                .extract().body().jsonPath().getList("data", RolesList.class);
        boolean roleExists = false;
        for (RolesList roleData : rolesList) {
            if (roleData.getName().equals(createdRole.getName()) || roleData.getCode().equals(createdRole.getCode())) {
                roleExists = true;
                break;
            }
        }
        Assertions.assertFalse(roleExists, "Created role \"" + createdRole.getName() + "\" is not deleted");
    }
}

