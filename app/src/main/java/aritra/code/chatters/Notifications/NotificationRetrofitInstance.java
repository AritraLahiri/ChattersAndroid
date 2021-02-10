package aritra.code.chatters.Notifications;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationRetrofitInstance {

    private static Retrofit retrofit;
    private static String BASE_URL = "https://fcm.googleapis.com/";



    public static Retrofit getRetrofit() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
