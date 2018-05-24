package com.cabinet.autointerphonereponse.service;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.cabinet.autointerphonereponse.utils.Utils;

import java.io.IOException;

/**
 * Created by Vyach on 02/05/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLService extends NotificationListenerService {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        try {
            if (sbn.getNotification().actions != null) {
                for (Notification.Action action : sbn.getNotification().actions)
                {
                    String answerLabel = "Answer";

                    try {
                        answerLabel = Utils.getProperty("answer_label", getApplicationContext());

                        Log.i("onNotificationPosted", "reading answer label prop = " + answerLabel);

                    } catch(IOException ioe) {
                        Log.e("IOExeption", ioe.getMessage());
                    }
                    Log.i("onNotificationPosted", "" + action.title.toString());
                    if (action.title.toString().equalsIgnoreCase(answerLabel)) {
                        Log.e("onNotificationPosted", "" + true);
                        PendingIntent intent = action.actionIntent;

                        try {
                            intent.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}
