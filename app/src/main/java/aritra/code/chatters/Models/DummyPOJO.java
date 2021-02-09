package aritra.code.chatters.Models;

import com.google.gson.annotations.SerializedName;

public class DummyPOJO {

    @SerializedName("id")
    private String videoId;
    @SerializedName("snippet")
    private VideoDetailsPOJO details;

    public DummyPOJO(String videoId) {

        this.videoId = videoId;
    }

    public DummyPOJO() {
    }


    public String getVideoId() {
        return videoId;
    }

    public VideoDetailsPOJO getDetails() {
        return details;
    }
}
