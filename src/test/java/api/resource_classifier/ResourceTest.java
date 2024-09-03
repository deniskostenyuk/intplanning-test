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
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static api.Specs.requestSpecWithAuth;
import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.helpers.RequestingDataList.getDataForRequestingEnterpriseList;
import static api.helpers.RequestingDataList.getDataForRequestingRolesList;
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
    public void createSystemRole() {

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
        given()
                .body(bodyData)
                .when()
                .post("api/Registry/SaveRec")
                .then().log().all()
                .body("Saved", equalTo(true));

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


}
