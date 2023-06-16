package com.gamebuzz.data.source.user;

import static com.gamebuzz.util.Constants.FIREBASE_FAVORITE_GAMES_COLLECTION;
import static com.gamebuzz.util.Constants.FIREBASE_REALTIME_DATABASE;
import static com.gamebuzz.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gamebuzz.model.Game;
import com.gamebuzz.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDataRemoteDataSource  extends BaseUserDataRemoteDataSource{

    private static final String TAG = UserDataRemoteDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;

    public UserDataRemoteDataSource() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
    }

    @Override
    public void saveUserData(User user) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Log.d(TAG, "User already present in Firebase Realtime DB");
                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                } else {
                    Log.d(TAG, "User not present in Realtime DB");
                    databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            userResponseCallback.onSuccessFromRemoteDatabase(user);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Errore 1");
                            userResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Errore 2");
                Log.e(TAG, "Errore: " + error.getMessage());
                userResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
            }
        });
    }

    @Override
    public void getUserFavoriteGames(String idToken) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).child(FIREBASE_FAVORITE_GAMES_COLLECTION).get().addOnCompleteListener( task -> {
            if(!task.isSuccessful()) {
                Log.d(TAG, "Error getting data", task.getException());
                userResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
            } else {
                Log.d(TAG, "Successful read: " + task.getResult().getValue());

                List<Game> gameList = new ArrayList<>();
                for(DataSnapshot ds : task.getResult().getChildren()) {
                    Game game = ds.getValue(Game.class);
                    gameList.add(game);
                }

                userResponseCallback.onSuccessFromRemoteDatabase(gameList);
            }
        });
    }

}
