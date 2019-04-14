package com.example.clientformusicserver;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.clientformusicserver.db.DataBase;
import com.example.clientformusicserver.db.MusicDao;

public class App extends Application {

    private DataBase mDatabase;
    private static App mInstance;

    @Override
    public void onCreate() {


        mInstance = this;

        mDatabase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "music_database")
                .fallbackToDestructiveMigration()
                .build();

        super.onCreate();
    }

    public DataBase getDatabase() {
        return mDatabase;
    }

    public static App getInstance() {
        return mInstance;
    }

    public MusicDao getMusicDao() {
        return mDatabase.getMusicDao();
    }
}
