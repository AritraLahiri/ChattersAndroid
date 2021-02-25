package aritra.code.chatters.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonResponse {


    @SerializedName("items")
    @Expose
    private ArrayList<DummyPOJO> itemsArray;
    private String nextPageToken;
    private int totalResult;

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public ArrayList<DummyPOJO> getItemsArray() {
        return itemsArray;
    }


}
