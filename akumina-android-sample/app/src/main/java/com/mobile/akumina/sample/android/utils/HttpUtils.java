/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.mobile.akumina.sample.android.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpUtils {
    private static final String TAG = HttpUtils.class.getSimpleName();
    private static final Logger LOGGER = Logger.getLogger(TAG);

    private final Context mContext;

    public HttpUtils(Context context) {
        this.mContext = context;
    }

    public void post(String url, String requestBody, Map<String, String> extraHeader, ResponseHandler handler) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                if(!TextUtils.isEmpty(response)) {
                    String data = new JSONObject(response).getString("Data");
                    if (!TextUtils.isEmpty(data)) {
                        handler.handleResponse(data);
                    }
                    LOGGER.info("Token " + data);
                } else {
                    handler.handleResponse("");
                }
            } catch (Exception exception) {
                LOGGER.info("VOLLEY Exception" + exception.getMessage());
            }
        }, error -> {
            Log.e("VOLLEY", error.toString());
            handler.handleError(error);
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                if(!extraHeader.isEmpty()) {
                    return extraHeader;
                } else {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            }

            @Override
            public byte[] getBody() {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    String responseString = new String(response.data);
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
                return null;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}