package com.nerothtr.githubrepo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nerothtr.githubrepo.R;
import com.nerothtr.githubrepo.app.Utils;
import com.nerothtr.githubrepo.response.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.nerothtr.githubrepo.app.Constants.TAG;

public class RepoBookmarkAdapter extends RecyclerView.Adapter<RepoBookmarkAdapter.RepoViewHolder> {
    private Context mContext;
    private Realm mRealm;
    private RealmResults<Repository> mRealmList;
    private List<Repository> mRepoAll;

    public RepoBookmarkAdapter(Context context, Realm realm) {
        mContext = context;
        mRealm = realm;
        mRealmList = realm.where(Repository.class).findAll().sort("name");
        mRepoAll = new ArrayList<>(mRealmList);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mRealmList.size(); i++) {
            stringBuilder.append(mRealmList.get(i).toString()).append("\n\n");
        }
        Log.d(TAG, "RepoBookmarkAdapter: " + stringBuilder);
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {

        Repository repository = mRealmList.get(position);
        holder.mTvRepoName.setText(repository.getName());

        String escapedLanguage = repository.getLanguage() != null ? TextUtils.htmlEncode(repository.getLanguage()) : "null";
        String langText = mContext.getString(R.string.language, escapedLanguage);
        holder.mTvRepoLang.setText(langText);

        holder.mTvRepoInfo.setText(mContext.getString(R.string.info, repository.getStargazersCount(), repository.getWatchersCount(), repository.getForks()));

        Glide.with(mContext).load(repository.getOwner().getAvatarUrl()).centerCrop().into(holder.mIvRepoOwner);

        holder.mBtnBookmark.setOnClickListener(v -> {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mRealmList.get(position).deleteFromRealm();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mRealmList.size());

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        if (mRealmList != null)
            return mRealmList.size();
        else return 0;
    }




    public class RepoViewHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
