package com.cabinet.autointerphonereponse.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static android.content.Context.TELEPHONY_SERVICE;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //permet de connaitre l etat de l appel
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String phoneNo = prefs.getString("phoneNo", "");
        int noToCompose = Integer.parseInt(prefs.getString("noToCompose", ""));

        Log.i("PhoneStateReceiver", "Phone number from param " + phoneNo);
        Log.i("PhoneStateReceiver", "No to compose from param " + noToCompose);



        //si le num entrant est equal au sharedPrefs tel num alors on deroule le metier de l app
        if (incomingNumber.equals(phoneNo)) {
            Log.i("PhoneStateReceiver", "Incomming phone number = sharedPrefs phone no");

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //Appel entrant
                Log.i("PhoneStateReceiver", "Receiving call from " + incomingNumber);

            }
            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                //L appel est decroche
                Log.i("PhoneStateReceiver", "Offhook call from " + incomingNumber);

                sendDTMF(noToCompose, 3);

                if (!killCall(context, 3)) {
                    Log.e("PhoneStateReceiver", "Unable to Hung up the call");
                }
            }
            if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //ignored call
                Log.i("PhoneStateReceiver", "Termined call from " + incomingNumber);
            }

        }
        else {
            Log.i("PhoneStateReceiver", "Incomming phone number != sharedPrefs phone no");
        }

    }

    /**
     * @param noToCompose number to compose in the dial
     * @param secondToWait int second to wait before compsing in dial
     */
    private void sendDTMF(int noToCompose, int secondToWait) {

        try {
            TimeUnit.SECONDS.sleep(secondToWait);
            Log.i("dialCompose", "waiting " + secondToWait + " secondes");
        } catch (InterruptedException e) {
            Log.e("Exception", e.getMessage());
        }

        ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_DTMF, 500);
        toneGen.startTone(noToCompose);
        toneGen.stopTone();
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
                    (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

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
