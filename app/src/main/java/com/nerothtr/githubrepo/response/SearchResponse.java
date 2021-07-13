package com.nerothtr.githubrepo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("items")
    private List<Repository> itemList;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Repository> getItemList() {
        return itemList;
    }

    public void setItemList(List<Repository> itemList) {
        this.itemList = itemList;
    }
}
