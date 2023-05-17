package com.gamebuzz.data.source.games;

import static com.gamebuzz.util.Constants.FIREBASE_FAVORITE_GAMES_COLLECTION;
import static com.gamebuzz.util.Constants.FIREBASE_REALTIME_DATABASE;
import static com.gamebuzz.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gamebuzz.model.Game;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoriteGamesDataSource extends BaseFavoriteGamesDataSource {

    private static final String TAG = FavoriteGamesDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;

    private final String idToken;

    public FavoriteGamesDataSource (String idToken) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
        this.idToken = idToken;
    }

    @Override
    public void getFavoriteGames() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).child(FIREBASE_FAVORITE_GAMES_COLLECTION).get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
            } else {
                Log.d(TAG, "Successful read: " + task.getResult().getValue());

                List<Game> gameList = new ArrayList<>();
                for(DataSnapshot ds : task.getResult().getChildren()) {
                    Game game = ds.getValue(Game.class);
                    game.setSynchronized(true);
                    gameList.add(game);
                }

                gamesCallback.onSuccessFromCloudReading(gameList);
            }
        });
    }


    @Override
    public void addFavoriteGames(Game game) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).child(FIREBASE_FAVORITE_GAMES_COLLECTION).child(String.valueOf(game.hashCode())).setValue(game).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                game.setSynchronized(true);
                gamesCallback.onSuccessFromCloudWriting(game);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                gamesCallback.onFailureFromCloud(e);
            }
        });
    }

    @Override
    public void synchronizeFavoriteGames(List<Game> notSynchronizedList) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).child(FIREBASE_FAVORITE_GAMES_COLLECTION).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                List<Game> gameList = new ArrayList<>();
                for(DataSnapshot ds : task.getResult().getChildren()) {
                    Game game = ds.getValue(Game.class);
                    game.setSynchronized(true);
                    gameList.add(game);
                }

                gameList.addAll(notSynchronizedList);

                for(Game game : gameList) {
                    databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).child(FIREBASE_FAVORITE_GAMES_COLLECTION).child(String.valueOf(game.hashCode())).setValue(game).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            game.setSynchronized(true);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void deleteFavoriteGames(Game game) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).child(FIREBASE_FAVORITE_GAMES_COLLECTION).child(String.valueOf(game.hashCode())).removeValue().addOnSuccessListener( aVoid -> {
            game.setSynchronized(false);
            gamesCallback.onSuccessFromCloudWriting(game);
        }).addOnFailureListener( e -> {
            gamesCallback.onFailureFromCloud(e);
        });
    }


}
