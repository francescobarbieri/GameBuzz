package com.gamebuzz.ui.welcome;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import static com.gamebuzz.util.Constants.EMAIL_ADDRESS;
import static com.gamebuzz.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static com.gamebuzz.util.Constants.ID_TOKEN;
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

    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    private ActivityResultContracts.StartIntentSenderForResult startIntentSenderForResult;

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

        oneTapClient = Identity.getSignInClient(requireActivity());

        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true).setServerClientId(getString(R.string.default_web_client_id_2))
                        .setFilterByAuthorizedAccounts(false).build())
                .setAutoSelectEnabled(true)
                .build();

        startIntentSenderForResult = new ActivityResultContracts.StartIntentSenderForResult();

        activityResultLauncher = registerForActivityResult(startIntentSenderForResult, activityResult -> {
            if(activityResult.getResultCode() == Activity.RESULT_OK) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(activityResult.getData());
                    String idToken = credential.getGoogleIdToken();

                    if(idToken != null) {
                        userViewModel.getGoogleUserMutableLiveData(idToken).observe(getViewLifecycleOwner(), authResult -> {
                            if (authResult.isSuccess()) {
                                User user = ((Result.UserResponseSuccess) authResult).getData();
                                saveLoginData(user.getEmail(), null, user.getIdToken());
                                userViewModel.setAuthenticationError((false));
                                /*
                                Navigation.findNavController(view).navigate(
                                        R.id.navigate_to_appActivity
                                );

                                 */
                            } else {
                                userViewModel.setAuthenticationError(true);
                                Snackbar.make(requireActivity().findViewById(android.R.id.content), getErrorMessage(((Result.Error) authResult).getMessage()), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (ApiException e) {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), "Unexpected Error", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

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

        final Button loginWithGoogleButton = view.findViewById(R.id.login_with_google_button);

        loginWithGoogleButton.setOnClickListener( v -> oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult beginSignInResult) {
                        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(beginSignInResult.getPendingIntent()).build();
                        activityResultLauncher.launch(intentSenderRequest);
                    }
                })
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getMessage());

                        Snackbar.make(requireActivity().findViewById(android.R.id.content), "No google accoutns have been found", Snackbar.LENGTH_SHORT).show();
                    }
                })
        );

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