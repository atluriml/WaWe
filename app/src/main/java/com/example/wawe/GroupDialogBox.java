package com.example.wawe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class GroupDialogBox extends AppCompatDialogFragment {
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
                        String name = etGroupName.getText().toString();
                        String description = etGroupDescription.getText().toString();
                        String location = etGroupLocation.getText().toString();
                        listener.applyTexts(name, description, location);
                    }
                });

        etGroupName = view.findViewById(R.id.etGroupName);
        etGroupDescription = view.findViewById(R.id.etGroupDescription);
        etGroupLocation = view.findViewById(R.id.etGroupLocation);

        return builder.create();
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
        void applyTexts(String name, String description, String location);
    }
}
