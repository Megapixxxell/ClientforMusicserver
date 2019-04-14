package com.example.clientformusicserver.album;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clientformusicserver.R;
import com.example.clientformusicserver.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongHolder> {

    @NonNull
    private final List<Song> mSongs = new ArrayList<>();


    @NonNull
    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_song, parent, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        Song song = mSongs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    void addData(List<Song> songs, boolean isRefresh){
        if (isRefresh) {
            mSongs.clear();
        }

        mSongs.addAll(songs);
        notifyDataSetChanged();
    }
}

