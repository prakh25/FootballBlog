package com.example.prakh.footballblog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.Spanned;

import com.example.corelib.SharedPreferenceManager;
import com.example.prakh.footballblog.post_detail.DetailActivity;
import com.example.prakh.footballblog.utils.Utils;
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

            if (sharedPreferenceManager.getAppDefaultNotification()) {
                RingtoneManager.getRingtone(this,
                        Uri.parse("android.resource://" + getPackageName() + "/" +
                                R.raw.whistle_notification)).play();
            } else {
                RingtoneManager.getRingtone(this,
                        Uri.parse(sharedPreferenceManager.getRingtone())).play();
            }

            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                FcmNotification notification = new FcmNotification();
                notification.setTitle(data.get("title"));
                notification.setContent(data.get("content"));
                notification.setPost_id(Integer.parseInt(data.get("post_id")));
                notification.setPermalink(data.get("permalink"));
                notification.setThumbnail(data.get("thumbnail"));
                displayNotification(notification);
            }
        }
    }

    private void displayNotification(FcmNotification notification) {

        Integer id = notification.getPost_id();
        Spanned title = Utils.fromHtml(notification.getContent());

        Intent intent = DetailActivity.newNotificationIntent(this, id);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingShareIntent = PendingIntent.getActivity(this,
                (int) System.currentTimeMillis(), Intent
                        .createChooser(Utils.sharingIntent(title,
                                getString(R.string.app_name), notification.getPermalink()), "Share Using"),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_FOOTBALL_BLOG");
        builder.setContentTitle(getString(R.string.app_name));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(title));
        builder.setContentText(title);
        builder.setSmallIcon(R.drawable.ic_notification_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher_round));
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        builder.addAction(R.drawable.ic_share_black_24dp,
                getString(R.string.action_share), pendingShareIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int unique_id = (int) System.currentTimeMillis();
        notificationManager.notify(unique_id, builder.build());
    }
}
