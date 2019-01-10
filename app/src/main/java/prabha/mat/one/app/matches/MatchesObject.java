package prabha.mat.one.app.matches;

public class MatchesObject {
    private String userId;
    private String userName;
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

    public MatchesObject (String userId, String userName, String profileImageUrl){
        this.userId = userId;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
    }
    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userId;
    }
}
