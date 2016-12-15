package com.meetingroom.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import itutorgroup.h2h.R;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月18日 13:11
 * 邮箱：nianbin@mosainet.com
 */
public class NotificationUtil {
    private static NotificationUtil ourInstance = new NotificationUtil();
    public static NotificationUtil getInstance() {
        return ourInstance;
    }

    private NotificationUtil() {
    }

    public void showNotification(Context context, Class<?> cls, boolean vibrate, String content) {
        CharSequence contentText = StringUtil.fromHtml(content);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        int smallIconId = R.mipmap.ic_launcher;
        Bitmap largeIcon = ((BitmapDrawable) ResourcesUtil.getDrawable(context, R.mipmap.ic_launcher)).getBitmap();
        Intent intent = new Intent(context, cls);
        intent.putExtra("showChat", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setDefaults(0)
                .setAutoCancel(true)
                .setLargeIcon(largeIcon)
                .setSmallIcon(smallIconId)
                .setContentTitle(context.getString(R.string.new_message))
                .setContentText(contentText)
                .setTicker(contentText)
                .setContentIntent(pendingIntent);

        nm.notify(0, builder.build());
        if (vibrate)
            vibrate(context);
    }

    private void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 800};
        vibrator.vibrate(pattern, -1);
    }

    public void cancelAll(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }
}
