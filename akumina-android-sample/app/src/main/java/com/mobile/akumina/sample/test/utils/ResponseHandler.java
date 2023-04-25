/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.mobile.akumina.sample.test.utils;

import com.android.volley.VolleyError;

public interface ResponseHandler {


    void handleResponse(String data) ;

    void handleError(VolleyError e);
}
