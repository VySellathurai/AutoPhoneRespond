package com.cabinet.autointerphonereponse.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Vyach on 24/05/2018.
 */

public class Permission {

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

    public static String currentVersion() {
        double release = Double.parseDouble(Build.VERSION.RELEASE.replaceAll("(\\d+[.]\\d+)(.*)","$1"));
        String codeName = "Unsupported";
        if(release >= 4.1 && release < 4.4) codeName= "Jelly Bean";
        else if(release < 5) codeName = "Kit Kat";
        else if(release < 6) codeName = "Lollipop";
        else if(release < 7) codeName = "Marshmallow";
        else if(release < 8) codeName = "Nougat";
        else if(release < 9) codeName = "Oreo";
        return codeName + " v" + release + ", API Level: " + Build.VERSION.SDK_INT;
    }


}
