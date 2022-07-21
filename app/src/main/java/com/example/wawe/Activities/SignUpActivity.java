package com.example.wawe.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.wawe.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etRetypePassword;
    private EditText etLocation;
    private Button btnSignUp;
    private Spinner spDietaryRestriction;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadingBar = new ProgressDialog(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRetypePassword = findViewById(R.id.etRetypePassword);
        etLocation = findViewById(R.id.etLocation);
        spDietaryRestriction = findViewById(R.id.spDietaryRestriction);
        String[] vegOption = new String[]{"", "vegetarian", "vegan", "gluten-free"};
        ArrayAdapter<String> adapterVeg = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vegOption);
        spDietaryRestriction.setAdapter(adapterVeg);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.isOnline(SignUpActivity.this)){
                    Toast.makeText(SignUpActivity.this, "Must be connected to the internet to create account", Toast.LENGTH_SHORT).show();
                    setSignUpCredentialsToNull();
                }
                signUpUser();
            }
        });

    }

    private void signUpUser() {
        if (TextUtils.isEmpty(etUsername.getText().toString())){
            Toast.makeText(SignUpActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(etPassword.getText().toString())){
            Toast.makeText(SignUpActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(etLocation.getText().toString())){
            Toast.makeText(SignUpActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
        else if (!etPassword.getText().toString().equals(etRetypePassword.getText().toString())){
            Toast.makeText(SignUpActivity.this, "Please make sure your passwords match", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Please wait, while we are creating your account");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            ParseUser user = new ParseUser();
            user.setUsername(etUsername.getText().toString());
            user.setPassword(etPassword.getText().toString());
            user.put("location", etLocation.getText().toString());
            if (!spDietaryRestriction.getSelectedItem().toString().equals("")) {
                user.put("dietaryRestriction", spDietaryRestriction.getSelectedItem().toString());
            }
            else {
                user.put("dietaryRestriction", " ");
            }
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(SignUpActivity.this, "Error Signing in. Make sure your credentials are correct", Toast.LENGTH_SHORT).show();
                        setSignUpCredentialsToNull();
                        return;
                    }
                    goLoginActivity();
                    loadingBar.dismiss();
                }
            });
        }
    }

    // logs new user in
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void setSignUpCredentialsToNull() {
        etLocation.setText(null);
        etPassword.setText(null);
        etRetypePassword.setText(null);
        etUsername.setText(null);
        spDietaryRestriction.setSelection(0);
    }
}