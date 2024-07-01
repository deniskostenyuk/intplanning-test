package users;

import com.fasterxml.jackson.annotation.JsonProperty;


public class AuthorizedUser {
    @JsonProperty("UserId")
    public String userId;
    @JsonProperty("AccessToken")
    public String accessToken;
    @JsonProperty("IsSuccess")
    public boolean isSuccess;
    @JsonProperty("IsUserLocked")
    public boolean isUserLocked;
    @JsonProperty("ErrorMessage")
    public Object errorMessage;

    public AuthorizedUser() {

    }

    public AuthorizedUser(String userId, String accessToken, boolean isSuccess, boolean isUserLocked, Object errorMessage) {
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
