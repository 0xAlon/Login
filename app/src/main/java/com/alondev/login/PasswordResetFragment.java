package com.alondev.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PasswordResetFragment extends Fragment {

    @BindView(R.id.tvEmail)
    EditText tvEmail;

    private static final String TAG = "PasswordResetFragment";
    private Unbinder unbinder;
    private FirebaseAuth mAuth;

    public PasswordResetFragment() {
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_reset, container, false);
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
        if (!tvEmail.getText().toString().equals("")) {
            mAuth.sendPasswordResetEmail(tvEmail.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(getContext(), "Email sent.", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        }
                    });
        }
    }
}
