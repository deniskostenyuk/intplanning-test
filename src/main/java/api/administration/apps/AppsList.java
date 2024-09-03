package api.administration.apps;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppsList {
    public double id;
    public String name;
    public String text;
    public Object parent_id;
    public double app_type;
    public Object url;
    public double index;
    public boolean hasChildren;
    public boolean leaf;
    public String iconCls;
    public boolean isFav;
    public boolean always_allow;
    public boolean hidden;
    public Object description;
    public boolean disable_user_preview;
    public Object default_preview;
    public boolean has_preview;
    public Object name_en;
    public Object user_settings;
    public Object namePath;
    public Object idPath;
    public Object path;
    public Object appcode;
    public Object appext;
    public Object appProcObjExtUrlKey;

    public AppsList() {
    }

    public AppsList(double id, String name, String text, Object parent_id, double app_type, Object url, double index,
                    boolean hasChildren, boolean leaf, String iconCls, boolean isFav, boolean always_allow,
                    boolean hidden, Object description, boolean disable_user_preview, Object default_preview,
                    boolean has_preview, Object name_en, Object user_settings, Object namePath, Object idPath,
                    Object path, Object appcode, Object appext, Object appProcObjExtUrlKey) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.parent_id = parent_id;
        this.app_type = app_type;
        this.url = url;
        this.index = index;
        this.hasChildren = hasChildren;
        this.leaf = leaf;
        this.iconCls = iconCls;
        this.isFav = isFav;
        this.always_allow = always_allow;
        this.hidden = hidden;
        this.description = description;
        this.disable_user_preview = disable_user_preview;
        this.default_preview = default_preview;
        this.has_preview = has_preview;
        this.name_en = name_en;
        this.user_settings = user_settings;
        this.namePath = namePath;
        this.idPath = idPath;
        this.path = path;
        this.appcode = appcode;
        this.appext = appext;
        this.appProcObjExtUrlKey = appProcObjExtUrlKey;
    }

    public String getId() {
        return BigDecimal.valueOf(id).toPlainString();
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public Object getParent_id() {
        return parent_id;
    }

    public double getApp_type() {
        return app_type;
    }

    public Object getUrl() {
        return url;
    }

    public double getIndex() {
        return index;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public String getIconCls() {
        return iconCls;
    }

    public boolean isFav() {
        return isFav;
    }

    public boolean isAlways_allow() {
        return always_allow;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Object getDescription() {
        return description;
    }

    public boolean isDisable_user_preview() {
        return disable_user_preview;
    }

    public Object getDefault_preview() {
        return default_preview;
    }

    public boolean isHas_preview() {
        return has_preview;
    }

    public Object getName_en() {
        return name_en;
    }

    public Object getUser_settings() {
        return user_settings;
    }

    public Object getNamePath() {
        return namePath;
    }

    public Object getIdPath() {
        return idPath;
    }

    public Object getPath() {
        return path;
    }

    public Object getAppcode() {
        return appcode;
    }

    public Object getAppext() {
        return appext;
    }

    public Object getAppProcObjExtUrlKey() {
        return appProcObjExtUrlKey;
    }
}
