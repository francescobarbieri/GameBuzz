package com.gamebuzz.ui.welcome;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gamebuzz.R;
import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.model.Result;
import com.gamebuzz.model.User;
import com.gamebuzz.util.DataEncryptionUtil;
import com.gamebuzz.util.ServiceLocator;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.material.snackbar.Snackbar;

import static com.gamebuzz.util.Constants.EMAIL_ADDRESS;
import static com.gamebuzz.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.gamebuzz.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.gamebuzz.util.Constants.INVALID_USER_ERROR;
import static com.gamebuzz.util.Constants.PASSWORD;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class WelcomeFragment extends Fragment {

    private final static String TAG = WelcomeFragment.class.getSimpleName();

    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;
    private SignInClient oneTapClient;

    private BeginSignInRequest signInRequest;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @NonNull Bundle savedInstanceState) {

        try {
            String email = dataEncryptionUtil.readSecretDataWithEncryptedSharePreferences(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS);
            String password = dataEncryptionUtil.readSecretDataWithEncryptedSharePreferences(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD);

            if(email != null && password != null) {
                if(!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email, password, true).observe(
                            getViewLifecycleOwner(), result -> {
                                if(result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    userViewModel.setAuthenticationError(false);
                                    // TODO: retrieveUserInformationAndStartActivity(user, R.id.navigate_to_newsPreferenceActivity);
                                    Navigation.findNavController(view).navigate(
                                            R.id.navigate_to_appActivity
                                    );
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content), getErrorMessage(((Result.Error) result).getMessage()), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email, password, true);
                }
            }

        } catch (GeneralSecurityException | IOException e ) {
            e.printStackTrace();
        }

        final Button buttonSignup = view.findViewById(R.id.button_to_signin);
        final Button buttonLogin = view.findViewById(R.id.button_to_login);

        buttonSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.navigate_to_signupFragment);
        });

        buttonLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.navigate_to_loginFragment);
        });
    }

    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case INVALID_CREDENTIALS_ERROR:
                return "Invalid credential error";
            case INVALID_USER_ERROR:
                return "Invalid user error";
            default:
                return "Unknown error";
        }
    }

}