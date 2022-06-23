package com.example.wawe.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wawe.Activities.FavoritesActivity;
import com.example.wawe.R;
import com.example.wawe.User;
import com.example.wawe.Activities.VisitedRestaurantsActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private Button btnCaptureImage;
    private String photoFileName = "photo.jpg";
    private File photoFile;

    private TextView tvProfileUsername;
    private ImageView ivProfileImage;
    private TextView tvProfileLocation;
    private Button btnFavorites;
    private Button btnVisited;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        tvProfileLocation = view.findViewById(R.id.tvLocation);
        tvProfileUsername = view.findViewById(R.id.tvUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnFavorites = view.findViewById(R.id.btnFavorites);
        btnVisited = view.findViewById(R.id.btnVisitedRestaurants);
        btnCaptureImage = view.findViewById(R.id.btnProfileImage);

        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("profileImage");

        if (profileImage == null){
            Glide.with(this).load(R.drawable.instagram_user_outline_24).into(ivProfileImage);
        }
        else  {
            Glide.with(this).load(profileImage.getUrl()).into(ivProfileImage);
        }

        tvProfileUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvProfileLocation.setText(ParseUser.getCurrentUser().getString("location"));

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FavoritesActivity.class);
                photoFile = getPhotoFileUri(photoFileName);
                getContext().startActivity(intent);
            }
        });

        btnVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VisitedRestaurantsActivity.class);
                getContext().startActivity(intent);
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.example.wawe", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
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
                ivProfileImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    // updates user's profile image to parse
    private void saveProfilePicture(Bitmap photoFile) {
        Log.d(TAG, String.valueOf(photoFile));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoFile.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

        ParseFile newProfileImage = new ParseFile(byteArrayOutputStream.toByteArray());
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put("profileImage", newProfileImage);

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving post!", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!");
                ivProfileImage.setImageBitmap(photoFile);
            }
        });
    }

}