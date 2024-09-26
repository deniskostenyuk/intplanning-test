package api.resource_classifier;

import api.administration.users.AuthorizedUser;
import api.administration.users.User;
import api.helpers.DataForCreation;
import api.helpers.Val;
import api.resource_classifier.resources.EnterpriseList;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.*;

import static api.Specs.requestSpecWithAuth;
import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.helpers.RequestingDataList.getDataForRequestingEnterpriseList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Классификатор ресурсов. Ресурсы")
public class ResourceTest {

    static {
        JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    }

    private static EnterpriseList createdEnterprise;

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
    @DisplayName("Создание предприятия")
    public void createEnterprise() {

        // формируем body для создания предприятия
        String code = String.valueOf(new Date().getTime());
        String enterpriseName = "testEnterprise" + code;
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId("");
        bodyData.setObject("merop_executor");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("CODE", code));
        vals.add(new Val("NAME", enterpriseName));
        vals.add(new Val("SF_REF_TYPE", "PR"));
        vals.add(new Val("RETURN_TYPE", "NOT-RETURN"));
        vals.add(new Val("WORK_SCHEDULE.IS_SHIFT_WORK", false));
        vals.add(new Val("WORK_SCHEDULE.IS_EXTRA_CONTRACTOR", false));
        vals.add(new Val("BASE_WELLPAD", null));
        bodyData.setVals(vals);

        // запрос на создание предприятия
        Response response = given()
                .body(bodyData)
                .when()
                .post("api/Registry/SaveRec")
                .then().log().all()
                .extract().response();
        if (response.jsonPath().getMap("$").containsKey("Saved")) {
            boolean isSaved = response.path("Saved");
            if (!isSaved) {
                throw new AssertionError("Failed to save data: " + response.path("Message"));
            }
        } else throw new AssertionError("Failed to save data: " + response.path("Message"));


        // проверка наличия созданного предприятия в общем списке предприятий
        List<EnterpriseList> enterpriseList = given()
                .body(getDataForRequestingEnterpriseList())
                .when()
                .post("Meta/Query")
                .then().log().all()
                .extract().body().jsonPath().getList("", EnterpriseList.class);
        boolean enterpriseExists = false;
        for (EnterpriseList enterpriseData : enterpriseList) {
            if (enterpriseData.getName().equals(enterpriseName) && enterpriseData.getCode().equals(code)) {
                createdEnterprise = enterpriseData;
                enterpriseExists = true;
                break;
            }
        }
        Assertions.assertTrue(enterpriseExists, "Created enterprise \"" + enterpriseName + "\" is not found in the enterprise list");
    }

    @Test
    @Order(2)
    @Owner("Денис Костенюк")
    @DisplayName("Редактирование предприятия")
    public void editEnterpriseName() {

        // формируем body для редактирования предприятия
        String newEnterpriseName = createdEnterprise.getName() + "edit";
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId(createdEnterprise.getId());
        bodyData.setObject("merop_executor");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("NAME", newEnterpriseName));
        vals.add(new Val("RETURN_TYPE", "NOT-RETURN"));
        vals.add(new Val("WORK_SCHEDULE.IS_SHIFT_WORK", 0));
        vals.add(new Val("WORK_SCHEDULE.IS_EXTRA_CONTRACTOR", 0));
        vals.add(new Val("BASE_WELLPAD", null));
        bodyData.setVals(vals);

        // запрос на редактирование предприятия
        given()
                .body(bodyData)
                .when()
                .post("api/Registry/SaveRec")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка наличия отредактированной предприятия в общем списке предприятий
        List<EnterpriseList> enterpriseList = given()
                .body(getDataForRequestingEnterpriseList())
                .when()
                .post("Meta/Query")
                .then().log().all()
                .extract().body().jsonPath().getList("", EnterpriseList.class);
        boolean enterpriseExists = false;
        for (EnterpriseList enterpriseData : enterpriseList) {
            if (enterpriseData.getName().equals(newEnterpriseName)) {
                createdEnterprise = enterpriseData;
                enterpriseExists = true;
                break;
            }
        }
        Assertions.assertTrue(enterpriseExists, "Created enterprise \"" + newEnterpriseName + "\" is not found in the enterprise list");
    }

    @Test
    @Order(3)
    @Owner("Денис Костенюк")
    @DisplayName("Удаление предприятия")
    public void deleteEnterprise() {

        // формируем body для удаления предприятия
        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("Id", createdEnterprise.getId());
        bodyData.put("Object", "merop_executor");

        // запрос на удаление предприятия
        given()
                .body(bodyData)
                .when()
                .post("api/Registry/DelRec")
                .then().log().all()
                .body("Saved", equalTo(true));

        // проверка отсутствия предприятия в общем списке предприятий
        List<EnterpriseList> enterpriseList = given()
                .body(getDataForRequestingEnterpriseList())
                .when()
                .post("Meta/Query")
                .then().log().all()
                .extract().body().jsonPath().getList("", EnterpriseList.class);
        boolean enterpriseExists = false;
        for (EnterpriseList enterpriseData : enterpriseList) {
            if (enterpriseData.getName().equals(createdEnterprise.getName())) {
                enterpriseExists = true;
                break;
            }
        }
        Assertions.assertFalse(enterpriseExists, "Created enterprise \"" + createdEnterprise.getName() + "\" is not deleted");
    }

}
