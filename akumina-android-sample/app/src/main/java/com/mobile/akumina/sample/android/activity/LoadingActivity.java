package com.mobile.akumina.sample.android.activity;

import static com.mobile.akumina.sample.android.utils.URLS.MSAL_SCOPES;
import static com.mobile.akumina.sample.android.utils.URLS.SHAREPOINT_SCOPE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.akumina.android.auth.akuminalib.AkuminaLib;
import com.akumina.android.auth.akuminalib.beans.ClientDetails;
import com.akumina.android.auth.akuminalib.impl.AuthenticationHandler;
import com.akumina.android.auth.akuminalib.listener.AkuminaTokenCallback;
import com.akumina.android.auth.akuminalib.listener.ApplicationListener;
import com.akumina.android.auth.akuminalib.listener.SharePointAuthCallback;
import com.akumina.android.auth.akuminalib.msal.AuthFile;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;
import com.mobile.akumina.sample.android.utils.URLS;
import com.mobile.akumina.sample.android.R;


import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadingActivity extends AppCompatActivity {

    private static final Logger LOGGER = Logger.getLogger(LoadingActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        doSignIn();
    }

    private  void doSignIn() {

        Thread thread = new Thread(() -> {
            boolean mamLogin = getIntent().getBooleanExtra("mamLogin", false);

            AkuminaLib akuminaLib = AkuminaLib.getInstance();
            akuminaLib.addSharePointAuthCallback(new SharePointTokenListener());
            akuminaLib.addAkuminaTokenCallback(new AkuminaTokenListener());
            akuminaLib.addLoggingHandler(new AppLoggingHandler());
            AuthFile authFile = new AuthFile(R.raw.auth_config);

            ClientDetails clientDetails = new ClientDetails(URLS.AUTHORITY, URLS.CLIENT_ID, URLS.REDIRECT_URL,
                    SHAREPOINT_SCOPE, URLS.APP_MANAGER_URL, URLS.TENANT_ID, MSAL_SCOPES);

            try {

                if (!mamLogin) {
                    akuminaLib.authenticateWithMSAL(this, authFile, clientDetails, new AuthCallback(), new AppListener());
                } else {
                    akuminaLib.authenticateWithMSALAndMAM(this, authFile, clientDetails, new AuthCallback(), new AppListener());
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error", e);
                showError(e);
            }
        });

        thread.start();
    }

    private void showError(Exception e) {
        Intent intent = new Intent(getBaseContext(), ErrorActivity.class);
        intent.putExtra("exception", e.getLocalizedMessage());
        startActivity(intent);
    }
    private  class  SharePointTokenListener implements SharePointAuthCallback {

        @Override
        public void onSuccess(IAuthenticationResult iAuthenticationResult) {
            LOGGER.log(Level.INFO, "Got Token : " + iAuthenticationResult.getAccessToken());
        }

        @Override
        public void onError(MsalException e) {
            LOGGER.log(Level.SEVERE, "Unable to get sharePointToken", e);
            showError(e);
        }
    }
    private  class AkuminaTokenListener implements AkuminaTokenCallback {

        @Override
        public void onSuccess(String s) {
            LOGGER.info("Got Token " + s);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(Exception e) {
            LOGGER.log(Level.SEVERE, "Error while get Akumina Token", e);
            showError(e);
        }
    }
    private class AppLoggingHandler implements com.akumina.android.auth.akuminalib.listener.LoggingHandler {

        @Override
        public void handleMessage(String message, boolean error) {
            if(error) {
                LOGGER.log(Level.SEVERE, message);
            }else {
                LOGGER.log(Level.INFO, message);
            }
        }
    }
    private  class  AuthCallback implements AuthenticationHandler {

        @Override
        public void onCancel() {
           showError(new Exception("User Cancelled the sign-in"));
        }

        @Override
        public void onSuccess(IAuthenticationResult iAuthenticationResult) {
            Log.i("AuthCallback", "onSuccess: ." + iAuthenticationResult.getAccessToken());
        }

        @Override
        public void onError(MsalException e) {
            Log.e("AuthCallback", "onError: ", e );
            showError(e);
        }
    }

    private  class  AppListener implements ApplicationListener {

        @Override
        public void onCreated(IPublicClientApplication application) {
            Log.i("AppListener", "onCreated: Application created "+ application.toString());
        }

        @Override
        public void onError(MsalException exception) {
//            Log.e("AppListener", "onError: ", exception);
//            showMessage("Unable to sign-in");
            showError(exception);
        }
    }
}