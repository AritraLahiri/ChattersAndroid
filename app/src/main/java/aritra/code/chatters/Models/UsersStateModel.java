package aritra.code.chatters.Models;

public class UsersStateModel {
    private String state;
    private String date;
    private String time;

    public UsersStateModel() {
    }

    public String getState() {
        return state;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
