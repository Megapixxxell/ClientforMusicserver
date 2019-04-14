package com.example.clientformusicserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clientformusicserver.albums.AlbumsActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView mTvEmail;
    private EditText mEtPassword;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mTvEmail = v.findViewById(R.id.et_mail);
        mEtPassword = v.findViewById(R.id.et_pass);
        Button btnSignIn = v.findViewById(R.id.btn_sign_in);
        Button btnSignUp = v.findViewById(R.id.btn_sign_up);

        btnSignIn.setOnClickListener(mOnSignInClickListener);
        btnSignUp.setOnClickListener(mOnSignUpClickListener);
        mTvEmail.setOnFocusChangeListener(mOnTvEmailFocusChangeListener);

        return v;
    }

    private View.OnClickListener mOnSignInClickListener = view -> {
        if (!isEmailValid()) {
            mTvEmail.setError("incorrect");
            mTvEmail.setText("");
        } else if (!isPasswordValid()) {
            mEtPassword.setError("enter password");
            mEtPassword.setText("");
        } else {
            ApiUtils.getApiService(
                    mTvEmail.getText().toString(),
                    mEtPassword.getText().toString(),
                    true)
                    .getUserBean()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((user) -> startActivity(new Intent(getActivity(), AlbumsActivity.class)),
                            throwable -> showMessage(getString(R.string.incorrect_password_or_login)));
        }
    };

    private View.OnClickListener mOnSignUpClickListener = view ->
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();

    private View.OnFocusChangeListener mOnTvEmailFocusChangeListener = (view, hasFocus) -> {
        if (hasFocus) {
            mTvEmail.showDropDown();
        }
    };

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mTvEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mTvEmail.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mEtPassword.getText());
    }

    private void showMessage(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }



}
