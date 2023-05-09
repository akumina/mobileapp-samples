/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.mobile.akumina.sample.test;

import static androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS;
import static com.mobile.akumina.sample.test.utils.URLS.MSAL_SCOPES;
import static com.mobile.akumina.sample.test.utils.URLS.SHAREPOINT_SCOPE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.akumina.android.auth.akuminalib.AkuminaLib;
import com.akumina.android.auth.akuminalib.beans.ClientDetails;
import com.akumina.android.auth.akuminalib.impl.AuthenticationHandler;
import com.akumina.android.auth.akuminalib.listener.AkuminaTokenCallback;
import com.akumina.android.auth.akuminalib.listener.ApplicationListener;
import com.akumina.android.auth.akuminalib.listener.LoggingHandler;
import com.akumina.android.auth.akuminalib.listener.SharePointAuthCallback;
import com.akumina.android.auth.akuminalib.msal.AuthFile;
import com.google.android.material.navigation.NavigationView;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;
import com.mobile.akumina.sample.test.activity.ErrorActivity;
import com.mobile.akumina.sample.test.activity.LoadingActivity;
import com.mobile.akumina.sample.test.activity.WebActivity;
import com.mobile.akumina.sample.test.utils.URLS;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main activity of the app - runs when the app starts.
 * <p>
 * Handles authentication, explicitly interacting with MSAL and implicitly with MAM.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final Logger LOGGER = Logger.getLogger(MainActivity.class.getName());

    private Button loginWithMSALButton;
    private Button loginWithMAMButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printHashKey(getApplicationContext());
        initView();
    }
    private void initView() {
        setContentView(R.layout.activity_main);
//        Button authButton = findViewById(R.id.authButton);
//
//        authButton.setOnClickListener(view -> auth());

        this.loginWithMSALButton = (Button) findViewById(R.id.loginWithMsal);
        this.loginWithMSALButton.setOnClickListener(view -> doSignIn(false));
        this.loginWithMAMButton = findViewById(R.id.loginWithMAM);
        this.loginWithMAMButton.setOnClickListener(view -> doSignIn(true));
//        showButtons(Button.INVISIBLE);
    }

    private Integer  hasBiometricCapability(Context  context)  {
        BiometricManager  biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
    }
    public  void doSignIn(boolean mamLogin) {
        LOGGER.info("Starting interactive auth");
         new Thread(() -> login(mamLogin)).start();
    }

    private void login(boolean mamLogin) {

        showLoading(mamLogin);

    }
    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        return false;
    }

    private void showMessage(final String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    private void  showLoading(boolean mamLogin) {
        Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
        intent.putExtra("mamLogin", mamLogin);
        startActivity(intent);
    }

    private void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("HashKey", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Error", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Error", "printHashKey()", e);
        }
    }
}
