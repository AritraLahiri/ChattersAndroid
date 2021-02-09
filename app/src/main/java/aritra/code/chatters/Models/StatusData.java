package aritra.code.chatters.Models;

public class StatusData {

    private String imageUrl , imageDescription;
    private long timeStamp;
    private  String profileImage, userName;

    public StatusData(String imageUrl, long timeStamp ) {
        this.imageUrl = imageUrl;
        this.timeStamp = timeStamp;
    }

    public StatusData() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

}
