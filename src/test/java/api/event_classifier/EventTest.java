package api.event_classifier;

import api.administration.users.AuthorizedUser;
import api.administration.users.User;
import api.event_classifier.events.EventList;
import api.helpers.DataForCreation;
import api.helpers.Val;
import api.resource_classifier.resources.EnterpriseList;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static api.Specs.requestSpecWithAuth;
import static api.helpers.Properties.*;
import static api.helpers.Properties.getStartUrl;
import static api.helpers.RequestingDataList.getDataForRequestingEnterpriseList;
import static api.helpers.RequestingDataList.getDataForRequestingEventList;
import static io.restassured.RestAssured.given;

@DisplayName("Классификатор мероприятий. Мероприятия")
public class EventTest {

    static {
        JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    }

    private static EventList createdEvent;

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
    public void createMainEvent() {

        // формируем body для создания мероприятия
        String code = String.valueOf(new Date().getTime());
        String eventName = "testEvent" + code;
        String eventShortName = "shortEvent" + code;
        DataForCreation bodyData = new DataForCreation();
        bodyData.setId("");
        bodyData.setObject("merop");
        List<Val> vals = new ArrayList<>();
        vals.add(new Val("FORM_ACTION", "ADD"));
        vals.add(new Val("TYPE", code));
        vals.add(new Val("CODE", code));
        vals.add(new Val("SHORTNAME", eventShortName));
        vals.add(new Val("NAME", eventName));
        bodyData.setVals(vals);

        // запрос на создание мероприятия
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


        // проверка наличия созданного мероприятия в общем списке мероприятий
        List<EventList> eventLists = given()
                .body(getDataForRequestingEventList())
                .when()
                .post("Meta/Query")
                .then().log().all()
                .extract().body().jsonPath().getList("", EventList.class);
        boolean enterpriseExists = false;
        for (EventList eventData : eventLists) {
            if (eventData.getName().equals(eventName) && eventData.getCode().equals(code)) {
                createdEvent = eventData;
                enterpriseExists = true;
                break;
            }
        }
        Assertions.assertTrue(enterpriseExists, "Created enterprise \"" + eventName + "\" is not found in the enterprise list");
    }
}
