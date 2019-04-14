package com.example.clientformusicserver.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.clientformusicserver.model.converter.ListSongConverter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


@Entity
public class Album implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int mId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String mName;

    @ColumnInfo(name = "release")
    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("songs")
    @ColumnInfo(name = "songs")
    @TypeConverters({ListSongConverter.class})
    private List<Song> mSongs;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }


}
