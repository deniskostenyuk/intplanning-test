package api.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataForCreation {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Object")
    private String object;
    @JsonProperty("Vals")
    private List<Val> vals;

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public List<Val> getVals() {
        return vals;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setVals(List<Val> vals) {
        this.vals = vals;
    }
}
