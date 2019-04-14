package com.example.clientformusicserver.album;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.clientformusicserver.R;
import com.example.clientformusicserver.model.Song;

class SongHolder extends RecyclerView.ViewHolder {
    private TextView mTvTitleOfSong, mTvDuration, mId;

    SongHolder(View itemView) {
        super(itemView);
        mId = itemView.findViewById(R.id.tv_id);
        mTvTitleOfSong = itemView.findViewById(R.id.tv_title_song);
        mTvDuration = itemView.findViewById(R.id.tv_duration);
    }

    void bind(Song item) {
        mId.setText(item.getId().toString());
        mTvTitleOfSong.setText(item.getName());
        mTvDuration.setText(item.getDuration());
    }
}