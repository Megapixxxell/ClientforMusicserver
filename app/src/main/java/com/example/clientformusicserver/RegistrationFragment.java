package com.example.clientformusicserver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clientformusicserver.model.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegistrationFragment extends Fragment {

    private EditText mEmail;
    private EditText mName;
    private EditText mPassword;
    private EditText mPasswordAgain;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        mEmail = view.findViewById(R.id.etEmail);
        mName = view.findViewById(R.id.etName);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        Button registration = view.findViewById(R.id.btnRegistration);

        registration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    private View.OnClickListener mOnRegistrationClickListener = view -> {
        if (!isEmailValid()) {
            mEmail.setText("");
            mEmail.setError("incorrect");

        } else if (!isNameValid()) {
            mName.setText("");
            mName.setError("input name!");

        } else if (!isPasswordsValid()) {
            mPassword.setText("");
            mPassword.setError("invalid pass");
            mPasswordAgain.setText("");

        } else if (!isPasswordAgainValid()) {
            mPasswordAgain.setError("enter password again");
            mPasswordAgain.setText("");

        } else {
            User user = new User(
                    mEmail.getText().toString(),
                    mName.getText().toString(),
                    mPassword.getText().toString());

            ApiUtils.getApiService()
                    .registration(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        showMessage(R.string.registration_success);
                        getFragmentManager().popBackStack();
                    }, throwable -> showMessage(R.string.request_error));
        }
    };

    private boolean isEmailValid() {
        String email = mEmail.getText().toString();
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isNameValid() {
        return !TextUtils.isEmpty(mName.getText());
    }

    private boolean isPasswordsValid() {
        String password = mPassword.getText().toString();

        return !TextUtils.isEmpty(password)
                && password.length() >= 8;
    }

    private boolean isPasswordAgainValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        return password.equals(passwordAgain);
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }


}
