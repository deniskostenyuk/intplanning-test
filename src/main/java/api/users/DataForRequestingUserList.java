package api.users;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataForRequestingUserList {

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

    public DataForRequestingUserList(Object conditions, Object endDate, String object, Object procObj,
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

    public static DataForRequestingUserList getDataForRequestingUserList() {
        return new DataForRequestingUserList(null, null, "user", null,null,
                null, null, "userView", 1000, 1, 0);
    }
}
