package com.nerothtr.githubrepo.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nerothtr.githubrepo.DetailActivity;
import com.nerothtr.githubrepo.R;
import com.nerothtr.githubrepo.adapter.RepoAdapter;
import com.nerothtr.githubrepo.app.Constants;
import com.nerothtr.githubrepo.response.Repository;
import com.nerothtr.githubrepo.response.SearchResponse;
import com.nerothtr.githubrepo.service.GithubApiService;
import com.nerothtr.githubrepo.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements RepoAdapter.OnRepoClick, SwipeRefreshLayout.OnRefreshListener {

    private final List<Repository> mRepositoryList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private NestedScrollView mNestedScrollView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int page = 1, per_page = 10;
    private RepoAdapter mRepoAdapter;
    private String mQuery = "all";

    private Realm mRealm;

    public static HomeFragment newInstance(String q) {
        HomeFragment homeFragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putString(Constants.SEARCH_DATA_KEY, q);
        homeFragment.setArguments(args);

        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mQuery = this.getArguments().getString(Constants.SEARCH_DATA_KEY);
            Log.d(Constants.TAG, "onCreate: " + mQuery);
            mRepositoryList.clear();
        }
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mNestedScrollView = view.findViewById(R.id.nested_scroll_view);
        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                Log.d(Constants.TAG, "onScrollChange: v.getMeasuredHeight --> " + v.getMeasuredHeight());

                page++;
                mProgressBar.setVisibility(View.VISIBLE);
                loadRepo(mQuery, page);
            }
        });
        mProgressBar = view.findViewById(R.id.progressBar);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mRepoAdapter = new RepoAdapter(view.getContext(), mRepositoryList, mRealm, this::onClick);
        recyclerView.setAdapter(mRepoAdapter);

        loadRepo(mQuery, page);
    }

    private void loadRepo(String query, int page) {
        Retrofit retrofit = RetrofitClient.getRetrofit();
        GithubApiService githubApiService = retrofit.create(GithubApiService.class);

        Call<SearchResponse> call = githubApiService.getRepositories(query, page, per_page);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mRepositoryList.addAll(response.body().getItemList());
                    mProgressBar.setVisibility(View.GONE);
                    mRepoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        mRepositoryList.clear();
        mRepoAdapter.notifyDataSetChanged();
        page = 1;
        loadRepo(mQuery, page);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(Constants.DETAIL_KEY_EXTRA, mRepositoryList.get(position));
        startActivity(intent);
    }
}