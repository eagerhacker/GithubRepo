package com.nerothtr.githubrepo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import com.bumptech.glide.Glide;
import com.nerothtr.githubrepo.R;
import com.nerothtr.githubrepo.app.Utils;
import com.nerothtr.githubrepo.response.Owner;
import com.nerothtr.githubrepo.response.Repository;

import java.util.List;
import java.util.UUID;

import static com.nerothtr.githubrepo.app.Constants.TAG;
import static io.realm.FieldAttribute.PRIMARY_KEY;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {
    private List<Repository> mRepositoryList;
    private Context mContext;
    private RealmResults<Repository> mRealmList;
    private RealmResults<Owner> mRealmOwnerList;
    private Realm mRealm;
    private OnRepoClick mOnRepoClick;

    public RepoAdapter(Context context, List<Repository> repositoryList, Realm realm, OnRepoClick onRepoClick) {
        mRepositoryList = repositoryList;
        mContext = context;
        mRealm = realm;
        mRealmList = mRealm.where(Repository.class).findAll();
        mOnRepoClick = onRepoClick;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repository repository = mRepositoryList.get(position);
        holder.setData(repository, position);
    }

    public void swap(RealmResults<Repository> list) {
        if (list.size() == 0) {
            Toast.makeText(mContext, "No Items Found", Toast.LENGTH_SHORT).show();
        } else {
            mRepositoryList = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mRepositoryList.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTvRepoName, mTvRepoLang, mTvRepoInfo;
        private Button mBtnBookmark;
        private ImageView mIvRepoOwner;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvRepoName = itemView.findViewById(R.id.tv_repo_name);
            mTvRepoLang = itemView.findViewById(R.id.tv_repo_lang);
            mTvRepoInfo = itemView.findViewById(R.id.tv_repo_info);
            mBtnBookmark = itemView.findViewById(R.id.btn_bookmark);
            mIvRepoOwner = itemView.findViewById(R.id.iv_repo_owner);

            itemView.setOnClickListener(this::onClick);
        }

        public void setData(Repository repository, int position) {
            mTvRepoName.setText(repository.getName());

            for (Repository repo : mRealmList) {
                if (repo.getName().equals(repository.getName()) && repo.getStargazersCount() == repository.getStargazersCount())
                    mBtnBookmark.setSelected(true);
            }

            String escapedLanguage = repository.getLanguage() != null ? TextUtils.htmlEncode(repository.getLanguage()) : "null";
            String langText = mContext.getString(R.string.language, escapedLanguage);
            mTvRepoLang.setText(langText);

            mTvRepoInfo.setText(mContext.getString(R.string.info, repository.getStargazersCount(), repository.getWatchersCount(), repository.getForks()));

            Glide.with(mContext).load(repository.getOwner().getAvatarUrl()).centerCrop().into(mIvRepoOwner);
            mBtnBookmark.setOnClickListener(v -> {
                if (mBtnBookmark.isSelected()) {
                    mBtnBookmark.setSelected(false);
                    Utils.showMessage(mContext, "Remove from bookmark");
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            mRealm.where(Repository.class).equalTo("id", repository.getId()).findFirst().deleteFromRealm();
                        }
                    });
                } else {
                    mBtnBookmark.setSelected(true);
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            String id = Integer.toString(repository.getId());
                            int ownerId = repository.getOwner().getId();

                            Repository repoRealm = realm.createObject(Repository.class, id);
                            repoRealm.setName(repository.getName());
                            repoRealm.setLanguage(repository.getLanguage());
                            repoRealm.setStargazersCount(repository.getStargazersCount());
                            repoRealm.setWatchersCount(repository.getWatchersCount());
                            repoRealm.setForks(repository.getForks());

                            Owner owner = realm.where(Owner.class).equalTo("id", ownerId).findFirst();
                            if (owner == null) {
                                owner = realm.createObject(Owner.class, ownerId);
                                owner.setAvatarUrl(repository.getOwner().getAvatarUrl());
                            }
                            repoRealm.setOwner(owner);
                        }
                    });

                    Utils.showMessage(mContext, "Added to bookmark");
                }
            });
        }

        @Override
        public void onClick(View v) {
            mOnRepoClick.onClick(getAdapterPosition());
        }
    }

    public interface OnRepoClick {
        void onClick(int position);
    }
}
