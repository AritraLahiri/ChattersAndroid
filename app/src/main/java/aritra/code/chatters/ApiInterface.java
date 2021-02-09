package aritra.code.chatters;

import aritra.code.chatters.Models.JsonResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    String header = "youtube/v3/videos?part=snippet,contentDetails,statistics&chart=mostPopular&regionCode=IN";
    String apiKey = "AIzaSyA6xGwp7f305cbE5F8lsHi4VrMcbIN-Y_Q";

    @GET(header + "&key=" + apiKey)
    Call<JsonResponse> getPosts(@Query("pageToken") String token);

}
