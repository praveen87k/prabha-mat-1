package prabha.mat.one.app;

public class UserProfile {
    private String userAge;
    private String userName;

    public UserProfile(){

    }

    public UserProfile(String userAge, String userName) {
        this.userAge = userAge;
        this.userName = userName;
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
}
