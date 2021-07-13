package com.nerothtr.githubrepo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nerothtr.githubrepo.R;
import com.nerothtr.githubrepo.adapter.RepoBookmarkAdapter;
import com.nerothtr.githubrepo.response.Repository;

import io.realm.Realm;
import io.realm.RealmResults;

public class BookmarkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RepoBookmarkAdapter mRepoAdapter;
    private Realm mRealm;
    private RealmResults<Repository> mResults;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_view_bookmark);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRepoAdapter = new RepoBookmarkAdapter(view.getContext(), mRealm);
        mRecyclerView.setAdapter(mRepoAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_bookmark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mRepoAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
