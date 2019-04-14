package com.example.clientformusicserver.model.converter;

import android.arch.persistence.room.TypeConverter;

import com.example.clientformusicserver.model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListSongConverter {
    private static Gson mGson = new Gson();

    @TypeConverter
    public static String listToString(List<Song> songs) {
        return mGson.toJson(songs);
    }

    @TypeConverter
    public static List<Song> stringToList(String data) {
        if (data == null) {
            return null;
        }
        Type listType = new TypeToken<List<Song>>() {}.getType();
        return mGson.fromJson(data, listType);
    }
}
