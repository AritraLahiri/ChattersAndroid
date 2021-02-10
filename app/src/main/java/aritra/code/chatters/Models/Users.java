package aritra.code.chatters.Models;

public class Users {
    private String profilePic, userMail, userName, userPass, userId, lastMessage, about, phoneNumber, token;
    private UsersStateModel State;


    public Users(String profilePic, String userMail, String userName, String userPass, String userId, String lastMessage, String about) {
        this.profilePic = profilePic;
        this.userMail = userMail;
        this.userName = userName;
        this.userPass = userPass;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.about = about;
    }

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

    public void setState(UsersStateModel state) {
        State = state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
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

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
