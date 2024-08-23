package api;

import api.users.AuthorizedUser;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public class Specs {

    public static RequestSpecification requestSpec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .log(LogDetail.ALL)
                .addFilter(new AllureRestAssured())
                .build()
                .headers("X-Requested-With", "XMLHttpRequest", "Content-Type", "application/json");
    }

    public static RequestSpecification requestSpecWithAuth(String baseUri, AuthorizedUser authorizedUser) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .log(LogDetail.ALL)
                .addFilter(new AllureRestAssured())
                .build()
                .headers("X-Requested-With", "XMLHttpRequest",
                        "Content-Type", "application/json",
                        "Authorization", "Bearer " + authorizedUser.getAccessToken());
    }
}
