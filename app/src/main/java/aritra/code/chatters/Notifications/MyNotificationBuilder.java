package aritra.code.chatters.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import aritra.code.chatters.MainActivity;
import aritra.code.chatters.R;

public class MyNotificationBuilder {

    private final static String channelId = "new_chat";


    public static void displayNotifications(Context context, String title, String body) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                101,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(R.drawable.viral_icon);
        builder.setContentText(body);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());

    }

}
