package prabha.mat.one.app.matches;

public class MatchesObject {
    private String userId;
    private String userName;
    private String userAge;
    private String userLocation;
    private String profileImageUrl;

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

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public MatchesObject (String userId, String userName, String profileImageUrl,
                          String userAge, String userLocation){
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userLocation = userLocation;
        this.profileImageUrl = profileImageUrl;
    }
    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }
}
