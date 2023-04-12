package com.dicoding.githubuser.apis

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUserSearch(@Query("q") query: String) : Call<UserSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUser(
        @Path("username") username: String?
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String?
    ): Call<ArrayList<UserSearchResponseItems>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String?
    ): Call<ArrayList<UserSearchResponseItems>>
}