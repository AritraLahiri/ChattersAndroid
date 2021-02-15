package aritra.code.chatters.Models;

public class Users {
    private String profilePic, userName, userId, about, phoneNumber, token;
    private UsersStateModel State;

    public Users() {

    }

    public Users(String phoneNumber, String userName, String token) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.token = token;
    }

    public UsersStateModel getState() {
        return State;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getToken() {
        return token;
    }


}
