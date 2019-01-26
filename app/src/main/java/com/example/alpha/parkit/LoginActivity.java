package com.example.alpha.parkit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.alpha.parkit.R;

public class LoginActivity extends Activity {
    EditText PasswordEt;
    EditText UsernameEt;
    Button bsignup;
    Button btnLogin;
    String result;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;


    public void updateUI(FirebaseUser user) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    class C01841 implements OnClickListener {
        C01841() {
        }

        public void onClick(View v) {
            String username = UsernameEt.getText().toString();
            String password = PasswordEt.getText().toString();
            if (username.equals("") || password.equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter all fields", Toast.LENGTH_LONG).show();
            } else {
                signIn(username, password);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            //Toast.makeText(getApplicationContext(), currentUser.getUid().toString(), Toast.LENGTH_SHORT).show();
            updateUI(currentUser);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsernameEt = (EditText) findViewById(R.id.etUserName);
        PasswordEt = (EditText) findViewById(R.id.etPassword);
        getWindow().setSoftInputMode(3);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new C01841());
        bsignup = (Button) findViewById(R.id.btn_signup);
        bsignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in ...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public void signIn(final String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.hide();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Logged in",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
