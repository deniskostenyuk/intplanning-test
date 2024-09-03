package api.administration.users;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AuthorizedUser {
    @JsonProperty("UserId")
    private String userId;
    @JsonProperty("AccessToken")
    private String accessToken;
    @JsonProperty("IsSuccess")
    private boolean isSuccess;
    @JsonProperty("IsUserLocked")
    private boolean isUserLocked;
    @JsonProperty("ErrorMessage")
    private String errorMessage;

    public AuthorizedUser() {

    }

    public AuthorizedUser(String userId, String accessToken, boolean isSuccess, boolean isUserLocked, String errorMessage) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.isSuccess = isSuccess;
        this.isUserLocked = isUserLocked;
        this.errorMessage = errorMessage;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isUserLocked() {
        return isUserLocked;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }
}
