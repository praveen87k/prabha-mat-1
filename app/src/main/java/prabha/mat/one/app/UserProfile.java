package prabha.mat.one.app;

public class UserProfile {
    private String userAge;
    private String userName;
    private String userGender;
    private String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public UserProfile(){

    }

    public UserProfile(String userAge, String userName, String userGender, String profileImageUrl) {
        this.userAge = userAge;
        this.userName = userName;
        this.userGender = userGender;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
