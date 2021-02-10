package aritra.code.chatters.Notifications;

public class Data {
    private String Title;
    private String Message;

    public Data(String Title, String Message) {
        this.Title = Title;
        this.Message = Message;
    }

    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
