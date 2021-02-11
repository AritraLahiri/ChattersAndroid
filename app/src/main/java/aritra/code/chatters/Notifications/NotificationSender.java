package aritra.code.chatters.Notifications;

public class NotificationSender {

    private Data notification;
    private String to;


    public NotificationSender(Data notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public NotificationSender() {
    }

    public Data getData() {
        return notification;
    }

    public void setData(Data notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
