package prabha.mat.one.app;

public class UserProfile {
    private String userAge;
    private String userName;
    private String userGender;
    private String userPhone;
    private String userLocation;
    private String profileImageUrl;

    public UserProfile(){

    }

    public UserProfile(String userAge, String userName, String userGender, String profileImageUrl,
                                       String userPhone, String userLocation) {
        this.userAge = userAge;
        this.userName = userName;
        this.userGender = userGender;
        this.profileImageUrl = profileImageUrl;
        this.userPhone = userPhone;
        this.userLocation = userLocation;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
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
