package com.gamebuzz.ui.main;

import static com.gamebuzz.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gamebuzz.R;
import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.model.Result;
import com.gamebuzz.ui.welcome.UserViewModel;
import com.gamebuzz.ui.welcome.UserViewModelFactory;
import com.gamebuzz.util.DataEncryptionUtil;
import com.gamebuzz.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {

    private static String TAG = SettingsFragment.class.getSimpleName();

    private Button logoutButton;

    private UserViewModel userViewModel;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutButton = view.findViewById(R.id.logout_button);

        logoutButton.setOnClickListener( v -> {
            userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                Log.e(TAG, result.toString());
                if(result instanceof Result.UserResponseSuccess) {
                    DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
                    dataEncryptionUtil.deleteAll(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME);
                    Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_authActivity);
                } else {
                    Snackbar.make(view, "Unexpected error", Snackbar.LENGTH_SHORT);
                }
            });
        });
    }

}