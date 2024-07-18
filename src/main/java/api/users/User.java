package api.users;


import java.util.HashMap;
import java.util.Map;

import static api.helpers.Properties.getUserLogin;
import static api.helpers.Properties.getUserPassword;

public class User {
    public String locale;
    public String userId;
    public String userPwd;
    public String context;

    private static final String URL_LUK_IFIELD = "https://web-luk-ifield-nightly.okd.asuproject.ru/";
    private static final String URL_LUK_UFAM = "https://web-luk-ufam-dev.okd.asuproject.ru/";
    private static final String URL_GPN = "https://web-gpn-ufam-nightly.okd.asuproject.ru/";
    private static final String URL_BEL = "https://web-bel-ufam-nightly.okd.asuproject.ru/";

    private static final Map<String, String> CONTEXT_MAP = new HashMap<>();

    static {
        CONTEXT_MAP.put(URL_LUK_IFIELD, "LUKOILALL");
        CONTEXT_MAP.put(URL_LUK_UFAM, "LUKOIL_SAP");
        CONTEXT_MAP.put(URL_GPN, "GPN");
        CONTEXT_MAP.put(URL_BEL, "BelOil");
    }
    public User(String locale, String userId, String userPwd, String context) {
        this.locale = locale;
        this.userId = userId;
        this.userPwd = userPwd;
        this.context = context;
    }

    public static User setUser(String baseUrl) {
        String context = CONTEXT_MAP.get(baseUrl);
        if (context != null) {
            return new User("ru", getUserLogin(), getUserPassword(), context);
        } else throw new IllegalArgumentException("Invalid URL: " + baseUrl);
    }
}
