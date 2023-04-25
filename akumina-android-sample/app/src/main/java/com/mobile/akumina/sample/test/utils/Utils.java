package com.mobile.akumina.sample.test.utils;


public class Utils {

    public static String getFormattedScope(String[] scopes, String SCOPE_TO_REPLACE) {
        String formattedValue = "";
        if (null != scopes && scopes.length > 0) {
            formattedValue = String.join(" ", scopes).replace(SCOPE_TO_REPLACE, "");
        }
        return formattedValue;
    }
}
