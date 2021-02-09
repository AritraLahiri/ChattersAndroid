package aritra.code.chatters.Models;

public class VideoDetailsPOJO {

    private String publishedAt;
    private String channelId;
    private String title;
    private VideoImage thumbnails;
    private String description;
    private  String channelTitle;



    public String getPublishedAt() {
        return publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public VideoImage getThumbnails() {
        return thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }
}
