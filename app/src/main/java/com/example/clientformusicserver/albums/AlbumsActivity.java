package com.example.clientformusicserver.albums;

import android.support.v4.app.Fragment;

import com.example.clientformusicserver.SingleFragmentActivity;

public class AlbumsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AlbumsFragment.newInstance();
    }
}
