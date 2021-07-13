package com.nerothtr.githubrepo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nerothtr.githubrepo.app.Utils;
import com.nerothtr.githubrepo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName() + "_LOG_TAG";
    private ActivityLoginBinding binding;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupFirebaseAuth();
//        FirebaseAuth.getInstance().signOut();
        Glide.with(this).load(R.drawable.git_launch).centerCrop().into(binding.ivLogin);
        binding.btnLogin.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String userName = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if (userName.equals("") || password.equals(""))
                    Toast.makeText(this, "Enter your email/password credential", Toast.LENGTH_SHORT).show();

                else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(userName, password)
                            .addOnCompleteListener(this, task -> {
                                Log.d(TAG, "onComplete: " + "sign in successfully");

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }).addOnFailureListener(e -> {
                        Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_SHORT).show();

                    });
                }

                break;

            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;


        }

    }

    private void setupFirebaseAuth() {
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Log.d(TAG, "onComplete: " + "true sign in " + user.getUid());

            } else {
//                Utils.showMessage(LoginActivity.this, "Incorrect Credential");
                Log.d(TAG, "onComplete: " + "sign in failed");

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthStateListener != null)
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}