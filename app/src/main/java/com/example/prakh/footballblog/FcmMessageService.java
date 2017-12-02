package com.example.prakh.footballblog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.example.corelib.SharedPreferenceManager;
import com.example.prakh.footballblog.post_detail.DetailActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by prakh on 13-11-2017.
 */

public class FcmMessageService extends FirebaseMessagingService {

    private SharedPreferenceManager sharedPreferenceManager;
    private static int VIBRATION_TIME = 500; // in millisecond

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPreferenceManager = SharedPreferenceManager.getInstance();
        if (sharedPreferenceManager.getNotifications()) {
            // play vibration
            if (sharedPreferenceManager.getVibration()) {
                if (getSystemService(Context.VIBRATOR_SERVICE) != null) {
                    ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
                }
            }
            RingtoneManager.getRingtone(this,
                    Uri.parse(sharedPreferenceManager.getRingtone())).play();

            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                displayNotification(data.get("title"), data.get("id"));
            }
        }
    }

    private void displayNotification(String title, String postId) {
        Integer id = Integer.valueOf(postId);
        boolean fromNotif = !HomeActivity.active;

        Intent intent = DetailActivity.createNewIntent(this,
                id, fromNotif);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_FOOTBALL_BLOG");
        builder.setContentTitle(getString(R.string.app_name));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(title));
        builder.setContentText(title);
        builder.setSmallIcon(R.drawable.ic_notification_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher_round));
        builder.setColor(this.getResources().getColor(R.color.colorPrimary));
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int unique_id = (int) System.currentTimeMillis();
        notificationManager.notify(unique_id, builder.build());
    }
}
