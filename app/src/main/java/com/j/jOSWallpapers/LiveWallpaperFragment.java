package com.j.jOSWallpapers;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class LiveWallpaperFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livewallpaper, container, false);

        view.findViewById(R.id.fragment_livewallpaper_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getActivity(), MyWallpaperService.class));
                startActivity(intent);
                NavHostFragment.findNavController(LiveWallpaperFragment.this)
                        .navigate(R.id.action_liveWallpaperFragment_to_wallpapersListFragment);
            }
        });
        view.findViewById(R.id.home_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LiveWallpaperFragment.this)
                        .navigate(R.id.action_liveWallpaperFragment_to_wallpapersListFragment);
            }
        });

        return view;
    }
}