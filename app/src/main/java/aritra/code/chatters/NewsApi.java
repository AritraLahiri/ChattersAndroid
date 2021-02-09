package aritra.code.chatters;

import aritra.code.chatters.Models.NewsArray;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsApi {
    @GET()
    Call<NewsArray> getData(@Url String url);
}
