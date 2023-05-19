package com.j.jOSWallpapers;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class lock extends Fragment implements WallpaperSelectListener {
    private RecyclerView wallpaperRecyclerView;
    private WallpaperGalleryRecyclerAdapter wallpaperGalleryRecyclerAdapter;
    private List<Wallpaper> wallpapers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wallpapers = new ArrayList<>();
        wallpapers.add(new Wallpaper("Default", "https://raw.githubusercontent.com/jf916/jOS-Wallpaper-Files/main/Lock_screen/0.jpeg"));
        wallpapers.add(new Wallpaper("Mountain", "https://images.pexels.com/photos/1261728/pexels-photo-1261728.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        wallpapers.add(new Wallpaper("Field", "https://images.pexels.com/photos/35857/amazing-beautiful-breathtaking-clouds.jpg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        wallpapers.add(new Wallpaper("Clouds", "https://images.pexels.com/photos/2088205/pexels-photo-2088205.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));
        wallpapers.add(new Wallpaper("Condensation", "https://images.pexels.com/photos/891030/pexels-photo-891030.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"));

        wallpaperGalleryRecyclerAdapter = new WallpaperGalleryRecyclerAdapter(this);
        wallpaperGalleryRecyclerAdapter.setWallpapers(wallpapers);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock, container, false);

        wallpaperRecyclerView = view.findViewById(R.id.fragment_listwallpapers_recyclerView);
        wallpaperRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        wallpaperRecyclerView.setAdapter(wallpaperGalleryRecyclerAdapter);

        view.findViewById(R.id.skip_lock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.wallpaper", "com.android.wallpaper.picker.CategoryPickerActivity"));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Define what your app should do if no activity can handle the intent.
                    NavHostFragment.findNavController(lock.this)
                            .navigate(R.id.action_lock_to_exit_now);
                }

            }
        });

        return view;
    }

    private void setHomeScreenWallpaper(Bitmap bitmap) {
        try {
            WallpaperManager.getInstance(getContext()).setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCroppedHomeScreenWallpaper(Bitmap bitmap) {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                int wallpaperHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
                int wallpaperWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

                Point start = new Point(0, 0);
                Point end = new Point(bitmap.getWidth(), bitmap.getHeight());

                if (bitmap.getWidth() > wallpaperWidth) {
                    start.x = (bitmap.getWidth() - wallpaperWidth) / 2;
                    end.x = start.x + wallpaperWidth;
                }

                if (bitmap.getHeight() > wallpaperHeight) {
                    start.y = (bitmap.getHeight() - wallpaperHeight) / 2;
                    end.y = start.y + wallpaperHeight;
                }

                wallpaperManager.setBitmap(bitmap, new Rect(start.x, start.y, end.x, end.y), false);
            } else {
                wallpaperManager.setBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLockScreenWallpaper(Bitmap bitmap) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                WallpaperManager.getInstance(getContext()).setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWallpaperSelect(Wallpaper wallpaper) {

        Glide.with(getContext())
                .asBitmap()
                .load(wallpaper.getImageUri())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        setLockScreenWallpaper(resource);
                        Toast.makeText(getContext(), wallpaper.getTitle() + " has been selected as the lock screen wallpaper", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setComponent(new ComponentName("com.android.wallpaper", "com.android.wallpaper.picker.CategoryPickerActivity"));
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // Define what your app should do if no activity can handle the intent.
                            NavHostFragment.findNavController(lock.this)
                                    .navigate(R.id.action_lock_to_exit_now);
                        }
                    }
                });
    }
}