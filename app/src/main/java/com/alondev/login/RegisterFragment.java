package com.alondev.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterFragment extends Fragment {

    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.tvPassword)
    TextView tvPassword;

    private static final String TAG = "RegisterFragment";
    private Unbinder unbinder;
    private FirebaseAuth mAuth;

    public RegisterFragment() {
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
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
        if (isValidPassword(tvPassword.getText().toString()) && !tvEmail.getText().toString().equals("") && !tvUser.getText().toString().equals("")) {
            mAuth.createUserWithEmailAndPassword(tvEmail.getText().toString(), tvPassword.getText().toString())
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("Uid", user.getUid());
                            userData.put("Username", String.valueOf(tvUser.getText()));

                            db.collection("Users") // Add a new document with a generated ID
                                    .add(userData)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        Intent intent = new Intent(getContext(), WelcomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                        }
                    }).addOnFailureListener(Throwable::printStackTrace);
        } else {
            Toast.makeText(getContext(), "Authentication failed. (Password must contain minimum 8 characters at least 1 Alphabet, 1 Number)", Toast.LENGTH_LONG).show();
        }
    }

    //Password must contain minimum 8 characters at least 1 Alphabet, 1 Number
    private static boolean isValidPassword(String s) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{8,24}");
        return !TextUtils.isEmpty(s) && pattern.matcher(s).matches();
    }
}

