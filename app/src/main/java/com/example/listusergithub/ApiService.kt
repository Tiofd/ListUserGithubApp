package com.example.listusergithub

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchUsers (
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getDetailUsers (
        @Path("username") username: String?
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String?
    ) : Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String?
    ) : Call<List<User>>
}