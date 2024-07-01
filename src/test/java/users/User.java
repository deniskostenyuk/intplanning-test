package users;


public class User {
    public String locale;
    public String userId;
    public String userPwd;
    public String context;

    public User(String locale, String userId, String userPwd, String context) {
        this.locale = locale;
        this.userId = userId;
        this.userPwd = userPwd;
        this.context = context;
    }
}
