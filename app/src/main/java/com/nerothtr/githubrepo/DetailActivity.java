package com.nerothtr.githubrepo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nerothtr.githubrepo.app.Constants;
import com.nerothtr.githubrepo.databinding.ActivityDetailBinding;
import com.nerothtr.githubrepo.response.Repository;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);

        mBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        Repository repository = intent.getParcelableExtra(Constants.DETAIL_KEY_EXTRA);

//        Glide.with(this).load(repository.getOwner().getAvatarUrl()).centerCrop().into(mBinding.ivDetail);

        mBinding.tvDetailName.setText(repository.getOwner().getLogin());
        mBinding.tvDetailRepo.setText(repository.getName());

    }
}