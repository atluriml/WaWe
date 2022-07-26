package com.example.wawe.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wawe.R;

public class GroupDialogFragment extends AppCompatDialogFragment {
    private EditText etGroupName;
    private EditText etGroupDescription;
    private EditText etGroupLocation;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Create New Group")
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
        etGroupName = view.findViewById(R.id.etGroupName);
        etGroupDescription = view.findViewById(R.id.etGroupDescription);
        etGroupLocation = view.findViewById(R.id.etGroupLocation);

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
                String name = etGroupName.getText().toString();
                String description = etGroupDescription.getText().toString();
                String location = etGroupLocation.getText().toString();
                if(!boxContainsEmptyField(name, description, location)) {
                    listener.applyTexts(name, description, location);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private boolean boxContainsEmptyField(String name, String description, String location) {
        if (name.trim().isEmpty()){
            Toast.makeText(getContext(), "Please enter group name", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (description.trim().isEmpty()) {
            Toast.makeText(getContext(), "Please enter group description", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (location.trim().isEmpty()){
            Toast.makeText(getContext(), "Please enter group location", Toast.LENGTH_SHORT).show();
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
            Log.e("d", e.toString());
            throw new ClassCastException(context.toString() +
                    "must implement DialogListener");
        }
    }

    public interface DialogListener {
        void applyTexts(String name, String description, String location);
    }
}
