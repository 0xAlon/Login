package com.alondev.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class LoginFragment extends Fragment {

    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvPassword)
    TextView tvPassword;

    private FirebaseAuth mAuth;
    private Unbinder unbinder;
    private FragmentManager fragmentManager;

    @SuppressLint("ValidFragment")
    public LoginFragment(FragmentManager fragmentManager) {
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnProceed)
    void Proceed() {
        if (!tvEmail.getText().toString().equals("") && !tvPassword.getText().toString().equals("")) {
            mAuth.signInWithEmailAndPassword(tvEmail.getText().toString(), tvPassword.getText().toString())
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getContext(), WelcomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "signIn Fail", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> e.printStackTrace());
        }
    }

    @OnClick(R.id.tvForgotPassword)
    void ForgotPassword() {
        fragmentManager.beginTransaction()
                .replace(R.id.login_main_container, new PasswordResetFragment())
                .addToBackStack("PasswordResetFragment")
                .commit();
    }

    @OnClick(R.id.tvCreateAccount)
    void CreateAccount() {
        fragmentManager.beginTransaction()
                .replace(R.id.login_main_container, new RegisterFragment())
                .addToBackStack("RegisterFragment")
                .commit();
    }

}
