package com.nerothtr.githubrepo.service;

import com.nerothtr.githubrepo.response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubApiService {

    @GET("/search/repositories")
    Call<SearchResponse> getRepositories(@Query("q") String query,
                                         @Query("page") int page,
                                         @Query("per_page") int per_page);
}
