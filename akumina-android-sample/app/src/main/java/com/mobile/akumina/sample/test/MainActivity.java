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
import android.os.Bundle;
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
import com.mobile.akumina.sample.test.activity.WebActivity;
import com.mobile.akumina.sample.test.utils.URLS;

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

        AkuminaLib akuminaLib = AkuminaLib.getInstance();
        akuminaLib.addSharePointAuthCallback(new SharePointTokenListener());
        akuminaLib.addAkuminaTokenCallback(new AkuminaTokenListener());
        akuminaLib.addLoggingHandler(new AppLoggingHandler());
        AuthFile authFile = new AuthFile(R.raw.auth_config);

        ClientDetails clientDetails= new ClientDetails(URLS.AUTHORITY,URLS.CLIENT_ID,URLS.REDIRECT_URL,
                SHAREPOINT_SCOPE, URLS.APP_MANAGER_URL, URLS.TENANT_ID,MSAL_SCOPES);

        try {

                if(!mamLogin) {
                    akuminaLib.authenticateWithMSAL(this, authFile, clientDetails, new AuthCallback(), new AppListener());
                }else {
                    akuminaLib.authenticateWithMSALAndMAM(this, authFile, clientDetails, new AuthCallback(), new AppListener());
                }

        }catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error", e);
            showMessage("An Error Occurred during the sign-in");
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
//    public void doSignIn() {
//        Thread thread = new Thread(() -> {
//            LOGGER.info("Starting interactive auth");
//
//            try {
//                String loginHint = null;
//                mUserAccount = AppSettings.getAccount(getApplicationContext());
//                String token = AppSettings.getToken(getApplicationContext());
//                AppSettings.saveOldToken(token, getApplicationContext());
//                if (mUserAccount != null) {
//                    loginHint = mUserAccount.getUPN();
//                }
//                if (StringUtil.isEmpty(token)) {
//                    MSALUtil.acquireToken(MainActivity.this, MSAL_SCOPES, loginHint, new AuthCallback());
//                } else {
//                    MSALUtil.acquireTokenSilent(MainActivity.this, mUserAccount.getAADID(), MSAL_SCOPES, new AuthCallback());
//                }
//
//            } catch (MsalException | InterruptedException e) {
//                LOGGER.log(Level.SEVERE, getString(R.string.err_auth), e);
//                Rollbar.instance().error(e, "Error while doSignIn");
//                showMessage("Authentication exception occurred");
//            }
//        });
//        thread.start();
//    }

