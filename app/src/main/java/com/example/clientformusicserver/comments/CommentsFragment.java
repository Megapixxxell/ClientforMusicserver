package com.example.clientformusicserver.comments;


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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clientformusicserver.ApiUtils;
import com.example.clientformusicserver.App;
import com.example.clientformusicserver.R;
import com.example.clientformusicserver.model.Comment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    public static final String ALBUM_ID = "ALBUM_ID";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefresher;
    private View mErrorView;
    private ConnectivityManager connectivityManager;
    private int albumId;
    private EditText mEtComment;


    private CommentsAdapter mCommentsAdapter = new CommentsAdapter();

    public static CommentsFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt(ALBUM_ID, id);

        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_recycler_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_comment);
        mRefresher = view.findViewById(R.id.refresher_comment);
        mRefresher.setOnRefreshListener(this);
        mErrorView = view.findViewById(R.id.errorView);

        Button btnSend = view.findViewById(R.id.btn_add_comment);
        mEtComment = view.findViewById(R.id.etComment);

        btnSend.setOnClickListener(this);

        mEtComment.setOnEditorActionListener((v, actionId, event) -> {

            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendComment();
                handled = true;
            }
            return handled;
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        albumId = getArguments().getInt(ALBUM_ID);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);


        getActivity().setTitle("Comments");

        getComments();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCommentsAdapter);

    }

    @Override
    public void onRefresh() {
        if (!(checkInternetConnection())) {
            Toast.makeText(getActivity(), "Lost connection to the Music server", Toast.LENGTH_SHORT).show();
        }

        mRefresher.post(this::getComments);
    }

    private boolean checkInternetConnection() {
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

    @SuppressLint("CheckResult")
    private void getComments(){
        ApiUtils.getApiService()
                .getComments(albumId)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(comments -> {
                    App.getInstance().getMusicDao().insertComments(comments);
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return App.getInstance().getMusicDao().getComments();
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> mRefresher.setRefreshing(true))
                .doFinally(() -> mRefresher.setRefreshing(false))
                .subscribe((comments -> {
                    mErrorView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mCommentsAdapter.addData(comments, true);
                }), throwable -> {
                    mErrorView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mRefresher.setRefreshing(false);
                });
    }

    @SuppressLint("CheckResult")
    private void getAllComments(){
        ApiUtils.getApiService()
                .getAllComments()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(comments -> {
                    App.getInstance().getMusicDao().insertComments(comments);
                })
                .onErrorReturn(throwable -> {
                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
                        return App.getInstance().getMusicDao().getComments();
                    } else return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> mRefresher.setRefreshing(true))
                .doFinally(() -> mRefresher.setRefreshing(false))
                .subscribe((comments -> {
                    mErrorView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mCommentsAdapter.addData(comments, true);
                }), throwable -> {
                    mErrorView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mRefresher.setRefreshing(false);
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_comment:
                sendComment();
        }
    }

    public void sendComment() {
        if (TextUtils.isEmpty(mEtComment.getText())) {
            Toast.makeText(getActivity(), "Input comment", Toast.LENGTH_SHORT).show();
        } else if (checkInternetConnection()) {

            Comment comment = new Comment(albumId, mEtComment.getText().toString());
            ApiUtils.getApiService()
                    .sendComment(comment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        mEtComment.setText("");
                        Toast.makeText(getActivity(), "Comment added", Toast.LENGTH_SHORT).show();
                        getComments();
                    }, throwable -> Toast.makeText(getActivity(), "HTTP error", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getActivity(), "Turn on the internet", Toast.LENGTH_SHORT).show();
        }
    }
}
