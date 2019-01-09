package com.example.alpha.parkit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.example.alpha.parkit.R;

import java.util.HashMap;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class SignupActivity extends AppCompatActivity {


    private TextInputLayout emaili;
    private EditText edit_email;

    private TextInputLayout passi;
    private EditText editpassword;

    private TextInputLayout cpassi;
    private EditText coneditpassword;

    private TextInputLayout mobi;
    private EditText phone;

    private Button buttonRegister;

    private TextView gologin;
    ProgressDialog progressDialog;


    private FirebaseAuth mAuth;
    private String tag = " Firebase-auth";

    class C01901 implements TextWatcher {
        C01901() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                emaili.setError(null);
                return;
            }
            emaili.setErrorEnabled(true);
            emaili.setError("Please Enter a Valid Email");
        }

        public void afterTextChanged(Editable s) {
            if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                emaili.setError(null);
                emaili.setErrorEnabled(false);
                return;
            }
            emaili.setErrorEnabled(true);
            emaili.setError("Please Enter a Valid Email");
        }
    }

    class C01912 implements TextWatcher {
        C01912() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() < 8) {
                passi.setErrorEnabled(true);
                passi.setError("Password length should be atlest 8 characters");
                return;
            }
            passi.setErrorEnabled(false);
            passi.setError(null);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    class C01923 implements TextWatcher {
        C01923() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editpassword.getText().toString().matches(coneditpassword.getText().toString())) {
                cpassi.setErrorEnabled(false);
                cpassi.setError(null);
                return;
            }
            cpassi.setErrorEnabled(true);
            cpassi.setError("Passwords do not match");
        }

        public void afterTextChanged(Editable s) {
            if (passi.getEditText().getText().toString().matches(s.toString())) {
                cpassi.setError(null);
                return;
            }
            cpassi.setErrorEnabled(true);
            cpassi.setError("Passwords do not match");
        }
    }

    class C01934 implements TextWatcher {
        C01934() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ((s.toString().startsWith("9") || s.toString().startsWith("8") || s.toString().startsWith("7") || s.toString().startsWith("6")) && s.length() == 10) {
                mobi.setError(null);
                return;
            }
            mobi.setErrorEnabled(true);
            mobi.setError("Invalid Mobile Number");
        }

        public void afterTextChanged(Editable s) {
            if (s.toString().startsWith("9") || s.toString().startsWith("8") || (s.toString().startsWith("7") && s.length() == 10)) {
                mobi.setError(null);
                return;
            }
            mobi.setErrorEnabled(true);
            mobi.setError("Invalid Mobile Number");
        }
    }

    class C01945 implements OnClickListener {
        C01945() {
        }

        public void onClick(View v) {
            registerUser();
        }
    }

    class C01956 implements OnClickListener {
        C01956() {
        }

        public void onClick(View v) {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        emaili = (TextInputLayout) findViewById(R.id.emaili);
        edit_email = (EditText) findViewById(R.id.input_email);

        passi = (TextInputLayout) findViewById(R.id.passi);
        editpassword = (EditText) findViewById(R.id.input_password1);

        cpassi = (TextInputLayout) findViewById(R.id.cpassi);
        coneditpassword = (EditText) findViewById(R.id.input_password2);

        mobi = (TextInputLayout) findViewById(R.id.mobi);
        phone = (EditText) findViewById(R.id.input_phone);

        buttonRegister = (Button) findViewById(R.id.btn_signup);

        gologin = (TextView) findViewById(R.id.gologin);

        emaili.getEditText().addTextChangedListener(new C01901());
        passi.getEditText().addTextChangedListener(new C01912());
        cpassi.getEditText().addTextChangedListener(new C01923());
        mobi.getEditText().addTextChangedListener(new C01934());
        buttonRegister.setOnClickListener(new C01945());
        gologin.setOnClickListener(new C01956());

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Logging in ...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void registerUser() {

        String uname = ((EditText) findViewById(R.id.input_fname)).getText().toString();
        String sedit_email = edit_email.getText().toString();
        String seditpassword = editpassword.getText().toString();
        String sconeditpassword = coneditpassword.getText().toString();
        String sphone = phone.getText().toString();

        if (sconeditpassword.matches("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!seditpassword.equals(sconeditpassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else if (sphone.length() != 10) {
            Toast.makeText(getApplicationContext(), "Please fill 10-digit Mobile Number", Toast.LENGTH_SHORT).show();
        } else {
            signUp(sedit_email, seditpassword, uname, sphone);
        }
    }


    public void signUp(final String email, String password, final String uname, final String phno) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(tag, "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Authentication Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore.getInstance()
                                    .collection("Users")
                                    .document(user.getUid())
                                    .set(new HashMap<String, Object>() {{
                                        put("uname", uname);
                                        put("phno", phno);
                                        put("mail", email);
                                    }})
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.hide();
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            } else {
                                                Toast.makeText(getApplicationContext(), "retry ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(tag, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
