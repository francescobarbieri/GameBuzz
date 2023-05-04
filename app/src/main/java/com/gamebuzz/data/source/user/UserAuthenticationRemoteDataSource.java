package com.gamebuzz.data.source.user.user;

import static com.gamebuzz.util.Constants.INVALID_CREDENTIALS_ERROR;
import static com.gamebuzz.util.Constants.INVALID_USER_ERROR;
import static com.gamebuzz.util.Constants.UNEXPECTED_ERROR;
import static com.gamebuzz.util.Constants.USER_COLLISION_ERROR;
import static com.gamebuzz.util.Constants.WEAK_PASSWORD_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gamebuzz.model.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserAuthenticationRemoteDataSource extends com.gamebuzz.data.source.user.user.BaseUserAuthenticationRemoteDataSource  {
    private static final String TAG = UserAuthenticationRemoteDataSource.class.getSimpleName();

    private final FirebaseAuth firebaseAuth;


    public UserAuthenticationRemoteDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public User getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) {
            return null;
        } else {
            return new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid());
        }
    }

    @Override
    public void logout() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    Log.d(TAG, "User logged out");
                    userResponseCallback.onSuccessLogout();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseAuth.signOut();
    }

    @Override
    public void signUp (String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    userResponseCallback.onSuccessFromAuthentication(new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid()));
                } else {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }

    @Override
    public void signInWithGoogle (String idToken) {
        if(idToken != null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser != null) {
                        userResponseCallback.onSuccessFromAuthentication(new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getUid()));
                    } else {
                        userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            });
        }
    }

    @Override
    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    userResponseCallback.onSuccessFromAuthentication(new User(firebaseUser.getDisplayName(), email, firebaseUser.getUid()));
                } else {
                    userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                userResponseCallback.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }


    private String getErrorMessage(Exception exception) {
        if(exception instanceof FirebaseAuthWeakPasswordException) {
            return WEAK_PASSWORD_ERROR;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return INVALID_CREDENTIALS_ERROR;
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return INVALID_USER_ERROR;
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return USER_COLLISION_ERROR;
        }
        return UNEXPECTED_ERROR;
    }
}
