package com.example.practica3.Interface;

import com.example.practica3.Model.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("posts/?userId=2")
    Call<List<Posts>> getPosts();

}
