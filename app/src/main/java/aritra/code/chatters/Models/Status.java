package aritra.code.chatters.Models;

import java.util.ArrayList;

public class Status {

    String profileImage, statusId, userName, phoneNumber, userId;
    Long lastUpdated;
    ArrayList<StatusData> statuses;


    public Status(String profileImage, String userName, Long lastUpdated, ArrayList<StatusData> statuses, String phoneNumber) {
        this.profileImage = profileImage;
        this.userName = userName;
        this.lastUpdated = lastUpdated;
        this.statuses = statuses;
        this.phoneNumber = phoneNumber;
    }

    public Status() {
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<StatusData> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<StatusData> statuses) {
        this.statuses = statuses;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
