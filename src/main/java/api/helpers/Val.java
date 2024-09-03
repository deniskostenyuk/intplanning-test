package api.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Val {
    @JsonProperty("Property")
    private String property;
    @JsonProperty("Value")
    private Object value;

    public Val(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public Object getValue() {
        return value;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
