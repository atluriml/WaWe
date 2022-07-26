package com.example.wawe.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.wawe.R;
import com.parse.ParseFile;
import com.parse.ParseUser;
import java.io.File;

public class PostDialogFragment extends AppCompatDialogFragment {

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private EditText etPostTitle;
    private EditText etPostDescription;
    public  ImageView ivProfileImage;
    private TextView tvUsername;
    private DialogListener listener;
    private ImageView ivPostImage;
    private Button btnCaptureImage;
    private File photoFile;
    private String photoFileName = "photo.jpg";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.post_dialog, null);

        builder.setView(view)
                .setTitle("Create New Post")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        etPostTitle = view.findViewById(R.id.etPostTitle);
        etPostDescription = view.findViewById(R.id.etPostDescription);
        tvUsername = view.findViewById(R.id.tvPostDialogUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("profileImage");
        if (profileImage == null){
            Glide.with(this).load(R.drawable.instagram_user_outline_24).into(ivProfileImage);
        }
        else  {
            Glide.with(this).load(profileImage.getUrl()).into(ivProfileImage);
        }

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
        return builder.create();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String title = etPostTitle.getText().toString();
                String description = etPostDescription.getText().toString();
                if(!boxContainsEmptyField(title, description)) {
                    listener.applyTexts(title, description, photoFile);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private boolean boxContainsEmptyField(String title, String description) {
        if (title.trim().isEmpty()){
            Toast.makeText(getContext(), "Please enter post name", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (description.trim().isEmpty()) {
            Toast.makeText(getContext(), "Please enter post description", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DialogListener");
        }
    }

    public interface DialogListener {
        void applyTexts(String title, String description, File photoFile);
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
                ivPostImage.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName){
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PostDialog");
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

}
