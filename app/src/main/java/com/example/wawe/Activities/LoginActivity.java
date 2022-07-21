package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.wawe.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // checks to see if a user is already logged in
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        loadingBar = new ProgressDialog(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.isOnline(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this, "Must be connected to the internet to login", Toast.LENGTH_SHORT).show();
                    setLoginCredentialsToNull();
                }
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        // when a new user wants to sign up
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void loginUser(String username, String password){
        if (TextUtils.isEmpty(etUsername.getText().toString())){
            Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(etPassword.getText().toString())){
            Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please wait, while we are logging you in");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Toast.makeText(LoginActivity.this, "Error Logging in. Make sure your credentials are correct", Toast.LENGTH_SHORT).show();
                        setLoginCredentialsToNull();
                        loadingBar.dismiss();
                        return;
                    }
                    goMainActivity();
                    loadingBar.dismiss();
                }
            });
        }
    }

    // when the user successfully logs in the program then goes to the MainActivity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void setLoginCredentialsToNull () {
        etPassword.setText(null);
        etUsername.setText(null);
    }
}