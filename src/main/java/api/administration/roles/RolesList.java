package api.administration.roles;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class RolesList {

    @JsonProperty("CODE")
    private String code;

    @JsonProperty("NAME")
    private String name;

    @JsonProperty("DESCRIPTION")
    private String description;

    @JsonProperty("ID")
    private double id;

    @JsonProperty("OBJ_TYPE.NAME")
    private String objTypeName;

    public RolesList() {
    }

    public RolesList(String code, String name, String description, double id, String objTypeName) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.id = id;
        this.objTypeName = objTypeName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return BigDecimal.valueOf(id).toPlainString();
    }

    public String getObjTypeName() {
        return objTypeName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(double id) {
        this.id = id;
    }

    public void setObjTypeName(String objTypeName) {
        this.objTypeName = objTypeName;
    }
}
