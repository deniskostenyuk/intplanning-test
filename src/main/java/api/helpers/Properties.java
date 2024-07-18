package api.helpers;

public class Properties {

    public static String getStartUrl() {
        return System.getProperty("webdriver.starting.url");
    }

    public static String getUserLogin() {
        return System.getProperty("user.login");
    }

    public static String getUserPassword() {
        return System.getProperty("user.password");
    }

}
