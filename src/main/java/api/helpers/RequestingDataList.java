package api.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestingDataList {

    @JsonProperty("Conditions")
    public Object conditions;
    @JsonProperty("EndDate")
    public Object endDate;
    @JsonProperty("Object")
    public String object;
    @JsonProperty("ProcObj")
    public Object procObj;
    @JsonProperty("PropertiesQueryParameters")
    public Object propertiesQueryParameters;
    @JsonProperty("QueryParameters")
    public Object queryParameters;
    @JsonProperty("StartDate")
    public Object startDate;
    @JsonProperty("ViewName")
    public String viewName;
    public int limit;
    public int page;
    public int start;

    public RequestingDataList(Object conditions, Object endDate, String object, Object procObj,
                              Object propertiesQueryParameters, Object queryParameters, Object startDate,
                              String viewName, int limit, int page, int start) {
        this.conditions = conditions;
        this.endDate = endDate;
        this.object = object;
        this.procObj = procObj;
        this.propertiesQueryParameters = propertiesQueryParameters;
        this.queryParameters = queryParameters;
        this.startDate = startDate;
        this.viewName = viewName;
        this.limit = limit;
        this.page = page;
        this.start = start;
    }

    public RequestingDataList(Object conditions, Object queryParameters, String object, String viewName) {
        this.conditions = conditions;
        this.object = object;
        this.viewName = viewName;
        this.queryParameters = queryParameters;
    }

    public static RequestingDataList getDataForRequestingUserList() {
        return new RequestingDataList(null, null, "user", null,null,
                null, null, "userView", 1000, 1, 0);
    }

    public static RequestingDataList getDataForRequestingRolesList() {
        return new RequestingDataList(null, null, "role", null,null,
                null, null, "EditRole", 1000, 1, 0);
    }

    public static RequestingDataList getDataForRequestingEnterpriseList() {
        return new RequestingDataList(null,
                new HashMap<>(Map.of("@visibility", "0")),
                "merop_executor",
                "nsi");
    }

    public static RequestingDataList getDataForRequestingEventList() {
        HashMap<String, String> configMap = new HashMap<>(Map.of(
                "IdProperty", "ID",
                "ParentIdProperty", "PARENT_ID"
        ));
        return new RequestingDataList(null,
                new HashMap<>(Map.of(
                        "Key", "Tree",
                        "Config", configMap
                        )),
                "merop",
                "nsi");
    }

}
