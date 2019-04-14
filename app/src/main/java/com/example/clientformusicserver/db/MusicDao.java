package com.example.clientformusicserver.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.example.clientformusicserver.model.Album;
import com.example.clientformusicserver.model.AlbumSong;
import com.example.clientformusicserver.model.Comment;
import com.example.clientformusicserver.model.Song;
import com.example.clientformusicserver.model.converter.ListSongConverter;

import java.util.List;

@Dao
@TypeConverters({ListSongConverter.class})
public interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(Album album);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSong(Song song);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSongs(List<AlbumSong> linksAlbumSongs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSong(AlbumSong linksAlbumSong);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<Comment> comments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comment);



    @Query("select * from song where id = :songId")
    Song getSongWithId(int songId);

    @Query("select * from album where id = :albumId")
    Album getAlbumWithId(int albumId);

    @Query("select * from comment where id = :id")
    Comment getCommentsWithId(int id);



    @Query("SELECT * from album")
    List<Album> getAlbums();

    @Query("SELECT * from song")
    List<Song> getSongs();

    @Query("select * from albumsong")
    List<AlbumSong> getAlbumsSongs();

    @Query("select * from comment")
    List<Comment> getComments();


    @Delete
    void deleteAlbum(Album album);

    @Delete
    void deleteSong(Song song);

    @Delete
    void deleteAlbumSong(AlbumSong albumSong);

    @Delete
    void deleteComment(Comment comment);


    //получить список песен переданного id альбома
    @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);

//    @Query("insert into album where   ")


//    @Query("update album set songs = :listOfSongs where id in (:albumId)")
//    void updateSongsInAlbum(List<Song> listOfSongs, int albumId);

    //удалить альбом по id
    @Query("DELETE FROM album where id = :albumId")
    void deleteAlbumById(int albumId);

    @Query("DELETE FROM album where id = :songId")
    void deleteSongById(int songId);

    @Query("DELETE FROM albumsong where id = :albumsongId")
    int deleteAlbumSongById(int albumsongId);

    @Query("DELETE FROM albumsong where id = :commentId")
    int deleteCommentById(int commentId);



}
