package itutorgroup.h2h.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import itutorgroup.h2h.R;
import itutorgroup.h2h.activity.MeetingActivity;

/**
 * 描述:
 * 作者：znb
 * 时间：2016年07月18日 13:11
 * 邮箱：nianbin@mosainet.com
 */
public class NotificationUtil {
    private static NotificationUtil ourInstance = new NotificationUtil();
    private Notification n;
    private NotificationManager nm;
    public static NotificationUtil getInstance() {
        return ourInstance;
    }

    private NotificationUtil() {
    }

    public void showNotification(Context context, boolean vibrate, String content) {
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        int smallIconId = R.mipmap.ic_launcher;
        Bitmap largeIcon = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
        Intent intent = new Intent(context, MeetingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setDefaults(0)
                .setAutoCancel(true)
                .setLargeIcon(largeIcon)
                .setSmallIcon(smallIconId)
                .setContentTitle(context.getString(R.string.new_message))
                .setContentText(content)
                .setTicker(content)
                .setContentIntent(pendingIntent);


//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
////            PendingIntent huangIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
//            builder.setFullScreenIntent(pendingIntent, true);
//            builder.setVisibility(Notification.VISIBILITY_PRIVATE);
//        }
        n = builder.build();
        nm.notify(0, n);
        if (vibrate)
            vibrate(context);
    }

    public void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 800};
        vibrator.vibrate(pattern, -1);
    }
    public void cancelAll(){
        if(n!=null)
            nm.cancelAll();
    }
}
