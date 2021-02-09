package aritra.code.chatters.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonResponse {


    @SerializedName("items")
    @Expose
    private ArrayList<DummyPOJO> itemsArray;
    private  String nextPageToken ;
    private int totalResult;

    public int getTotalResult() {
        return totalResult;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public ArrayList<DummyPOJO> getItemsArray() {
        return itemsArray;
    }


}
