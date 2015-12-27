package group20.flashcards.flashcards;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by dangn on 10/31/2015.
 */
public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
		/*Intent i = new Intent(context, DisplayNotification.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);*/
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent2, 0);
        Notification.Builder notifbuilder = new Notification.Builder(context)
                .setContentTitle("Flashcard")
                .setContentText("Have you review your words?").setSmallIcon(R.drawable.notification)
                .setContentIntent(pending);
        Notification notifications = new Notification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notifications = notifbuilder.build();
        } else{
            notifications = notifbuilder.getNotification();
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notifications.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notifications);
    }
}
