package ua.dn.effect.AgentApp.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import ua.dn.effect.AgentApp.R;

/**
 * Created with IntelliJ IDEA.
 * User: igrebeshkov
 * Date: 06.11.13
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public class AgentNotificationManager {
    Activity context;
    NotificationManager manager;
    public AgentNotificationManager(Activity ctx){
        context = ctx;
    }

    public void setNotification(String title, String text){
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notif = new Notification(R.drawable.logoicon, "Торговый агент",
                System.currentTimeMillis());

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, context.getIntent(), 0);

        notif.setLatestEventInfo(context, title, text, pIntent);

        notif.flags |= Notification.FLAG_AUTO_CANCEL;

        manager.notify(1, notif);
    }

}
