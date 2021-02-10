package aritra.code.chatters;

import aritra.code.chatters.Models.NewsArray;
import aritra.code.chatters.Notifications.NotificationSender;
import aritra.code.chatters.Notifications.ResponseDataNotification;
import retrofit2.Call;
import retrofit2.Response;
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
                    "Authorization:key=AAAAD9IWbxg:APA91bHA1dhxof-1wDNTOECvBUfOYN7uRhwD79pv2sPAEWrFBx08_C96XKkzQvoPxNYyyuKZ3ItDr82LQ1o25qM2xszQczE0xLi1WbK8qwP9onl8IAEYVJ4hVrSrMA-BryXEpQsfRD-J"
            }
    )
    @POST("fcm/send")
    Call<ResponseDataNotification> sendNotification(@Body NotificationSender body);

}
