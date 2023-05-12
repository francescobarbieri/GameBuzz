package com.gamebuzz.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gamebuzz.R;

public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button changeUsernameButton = view.findViewById(R.id.change_username_button);
        Button changePasswordButton = view.findViewById(R.id.change_password_button);
        Button logoutButton = view.findViewById(R.id.logout_button);
        Button sendFeedbackButton = view.findViewById(R.id.send_feedback_button);

        // Set onClickListeners for the buttons
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch a dialog that allows the user to enter their new username
                final EditText input = new EditText(getActivity());
                input.setHint("Enter new username");

                new AlertDialog.Builder(getActivity())
                        .setTitle("Change Username")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Get the new username entered by the user
                                String newUsername = input.getText().toString();

                                // Update the username in your app's database or shared preferences
                                /* Here is an example of how to update a value in shared preferences:
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username", newUsername);
                                editor.apply();*/

                                // Display a toast message indicating that the username has been changed
                                Toast.makeText(getActivity(), "Username changed to " + newUsername, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to handle password change
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to handle logout
            }
        });

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to handle feedback submission
            }
        });

        return view;
    }
}