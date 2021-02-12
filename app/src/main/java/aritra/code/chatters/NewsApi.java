package aritra.code.chatters;

import aritra.code.chatters.Models.NewsArray;
import aritra.code.chatters.Notifications.NotificationSender;
import aritra.code.chatters.Notifications.ResponseDataNotification;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface NewsApi {
    @GET()
    Call<NewsArray> getData(@Url String url);


    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=" +
                            BuildConfig.ServerApiKey
            }
    )
    @POST("fcm/send")
    Call<ResponseDataNotification> sendNotification(@Body NotificationSender body);

}