//    private void signOutUser() {
//        // Initiate an MSAL sign out on a background thread.
//        final AppAccount effectiveAccount = mUserAccount;
//
//        Thread thread = new Thread(() -> {
//            try {
//                MSALUtil.signOutAccount(this, effectiveAccount.getAADID());
//            } catch (MsalException | InterruptedException e) {
//                LOGGER.log(Level.SEVERE, "Failed to sign out user " + effectiveAccount.getAADID(), e);
//                Rollbar.instance().error(e, "Failed to sign out user " + effectiveAccount.getAADID());
//            }
//
//            mEnrollmentManager.unregisterAccountForMAM(effectiveAccount.getUPN());
//            AppSettings.clearAccount(getApplicationContext());
//            AppSettings.clearNewAndOldToken(getApplicationContext());
//            mUserAccount = null;
//
//        });
//        thread.start();
//    }

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

    private  class  AuthCallback implements AuthenticationHandler {

        @Override
        public void onCancel() {
            showMessage("User Cancelled the sign-in");
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

    private void showError(Exception e) {
        Intent intent = new Intent(getBaseContext(), ErrorActivity.class);
        intent.putExtra("exception", e);
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
            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(Exception e) {
            LOGGER.log(Level.SEVERE, "Error while get Akumina Token", e);
            showError(e);
        }
    }
//    private class AuthCallback implements AuthenticationCallback {
//        @Override
//        public void onError(final MsalException exc) {
//            LOGGER.log(Level.SEVERE, "authentication failed", exc);
//
//            if (exc instanceof MsalIntuneAppProtectionPolicyRequiredException) {
//                MsalIntuneAppProtectionPolicyRequiredException appException = (MsalIntuneAppProtectionPolicyRequiredException) exc;
//
//                // Note: An app that has enabled APP CA with Policy Assurance would need to pass these values to `remediateCompliance`.
//                // For more information, see https://docs.microsoft.com/en-us/mem/intune/developer/app-sdk-android#app-ca-with-policy-assurance
//                final String upn = appException.getAccountUpn();
//                final String aadid = appException.getAccountUserId();
//                final String tenantId = appException.getTenantId();
//                final String authorityURL = appException.getAuthorityUrl();
//
//                // The user cannot be considered "signed in" at this point, so don't save it to the settings.
//                mUserAccount = new AppAccount(upn, aadid, tenantId, authorityURL);
//
//                final String message = "Intune App Protection Policy required.";
//                showMessage(message);
//                final String info = String.format("Data from broker: UPN: %s; AAD ID: %s; Tenant ID: %s; Authority: %s",
//                        upn, aadid, tenantId, authorityURL);
//                Rollbar.instance().error(exc, message + info);
//                LOGGER.info("MsalIntuneAppProtectionPolicyRequiredException received.");
//                Rollbar.instance().error(exc, "authentication failed " + info);
//
//            } else if (exc instanceof MsalUserCancelException) {
//                showMessage("User cancelled sign-in request");
//            } else {
//                Rollbar.instance().error(exc, "Exception occurred");
//                showMessage("Exception occurred - check logcat");
//            }
//        }
//
//        @Override
//        public void onSuccess(final IAuthenticationResult result) {
//            IAccount account = result.getAccount();
//
//            final String upn = account.getUsername();
//            final String aadId = account.getId();
//            final String tenantId = account.getTenantId();
//            final String authorityURL = account.getAuthority();
//            String message = "Authentication succeeded for user " + upn + " token =" + result.getAccessToken();
//            LOGGER.info(message);
//
//            Map<String, String> graphParams = new Hashtable<>();
//            graphParams.put("resource", MSAL_SCOPES[0].replace(".default", ""));
//            graphParams.put("id_token", result.getAccount().getIdToken());
//            graphParams.put("access_token", result.getAccessToken());
//            graphParams.put("expires_on", String.valueOf(result.getExpiresOn().getTime() / 1000L));
//            String mScope = Utils.getFormattedScope(result.getScope(), MSAL_SCOPES[0].replace("/.default", ""));
//            graphParams.put("scope", mScope);
//            authData.add(graphParams);
//
//            // Save the user account in the settings, since the user is now "signed in".
//            mUserAccount = new AppAccount(upn, aadId, tenantId, authorityURL);
//            AppSettings.saveAccount(getApplicationContext(), mUserAccount);
//            AppSettings.saveAccessToken(result.getAccessToken(), getApplicationContext());
//
//            // Register the account for MAM.
//            mEnrollmentManager.registerAccountForMAM(upn, aadId, tenantId, authorityURL);
//
//            getSharePointAccessTokenAsync();
//        }
//
//        @Override
//        public void onCancel() {
//            showMessage("User cancelled auth attempt");
//        }
//    }

//    private void getSharePointAccessTokenAsync() {
//        MSALUtil.getSingleAccountApp().acquireTokenSilentAsync(
//                SHAREPOINT_SCOPE.split(" "),
//                URLS.AUTHORITY,
//                getSharepointAuthSilentCallback());
//    }
//
//    private SilentAuthenticationCallback getSharepointAuthSilentCallback() {
//        return new SilentAuthenticationCallback() {
//            @Override
//            public void onSuccess(IAuthenticationResult authenticationResult) {
//                new PushNotificationUtils().getPushNotificationToken(MainActivity.this);
//
//                LOGGER.log(Level.FINE, "Successfully authenticated " + authenticationResult.getAccessToken() + " ID TOKEN " + authenticationResult.getAccount().getIdToken());
//                Map<String, String> spParams = new Hashtable<>();
//                spParams.put("resource", SHAREPOINT_SCOPE);
//                spParams.put("id_token", authenticationResult.getAccount().getIdToken());
//                spParams.put("access_token", authenticationResult.getAccessToken());
//                spParams.put("expires_on", String.valueOf(authenticationResult.getExpiresOn().getTime() / 1000L));
//                String mScope = Utils.getFormattedScope(authenticationResult.getScope(), SHAREPOINT_SCOPE_REPLACE);
//                spParams.put("scope", mScope);
//                authData.add(spParams);
//                Gson gson = new Gson();
//                String jsonCartList = gson.toJson(authData);
//                LOGGER.log(Level.INFO, "DATA:" + jsonCartList);
//                try {
//                    sendTokenToServer(jsonCartList);
//                } catch (Exception exception) {
//                    Rollbar.instance().error(exception, "Error while connect SP JSONException");
//                    showMessage("Unable to connect the server, try later");
//                }
//            }
//
//            @Override
//            public void onError(MsalException exception) {
//                /* Failed to acquireToken */
//                LOGGER.log(Level.SEVERE, "Authentication failed: " + exception.toString());
//                Rollbar.instance().error(exception, "Authentication failed: ");
//                // displayError(exception);
//                if (exception instanceof MsalClientException) {
//                    LOGGER.info("MsalClientException " + exception.getMessage());
//                    /* Exception inside MSAL, more info inside MsalError.java */
//                } else if (exception instanceof MsalServiceException) {
//                    LOGGER.info("MsalServiceException " + exception.getMessage());
//                    /* Exception when communicating with the STS, likely config issue */
//                } else if (exception instanceof MsalUiRequiredException) {
//                    LOGGER.info("MsalUiRequiredException " + exception.getMessage());
//                    /* Tokens expired or no session, retry with interactive */
//                }
//            }
//        };
//    }
//
//    private void sendTokenToServer(String requestBody) {
//        String token = AppSettings.getToken(this.getApplicationContext());
//        Rollbar.instance().info(null, "Sending: x-akumina-auth-id=" + token);
//
//        Map<String, String> extraHeader = new HashMap<>();
//        extraHeader.put("Content-Type", "application/json");
//        extraHeader.put("x-akumina-auth-id", token);
//
//        HttpUtils httpUtils = new HttpUtils(getApplicationContext());
//        httpUtils.post(URLS.APP_MANAGER_URL, requestBody, extraHeader, new ResponseHandler() {
//            @Override
//            public void handleResponse(String token) {
//                String oldToken = AppSettings.getOldToken(getApplicationContext());
//                Rollbar.instance().info("Old = " + oldToken + "   New = " + token);
//                AppSettings.saveToken(token, getApplicationContext());
//                displayMainView();
//            }
//
//            @Override
//            public void handleError(VolleyError e) {
//                Rollbar.instance().error(e, "Error while connect SP " + URLS.APP_MANAGER_URL);
//                LOGGER.log(Level.SEVERE, "Error ", e.fillInStackTrace());
//                showMessage("Unable to connect the server, try later");
//            }
//        });
//    }
}
