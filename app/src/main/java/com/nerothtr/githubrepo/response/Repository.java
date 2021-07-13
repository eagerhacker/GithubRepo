package com.nerothtr.githubrepo.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Repository extends RealmObject implements Parcelable {
    public Repository() {
    }

    @PrimaryKey
    private int id;

    private String name;

    @SerializedName("full_name")
    private String fullName;

    private Owner owner;

    @SerializedName("html_url")
    private String htmlUrl;

    private String description;

    @SerializedName("stargazers_count")
    private int stargazersCount;

    @SerializedName("watchers_count")
    private int watchersCount;

    private String language;

    private int forks;

    protected Repository(Parcel in) {
        id = in.readInt();
        name = in.readString();
        fullName = in.readString();
        htmlUrl = in.readString();
        description = in.readString();
        stargazersCount = in.readInt();
        watchersCount = in.readInt();
        language = in.readString();
        forks = in.readInt();
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(fullName);
        dest.writeString(htmlUrl);
        dest.writeString(description);
        dest.writeInt(stargazersCount);
        dest.writeInt(watchersCount);
        dest.writeString(language);
        dest.writeInt(forks);
    }
}
