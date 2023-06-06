package com.gamebuzz.ui.welcome;

import static com.gamebuzz.util.Constants.EMAIL_ADDRESS;
import static com.gamebuzz.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.gamebuzz.util.Constants.ID_TOKEN;
import static com.gamebuzz.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.gamebuzz.util.Constants.INVALID_USER_ERROR;
import static com.gamebuzz.util.Constants.MINIMUM_PASSWORD_LENGTH;
import static com.gamebuzz.util.Constants.PASSWORD;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private UserViewModel userViewModel;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private DataEncryptionUtil dataEncryptionUtil;

    public LoginFragment() {
        // Required empty public constructor
    }



    public static LoginFragment newInstance() { return new LoginFragment(); }

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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @NonNull Bundle savedInstanceState) {
        final Button buttonSignup = view.findViewById(R.id.button_to_signin);

        textInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = view.findViewById(R.id.textInputLayoutPassword);

        final Button buttonLogin = view.findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener( v -> {
            String email = textInputLayoutEmail.getEditText().getText().toString().trim();
            String password = textInputLayoutPassword.getEditText().getText().toString().trim();

            if(isEmailOkay(email) & isPasswordOkay(password)) {
                if(!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email, password, true).observe(
                            getViewLifecycleOwner(), result -> {
                                if(result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    saveLoginData(email, password, user.getIdToken());
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
            } else {
                Snackbar.make(requireActivity().findViewById(android.R.id.content), "Check the data you inserted", Snackbar.LENGTH_SHORT).show();
            }
        });

        buttonSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.navigate_to_signupFragment);
        });
    }


    private boolean isPasswordOkay (String password) {
        if(password.isEmpty() || password.length() < MINIMUM_PASSWORD_LENGTH) {
            textInputLayoutPassword.setError("Password should be at least 6 characters");
            return false;
        } else {
            return true;
        }
    }

    private boolean isEmailOkay (String email)  {
        if(!EmailValidator.getInstance().isValid(email)) {
            textInputLayoutEmail.setError("Insert a valid email address");
            return false;
        } else {
            return true;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.setAuthenticationError(false);
    }

    private void saveLoginData(String email, String password, String idToken) {
        try {
            dataEncryptionUtil.writeSecretDaatWithEncryptedSharedPreferences(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS, email);
            dataEncryptionUtil.writeSecretDaatWithEncryptedSharedPreferences(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD, password);
            dataEncryptionUtil.writeSecretDaatWithEncryptedSharedPreferences(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN, idToken);

        } catch (GeneralSecurityException | IOException e ) {
            e.printStackTrace();
        }
    }

}