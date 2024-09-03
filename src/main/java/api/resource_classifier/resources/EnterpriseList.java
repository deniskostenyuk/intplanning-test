package api.resource_classifier.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class EnterpriseList {

    @JsonProperty("ID")
    public double id;

    @JsonProperty("PARENT_ID")
    public double parentId;

    @JsonProperty("CODE")
    public String code;

    @JsonProperty("IS_LEAF")
    public double isLeaf;

    @JsonProperty("NAME")
    public String name;

    @JsonProperty("SPEC_NAME")
    public String specName;

    @JsonProperty("SPEC_SHOP")
    public String specShop;

    @JsonProperty("SPEC_CODE")
    public String specCode;

    @JsonProperty("SF_REF_TYPE")
    public String sfRefType;

    @JsonProperty("IS_PERMANENT")
    public Object isPermanent;

    @JsonProperty("CONTRACT")
    public double contract;

    @JsonProperty("MOBILITY.NAME")
    public Object mobilityName;

    @JsonProperty("WORK_CALENDAR.SHORTNAME")
    public String workCalendarShortname;

    @JsonProperty("WORK_TIME.NAME")
    public String workTimeName;

    public EnterpriseList() {
    }

    public EnterpriseList(double id, double parentId, String code, double isLeaf, String name, String specName,
                          String specShop, String specCode, String sfRefType, Object isPermanent, double contract,
                          Object mobilityName, String workCalendarShortname, String workTimeName) {
        this.id = id;
        this.parentId = parentId;
        this.code = code;
        this.isLeaf = isLeaf;
        this.name = name;
        this.specName = specName;
        this.specShop = specShop;
        this.specCode = specCode;
        this.sfRefType = sfRefType;
        this.isPermanent = isPermanent;
        this.contract = contract;
        this.mobilityName = mobilityName;
        this.workCalendarShortname = workCalendarShortname;
        this.workTimeName = workTimeName;
    }

    public String getId() {
        return BigDecimal.valueOf(id).toPlainString();
    }

    public double getParentId() {
        return parentId;
    }

    public String getCode() {
        return code;
    }

    public double getIsLeaf() {
        return isLeaf;
    }

    public String getName() {
        return name;
    }

    public String getSpecName() {
        return specName;
    }

    public String getSpecShop() {
        return specShop;
    }

    public String getSpecCode() {
        return specCode;
    }

    public String getSfRefType() {
        return sfRefType;
    }

    public Object getIsPermanent() {
        return isPermanent;
    }

    public double getContract() {
        return contract;
    }

    public Object getMobilityName() {
        return mobilityName;
    }

    public String getWorkCalendarShortname() {
        return workCalendarShortname;
    }

    public String getWorkTimeName() {
        return workTimeName;
    }
}
