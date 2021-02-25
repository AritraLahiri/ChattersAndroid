package aritra.code.chatters.Models;

public class NewsPOJO {
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    private NewsSourcePOJO source;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }

    public NewsSourcePOJO getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }
}
