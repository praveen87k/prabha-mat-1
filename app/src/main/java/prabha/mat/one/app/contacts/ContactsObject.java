package prabha.mat.one.app.contacts;

public class ContactsObject {

    private String userId;
    private String userName;
    private String userAge;
    private String userPhoneNumber;
    private String profileImageUrl;
    private String requestType;

    public ContactsObject(String userId, String userName, String userAge,
                          String profileImageUrl, String requestType, String userPhoneNumber) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.profileImageUrl = profileImageUrl;
        this.requestType = requestType;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserAge() {
        return userAge;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
