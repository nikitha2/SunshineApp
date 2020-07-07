package com.nikitha.android.sunshineapp.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.nikitha.android.sunshineapp.MainActivity;
import com.nikitha.android.sunshineapp.R;
import com.nikitha.android.sunshineapp.data.SunshinePreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * Utility class for creating hydration notifications
 */
public class NotificationUtils {

    /* This notification ID can be used to access our notification after we've displayed it. This
     can be handy when we need to cancel the notification, or perhaps update it. This number is
     arbitrary and can be set to whatever you like. 1138 is in no way significant.*/
    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int ACTION_DRINK_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    // https://developer.android.com/training/notify-user/build-notification.html

       public static void updateUserWithNotification(Context context) {
           NotificationManager notificationManager = createNotificationChannel(context);

           NotificationCompat.Builder notificationBuilder = buildNotification(context);

           notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
           SunshinePreferences.saveLastNotificationTime(context, System.currentTimeMillis());
    }

    private static NotificationCompat.Builder buildNotification(Context context) {
        /*PendingIntent water_increment_PendingIntent = water_increment_intent(context);
        PendingIntent water_ignore_PendingIntent = water_ignore_intent(context);*/

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.art_clouds)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);
//                .addAction(R.drawable.ic_local_drink_black_24px, context.getString(R.string.didit),
//                        water_increment_PendingIntent)
//                .addAction(R.drawable.ic_local_drink_black_24px, context.getString(R.string.ignore),
//                        water_ignore_PendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        return notificationBuilder;
    }

    /*private static PendingIntent water_ignore_intent(Context context) {
        Intent water_ignore_intent = new Intent(context, WaterReminderIntentService.class);
        water_ignore_intent.setAction(ReminderTasks.ACTION_IGNORE_WATER_COUNT);
        //water_increment_intent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        return PendingIntent.getService(context, ACTION_IGNORE_PENDING_INTENT_ID, water_ignore_intent, 0);
    }

    private static PendingIntent water_increment_intent(Context context) {
        Intent water_increment_intent = new Intent(context, WaterReminderIntentService.class);
        water_increment_intent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        //water_increment_intent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        return PendingIntent.getService(context, ACTION_DRINK_PENDING_INTENT_ID, water_increment_intent, 0);
    }*/

    private static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return pendingIntent;
    }

    private static NotificationManager createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.main_notification_channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(WATER_REMINDER_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager =context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        return notificationManager;
    }


    private static Bitmap largeIcon(Context context) {
        // COMPLETED (5) Get a Resources object from the context.
        Resources res = context.getResources();
        // COMPLETED (6) Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.drawable.ic_local_drink_black_24px
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.art_clouds);
        return largeIcon;
    }

    // Method to clear all notifications
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
