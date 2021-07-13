package com.nerothtr.githubrepo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nerothtr.githubrepo.app.Utils;
import com.nerothtr.githubrepo.databinding.ActivityRegisterBinding;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegisterBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);

        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);


        // Start Here
        Glide.with(this).load(R.drawable.git_signup).into(mBinding.ivSignUp);

        mBinding.btnSignUp.setOnClickListener(this);

    }


    private boolean checkValidEmail(String email) {
        if (isStringEmpty(email))
            Utils.showMessage(this, "Please enter your email");

        else if (!email.contains("@") || !email.endsWith(".com"))
            Utils.showMessage(this, "Please enter valid email");

        else return true;

        return false;
    }

    private boolean checkValidPassword(String password, String rePassword) {
        if (isStringEmpty(password))
            Utils.showMessage(this, "Please enter your password");
        else if (password.length() < 6)
            Utils.showMessage(this, "Password must have at least 6 characters");
        else if (isStringEmpty(rePassword))
            Utils.showMessage(this, "Please re-enter your password");
        else if (!password.equals(rePassword))
            Utils.showMessage(this, "Password not match");
        else return true;

        return false;
    }

    private boolean isStringEmpty(String text) {
        return text.equals("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign_up) {
            String email = mBinding.etSignupEmail.getText().toString();
            String password = mBinding.etSignupPassword.getText().toString();
            String rePassword = mBinding.etSignupRePassword.getText().toString();

            if (checkValidEmail(email) && checkValidPassword(password, rePassword)) {
                createUser(email, password);
                finish();
            }
        }
    }

    private void createUser(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Utils.showMessage(RegisterActivity.this, "Sign Up Suscessfully");
                    FirebaseAuth.getInstance().signOut();
                }
            }
        });
    }
}