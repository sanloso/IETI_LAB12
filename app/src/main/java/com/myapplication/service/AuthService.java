package com.myapplication.service;

import com.myapplication.model.LoginWrapper;
import com.myapplication.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth")
    Call<Token> authentication(@Body LoginWrapper loginWrapper);
}

