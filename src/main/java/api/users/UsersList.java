package api.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class UsersList {

    @JsonProperty("FIO")
    private String fio;

    @JsonProperty("LOGIN")
    private String login;

    @JsonProperty("EMAIL")
    private String email;

    @JsonProperty("DISABLED")
    private double disabled;

    @JsonProperty("DESCRIPTION")
    private String description;

    @JsonProperty("POSITION")
    private Object position;

    @JsonProperty("ID")
    private BigDecimal id;

    @JsonProperty("DEPARTMENT.NAME")
    private Object departmentName;

    @JsonProperty("ENTERPRISE.NAME")
    private Object enterpriseNAME;

    @JsonProperty("SBS_USER_EXTEND.AUTH_TYPE")
    private String sbsUserExtendAuthType;

    public UsersList() {
    }

    public UsersList(String fio, String login, String email, double disabled, String description, Object position,
                     BigDecimal id, Object departmentName, Object enterpriseNAME, String sbsUserExtendAuthType) {
        this.fio = fio;
        this.login = login;
        this.email = email;
        this.disabled = disabled;
        this.description = description;
        this.position = position;
        this.id = id;
        this.departmentName = departmentName;
        this.enterpriseNAME = enterpriseNAME;
        this.sbsUserExtendAuthType = sbsUserExtendAuthType;
    }

    public String getFio() {
        return fio;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public double getDisabled() {
        return disabled;
    }

    public String getDescription() {
        return description;
    }

    public Object getPosition() {
        return position;
    }

    public BigDecimal getId() {
        return id;
    }

    public Object getDepartmentName() {
        return departmentName;
    }

    public Object getEnterpriseNAME() {
        return enterpriseNAME;
    }

    public String getSbsUserExtendAuthType() {
        return sbsUserExtendAuthType;
    }
}
