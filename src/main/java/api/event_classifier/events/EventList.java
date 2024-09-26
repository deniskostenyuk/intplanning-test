package api.event_classifier.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventList {

    @JsonProperty("ID")
    public double id;

    @JsonProperty("PARENT_ID")
    public Object parentId;

    @JsonProperty("IS_LEAF")
    public double isLeaf;

    @JsonProperty("SHORTNAME")
    public String shortName;

    @JsonProperty("CODE")
    public String code;

    @JsonProperty("TYPE")
    public String type;

    @JsonProperty("NAME")
    public String name;

    @JsonProperty("WELLSTOP")
    public double wellStop;

    @JsonProperty("PRIORITY")
    public Object priority;

    @JsonProperty("Id")
    public String secondId;

    @JsonProperty("id")
    public String thirdId;

    public ArrayList<Object> children;

    @JsonProperty("ECONOMIC_CS.NAME")
    public Object economicCsName;

    @JsonProperty("ACTIVITY_TEMPLATE.BEHAVIOR_ID.CODE")
    public Object activityTemplateBehaviorIdCode;

    @JsonProperty("ACTIVITY_TEMPLATE.BEHAVIOR_ID.NAME")
    public Object activityTemplateBehaviorIdName;

    @JsonProperty("BP.SHORTNAME")
    public Object bpShortName;

    @JsonProperty("FPLAN.SHORTNAME")
    public Object fplanShortName;

    @JsonProperty("TYPE.SHORTNAME")
    public String typeShortName;

    public boolean leaf;

    public EventList() {
    }

    public EventList(double id, Object parentId, double isLeaf, String shortName, String code, String type, String name,
                     double wellStop, Object priority, String secondId, String thirdId, ArrayList<Object> children,
                     Object economicCsName, Object activityTemplateBehaviorIdCode, Object activityTemplateBehaviorIdName,
                     Object bpShortName, Object fplanShortName, String typeShortName, boolean leaf) {
        this.id = id;
        this.parentId = parentId;
        this.isLeaf = isLeaf;
        this.shortName = shortName;
        this.code = code;
        this.type = type;
        this.name = name;
        this.wellStop = wellStop;
        this.priority = priority;
        this.secondId = secondId;
        this.thirdId = thirdId;
        this.children = children;
        this.economicCsName = economicCsName;
        this.activityTemplateBehaviorIdCode = activityTemplateBehaviorIdCode;
        this.activityTemplateBehaviorIdName = activityTemplateBehaviorIdName;
        this.bpShortName = bpShortName;
        this.fplanShortName = fplanShortName;
        this.typeShortName = typeShortName;
        this.leaf = leaf;
    }

    public String getId() {
        return BigDecimal.valueOf(id).toPlainString();
    }

    public Object getParentId() {
        return parentId;
    }

    public double getIsLeaf() {
        return isLeaf;
    }

    public String getShortName() {
        return shortName;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getWellStop() {
        return wellStop;
    }

    public Object getPriority() {
        return priority;
    }

    public String getSecondId() {
        return secondId;
    }

    public String getThirdId() {
        return thirdId;
    }

    public ArrayList<Object> getChildren() {
        return children;
    }

    public Object getEconomicCsName() {
        return economicCsName;
    }

    public Object getActivityTemplateBehaviorIdCode() {
        return activityTemplateBehaviorIdCode;
    }

    public Object getActivityTemplateBehaviorIdName() {
        return activityTemplateBehaviorIdName;
    }

    public Object getBpShortName() {
        return bpShortName;
    }

    public Object getFplanShortName() {
        return fplanShortName;
    }

    public String getTypeShortName() {
        return typeShortName;
    }

    public boolean isLeaf() {
        return leaf;
    }
}
