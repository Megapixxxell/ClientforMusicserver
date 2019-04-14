package com.example.clientformusicserver.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.clientformusicserver.model.Album;
import com.example.clientformusicserver.model.AlbumSong;
import com.example.clientformusicserver.model.Comment;
import com.example.clientformusicserver.model.Song;

@Database(entities = {Album.class, Song.class, AlbumSong.class, Comment.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}
