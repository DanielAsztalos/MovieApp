package com.example.movieapp.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.helpers.Encrypt;
import com.example.movieapp.tasks.ChangePasswordAsyncTask;

/**
 * This DialogFragment is displayed when a user clicks on the change password button
 */
public class ChangePassDialogFragment extends DialogFragment {


    public ChangePassDialogFragment() {
        // Required empty public constructor
    }

    private void changePass() {
        // get old and new passwords
        String oldPass = ((EditText) getDialog().findViewById(R.id.et_pass_old)).getText().toString();
        String newPass = ((EditText) getDialog().findViewById(R.id.et_pass_new)).getText().toString();

        // if new password is too short display error message
        if(newPass.length() < 6) {
            Toast.makeText(getContext(), R.string.passShort, Toast.LENGTH_LONG).show();
            return;
        }

        // run the password change task
        ChangePasswordAsyncTask task = new ChangePasswordAsyncTask(getActivity());
        task.execute(Encrypt.md5(newPass), Encrypt.md5(oldPass));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_change_pass_dialog, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changePass();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

}
