package aritra.code.chatters.Notifications;

public class Data {
    private String title;
    private String body;
    private String icon;
    private  String color;



    public Data(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Data() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}


