package com.cabinet.autointerphonereponse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static android.support.v4.content.ContextCompat.startActivity;
import static android.support.v4.content.ContextCompat.startForegroundService;

/**
 * Created by Vyach on 22/03/2018.
 */
public class PhoneStateReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        //permet de connaitre l etat de l appel
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            //Appel entrant
            Log.i("PhoneStateReceiver", "Receiving call from " + incomingNumber);

        }
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            //L appel est decroche
            Log.i("PhoneStateReceiver", "Offhook call from " + incomingNumber);

            int composeNumber = 7;

            //dialCompose(context, composeNumber, 3);
            

            /*if (!killCall(context, 3)) {
                Log.e("PhoneStateReceiver", "Unable to Hung up the call");
            }*/
        }
        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            //ignored call
            Log.i("PhoneStateReceiver", "Ignored call from " + incomingNumber);
        }

    }

    /**
     * compose number from dial depends on the context
     * @param context context used for compose the dial
     * @param noToCompose number to compose in the dial
     * @param secondToWait int second to wait before compsing in dial
     */
    private void dialCompose(Context context, int noToCompose, int secondToWait) {

        try {
            TimeUnit.SECONDS.sleep(secondToWait);
            Log.i("dialCompose", "waiting " + secondToWait + " secondes");
        } catch (InterruptedException e) {
            Log.e("Exception", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:,"+Uri.encode(noToCompose+"")));
        Bundle extras = intent.getExtras();

        startActivity(context, intent, extras);



        Log.i("dialCompose", noToCompose + " done");
    }

    /**
     *
     * Hung up a call from the context
     * @param context used for hunging up
     * @param secondToWait int second to wait before trying to hung up
     * @return true of false (hung up or fail)
     */
    public boolean killCall(Context context, int secondToWait) {
        try {
            Log.i("killCall", "Waiting " + secondToWait + " seconds before trying to hung up");
            TimeUnit.SECONDS.sleep(secondToWait);
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("PhoneStateReceiver","PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }
}
