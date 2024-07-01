package helpers;

public class Properties {

    public static String getStartUrl() {
        return System.getProperty("webdriver.starting.url");
    }

    public static String getUserLogin() {
        return System.getProperty("userLogin");
    }

    public static String getUserPassword() {
        return System.getProperty("userPassword");
    }

}
