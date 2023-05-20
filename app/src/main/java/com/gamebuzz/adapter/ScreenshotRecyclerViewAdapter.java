package com.gamebuzz.adapter;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gamebuzz.R;
import com.gamebuzz.model.GameScreenshot;

import java.util.List;

public class ScreenshotRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = ScreenshotRecyclerViewAdapter.class.getSimpleName();

    private final List<GameScreenshot> gameScreenshotList;
    private final Application application;

    public ScreenshotRecyclerViewAdapter(List<GameScreenshot> gameScreenshotList, Application application) {
        this.gameScreenshotList = gameScreenshotList;
        this.application = application;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.screenshot_list_item, parent, false);

        return new ScreenshotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ScreenshotViewHolder) holder).bind(gameScreenshotList.get(position));
    }

    @Override
    public int getItemCount() {
        if (gameScreenshotList != null) {
            return gameScreenshotList.size();
        }
        return 0;
    }

    public class ScreenshotViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ScreenshotViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.screenshot_imageView);
        }

        public void bind(GameScreenshot gameScreenshot) {

            Log.e(TAG, "https:" + gameScreenshot.getUrl().replace("t_thumb", "t_screenshot_big"));

            Glide.with(application)
                    .load("https:" + gameScreenshot.getUrl().replace("t_thumb", "t_screenshot_big"))
                    .into(imageView);
        }
    }

}
