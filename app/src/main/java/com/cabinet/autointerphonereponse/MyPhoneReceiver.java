package com.cabinet.autointerphonereponse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;


/**
 * Created by Vyach on 04/04/2018.
 */

public class MyPhoneReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String phoneNumber = "";
        if (extras != null)
        {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
                phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Intent i = new Intent(context, AcceptCallActivity.class);
            i.putExtras(intent);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }
}
