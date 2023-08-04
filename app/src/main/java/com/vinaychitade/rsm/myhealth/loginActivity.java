package com.vinaychitade.rsm.myhealth;

import static com.vinaychitade.rsm.myhealth.Emergencybtn_Activity.MY_PERMISSIONS_REQUEST_SEND_SMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.library.permission_handler.PermissionHandler;
import com.library.permission_handler.PermissionsHandler;

import java.security.Permission;
import java.util.ArrayList;


public class loginActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 123;
    com.google.android.gms.common.SignInButton btnlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin = findViewById(R.id.btnlogin);

        String[] permissions = {android.Manifest.permission.CALL_PHONE, android.Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA};
        String rationale = "Please provide permission for the App to work normally.";
        PermissionsHandler.Options options = new PermissionsHandler.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        PermissionsHandler.requestPermission(this,permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onPermissionGranted() {
                 Toast.makeText(loginActivity.this, "Permissions granted.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(Context context, ArrayList<String> deniedPermissions) {
                Toast.makeText(loginActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }

        });




        btnlogin.setOnClickListener(view -> onGoogleSignInClick());

        Toast.makeText(this, "Sign in With Google To Continue", Toast.LENGTH_SHORT).show();


        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Use your Web Client ID
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void onGoogleSignInClick() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String displayName = account.getDisplayName();
            Toast.makeText(this, "hello "+displayName, Toast.LENGTH_SHORT).show();
            // Google Sign-In successful, get the user's Google ID token
            String idToken = account.getIdToken();
            firebaseAuthWithGoogle(idToken);


        } catch (ApiException e) {
            // Google Sign-In failed, handle the error (e.g., display error message)
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Firebase Authentication successful, proceed to the next screen
                        // You can store the user credentials (e.g., UID and token) securely here
                        // and implement auto-login for subsequent app launches
                        Intent logintohome= new Intent(loginActivity.this,MainActivity.class);
                        startActivity(logintohome);
                        finish();

                    } else {
                        // Firebase Authentication failed, handle the error (e.g., display error message)
                        Toast.makeText(this, "User Authentication failed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}