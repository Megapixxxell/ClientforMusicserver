package com.example.clientformusicserver.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.clientformusicserver.ApiUtils;
import com.example.clientformusicserver.App;
import com.example.clientformusicserver.R;
import com.example.clientformusicserver.comments.CommentsFragment;
import com.example.clientformusicserver.model.Album;
import com.example.clientformusicserver.model.Song;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ALBUM_KEY = "ALBUM_KEY";
    public static final String myTAG = "myTAG";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private Album mAlbum;
    private ConnectivityManager connectivityManager;

    @NonNull
    private final SongAdapter mSongAdapter = new SongAdapter();


    public static DetailAlbumFragment newInstance(Album album) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_KEY, album);

        DetailAlbumFragment fragment = new DetailAlbumFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_recycler, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mRefresher = view.findViewById(R.id.refresher);
        mRefresher.setOnRefreshListener(this);
        mErrorView = view.findViewById(R.id.errorView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        getActivity().setTitle(mAlbum.getName());

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        getAlbum();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSongAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_fr_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, CommentsFragment.newInstance(mAlbum.getId()))
                .addToBackStack(CommentsFragment.class.getSimpleName())
                .commit();
        return true;

    }

    @Override
    public void onRefresh() {
        if (!(checkInternetConnection())) {
            Toast.makeText(getActivity(), "Lost connection to the Music server", Toast.LENGTH_SHORT).show();
        }
        mRefresher.post(this::getAlbum);
    }

    @SuppressLint("CheckResult")
    private void getAlbum() {

        ApiUtils.getApiService()
                .getAlbum(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(album -> {
                    App.getInstance().getMusicDao().insertAlbum(album);
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        List <Song> songs = App.getInstance().getMusicDao().getAlbumWithId(mAlbum.getId()).getSongs();
                        sortSongByName(songs);
                        Album album = App.getInstance().getMusicDao().getAlbumWithId(mAlbum.getId());
                        album.setSongs(songs);
                        return album;
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> mRefresher.setRefreshing(true))
                .doFinally(() -> mRefresher.setRefreshing(false))
                .subscribe((album -> {
                    mErrorView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Log.d(myTAG, "getAlbum: " + album.getSongs().toString());
                    mSongAdapter.addData(album.getSongs(), true);
                }), throwable -> {
                    mErrorView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mRefresher.setRefreshing(false);
                });
    }

    private List<Song> sortSongByName(List<Song> songs) {
        Collections.sort(songs, (song1, song2) -> song1.getName().compareTo(song2.getName()));
        return songs;
    }

    private boolean checkInternetConnection() {
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
}
