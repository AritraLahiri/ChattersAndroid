package aritra.code.chatters.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingServices extends FirebaseMessagingService {

    private final static String channelName = "newChat";
    private final static String channelDescription = "new message received";
    private final static String channelId = "new_chat";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null) {
            createNotificationChannel();
            String title = remoteMessage.getData().get("Title");
            String text = remoteMessage.getData().get("Message");
            MyNotificationBuilder.displayNotifications(getApplicationContext(), title, text);
        }


    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }
}
