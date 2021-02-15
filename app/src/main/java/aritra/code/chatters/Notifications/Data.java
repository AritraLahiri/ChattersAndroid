package aritra.code.chatters.Notifications;

public class Data {
    private String title;
    private String body;
    private String icon;
    private String color;


    public Data(String title, String body) {
        this.title = title;
        this.body = body;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}


