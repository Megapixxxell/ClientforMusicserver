package com.example.clientformusicserver;

import com.example.clientformusicserver.model.Album;
import com.example.clientformusicserver.model.Comment;
import com.example.clientformusicserver.model.Song;
import com.example.clientformusicserver.model.User;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AcademyApi {

    @POST("registration")
    Completable registration(@Body User user);

    @GET("user")
    Single<User> getUserBean();

    @GET("albums")
    Single<List<Album>> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Call<List<Song>> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);

    @GET("albums/{id}/comments")
    Single<List<Comment>> getComments(@Path("id") int id);

    @GET("comments")
    Single <List<Comment>> getAllComments();

    @POST("comments")
    Completable sendComment(@Body Comment comment);
}
