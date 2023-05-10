package com.mobile.akumina.sample.android.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.akumina.android.auth.akuminalib.AkuminaLib;
import com.akumina.android.auth.akuminalib.http.AkuminaResponse;
import com.akumina.android.auth.akuminalib.listener.ErrorListener;
import com.akumina.android.auth.akuminalib.listener.ResponseListener;
import com.akumina.android.auth.akuminalib.utils.TokenType;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobile.akumina.sample.android.R;
import com.mobile.akumina.sample.android.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        findViewById(R.id.homeBtn).setOnClickListener(view -> Utils.showHome(this));
        loadProfile();
    }

    private void loadProfile() {
        try {
            AkuminaLib akuminaLib = AkuminaLib.getInstance();
            String token = akuminaLib.getToken(TokenType.GRAPH);
            Log.i("TOKEN", "Calling with Token" + token);
            Map<String, String> query = new HashMap<>();
            query.put("queryUrl", "https://graph.microsoft.com/v1.0/me");
            query.put("headers", "{}");
            query.put("cachekey", "null");


            akuminaLib.callAkuminaApi(Request.Method.GET, "https://mainapp.akumina.dev/api/graph/graphquery", query, null,
                    null, token, response -> {
                        AkuminaResponse r = (AkuminaResponse) response;
                        showDetails(r);
                    }, error -> {
                        Log.e("HTTP", "Error", error);
                        Utils.showError(error, ProfilesActivity.this);
                    }, this);
        } catch (Exception e) {
            Log.e("Error", "loadProfile() returned: ", e);
            Utils.showError(e, this);
        }
    }


    void showDetails(AkuminaResponse response) {
        try {
            if (response.getStatusCode() == 200) {
                JsonObject jsonObject = new JsonParser().parse(response.getRawData()).getAsJsonObject();
                TextInputLayout textInputLayout = findViewById(R.id.displayName);
                textInputLayout.getEditText().setText(getValue(jsonObject,"displayName"));
                textInputLayout = findViewById(R.id.givenName);
                textInputLayout.getEditText().setText(getValue(jsonObject,"givenName"));
                textInputLayout = findViewById(R.id.surName);
                textInputLayout.getEditText().setText(getValue(jsonObject,"surname"));
                textInputLayout = findViewById(R.id.mail);
                textInputLayout.getEditText().setText(getValue(jsonObject,"mail"));
                textInputLayout = findViewById(R.id.principal);
                textInputLayout.getEditText().setText(getValue(jsonObject,"userPrincipalName"));
                textInputLayout = findViewById(R.id.jobTitle);
                textInputLayout.getEditText().setText(getValue(jsonObject,"jobTitle"));
            } else {
                Utils.showError(new Exception("Unable to fetch status code " + response.getStatusCode()), this);
            }
        } catch (Exception e) {
            Utils.showError(e, this);
        }
    }

    private String getValue(JsonObject jsonObject, String name) throws JSONException {
       if(jsonObject.get(name).isJsonNull()) {
           return  "  ";
       }else {
           return jsonObject.get(name).getAsString();
       }
    }
}