package com.example.wawe.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wawe.R;
import com.example.wawe.fragments.ProfileFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private Button btnCaptureImage;
    private String photoFileName = "photo.jpg";
    private File photoFile;

    private EditText etUsername;
    private EditText etPassword;
    private EditText etRetypePassword;
    private EditText etLocation;
    private Button btnUpdateProfile;
    private Spinner spDietaryRestriction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnCaptureImage = findViewById(R.id.btnProfileImage);

        etUsername = findViewById(R.id.etUsernameSettings);
        etPassword = findViewById(R.id.etPasswordSettings);
        etRetypePassword = findViewById(R.id.etRetypePasswordSettings);
        etLocation = findViewById(R.id.etLocationSettings);
        spDietaryRestriction = findViewById(R.id.spDietaryRestrictionSettings);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        String[] vegOption = new String[]{"", "vegetarian", "vegan", "gluten-free", "none"};
        ArrayAdapter<String> adapterVeg = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vegOption);
        spDietaryRestriction.setAdapter(adapterVeg);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.isOnline(SettingsActivity.this)){
                    Toast.makeText(SettingsActivity.this, "Must be connected to the internet to change your profile", Toast.LENGTH_SHORT).show();
                    setPreferencesToNull();
                    return;
                }
                photoFile = getPhotoFileUri(photoFileName);
                launchCamera();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.isOnline(SettingsActivity.this)){
                    Toast.makeText(SettingsActivity.this, "Must be connected to the internet to change your profile", Toast.LENGTH_SHORT).show();
                    setPreferencesToNull();
                    return;
                }
                if (!etPassword.getText().toString().equals(etRetypePassword.getText().toString())){
                    setPreferencesToNull();
                    Toast.makeText(SettingsActivity.this, "Please make sure your passwords match", Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseUser user = ParseUser.getCurrentUser();
                    if (!checkStringEmpty(etUsername.getText().toString())){
                        user.setUsername(etUsername.getText().toString());
                    }
                    if (!checkStringEmpty(etPassword.getText().toString())){
                        user.setPassword(etPassword.getText().toString());
                    }
                    if (!checkStringEmpty(etLocation.getText().toString())){
                        user.put("location", etLocation.getText().toString());
                    }
                    if (checkStringEmpty(spDietaryRestriction.getSelectedItem().toString())) {
                        user.put("dietaryRestriction", " ");
                    }
                    else if (!checkStringEmpty(spDietaryRestriction.getSelectedItem().toString())) {
                        user.put("dietaryRestriction", spDietaryRestriction.getSelectedItem().toString());
                    }
                    else {
                        user.put("dietaryRestriction", " ");
                    }
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null){
                                Log.e(TAG, e + " rip");
                                Toast.makeText(SettingsActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                setPreferencesToNull();
                                Toast.makeText(SettingsActivity.this, "Profile Successfully Updated!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void setPreferencesToNull () {
        etLocation.setText(null);
        etPassword.setText(null);
        etRetypePassword.setText(null);
        etUsername.setText(null);
        spDietaryRestriction.setSelection(0);
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(this, "com.example.wawe", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                saveProfilePicture(takenImage);
                ProfileFragment.getIvProfileImage().setImageBitmap(takenImage);
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    // updates user's profile image to parse
    private void saveProfilePicture(Bitmap photoFile) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoFile.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        ParseFile newProfileImage = new ParseFile(byteArrayOutputStream.toByteArray());
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put("profileImage", newProfileImage);

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Toast.makeText(SettingsActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                ProfileFragment.getIvProfileImage().setImageBitmap(photoFile);
            }
        });
    }

    public boolean checkStringEmpty (String input) {
        if (input.isEmpty() || input.equals("none")) {
            return true;
        }
        return false;
    }
}