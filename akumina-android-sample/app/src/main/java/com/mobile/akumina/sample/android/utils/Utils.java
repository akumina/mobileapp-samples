package com.mobile.akumina.sample.android.utils;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.mobile.akumina.sample.android.activity.ErrorActivity;
import com.mobile.akumina.sample.android.activity.HomeActivity;

public class Utils {

    public static String getFormattedScope(String[] scopes, String SCOPE_TO_REPLACE) {
        String formattedValue = "";
        if (null != scopes && scopes.length > 0) {
            formattedValue = String.join(" ", scopes).replace(SCOPE_TO_REPLACE, "");
        }
        return formattedValue;
    }

    public static void showError(Exception e, Activity activity) {
        String stackTrace = Log.getStackTraceString(e);
        Intent intent = new Intent(activity.getBaseContext(), ErrorActivity.class);
        intent.putExtra("exception", stackTrace);
        activity.startActivity(intent);
    }
    public static void showHome(Activity activity) {
        Intent intent = new Intent(activity.getBaseContext(), HomeActivity.class);
        activity.startActivity(intent);
    }
}
