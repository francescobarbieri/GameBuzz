package com.gamebuzz.ui.welcome;

import android.os.Bundle;

import static com.gamebuzz.util.Constants.EMAIL_ADDRESS;
import static com.gamebuzz.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.gamebuzz.util.Constants.ID_TOKEN;
import static com.gamebuzz.util.Constants.MINIMUM_PASSWORD_LENGTH;
import static com.gamebuzz.util.Constants.PASSWORD;
import static com.gamebuzz.util.Constants.USER_COLLISION_ERROR;
import static com.gamebuzz.util.Constants.WEAK_PASSWORD_ERROR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.apache.commons.validator.routines.EmailValidator;

import com.gamebuzz.R;
import com.gamebuzz.data.repository.user.IUserRepository;
import com.gamebuzz.databinding.FragmentSignupBinding;
import com.gamebuzz.model.Result;
import com.gamebuzz.model.User;
import com.gamebuzz.util.DataEncryptionUtil;
import com.gamebuzz.util.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SignupFragment extends Fragment {

    private static final String TAG = SignupFragment.class.getSimpleName();

    private FragmentSignupBinding binding;
    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;

    public SignupFragment() {
        // Required empty public constructor
    }
    public static SignupFragment newInstance() { return new SignupFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.buttonRegistration.setOnClickListener(v -> {
            String email = binding.textInputEditTextEmail.getText().toString().trim();
            String password = binding.textInputEditTextPassword.getText().toString().trim();

            if(isEmailOkay(email) & isPasswordOkay(password)) {
                // TODO: binding.progressBar.setVisibility(View.VISIBLE);
                if(!userViewModel.isAuthenticationError()) {
                    Log.e(TAG, "First if");
                    userViewModel.getUserMutableLiveData(email, password, false).observe(
                            getViewLifecycleOwner(), result -> {
                                Log.e(TAG, "Observe working");
                                if(result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    saveLoginData(email, password, user.getIdToken());
                                    userViewModel.setAuthenticationError(false);
                                    Navigation.findNavController(view).navigate(
                                            R.id.navigate_to_appActivity
                                    );
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()), Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email, password, false);
                }
                // TODO: binding.progressBar.setVisibility(View.GONE);
            } else {
                userViewModel.setAuthenticationError(true);
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        "Check the data you inserted", Snackbar.LENGTH_SHORT).show();
            }
        });

        final Button buttonSignup = view.findViewById(R.id.button_to_login);

        buttonSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.navigate_to_loginFragment);
        });
    }

    private boolean isPasswordOkay (String password) {
        if(password.isEmpty() || password.length() < MINIMUM_PASSWORD_LENGTH) {
            binding.textInputEditTextPassword.setError("Password should be at least 6 characters");
            return false;
        } else {
            return true;
        }
    }

    private boolean isEmailOkay (String email)  {
        if(!EmailValidator.getInstance().isValid(email)) {
            binding.textInputLayoutEmail.setError("Insert a valid email address");
            return false;
        } else {
            return true;
        }
    }

    private String getErrorMessage(String message) {
        switch(message) {
            case WEAK_PASSWORD_ERROR:
                return "Password should be at least 6 characters";
            case USER_COLLISION_ERROR:
                return "The email already exists";
            default:
                return "Unexpected error";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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