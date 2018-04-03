package com.cabinet.autointerphonereponse;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        EditText phoneNo = findViewById(R.id.et_phoneNo);
        EditText noToCompose = findViewById(R.id.et_noToCompose);

        phoneNo.setText(prefs.getString("phoneNo", ""));
        noToCompose.setText(prefs.getString("noToCompose", ""));

        phoneNo.addTextChangedListener(this.setWatcher(prefs, "phoneNo"));
        noToCompose.addTextChangedListener(this.setWatcher(prefs, "noToCompose"));
    }



    @Override
    public void onRequestPermissionsResult(int  requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {

                }
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param prefs required to configure the sharedPrefs
     * @param idPref required to configure the sharedPrefs
     * @return a watcher with a configured sharedPrefs
     */
    private TextWatcher setWatcher(final SharedPreferences prefs, final String idPref) {

        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                prefs.edit().putString(idPref, s.toString()).commit();
            }
        };
    }


}
