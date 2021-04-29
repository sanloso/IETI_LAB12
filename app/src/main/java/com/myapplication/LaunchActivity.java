package com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    public static final String TOKEN_KEY = "TOKEN_KEY";

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        SharedPreferences sharedPref = getSharedPreferences( getString( R.string.preference_file_key ), Context.MODE_PRIVATE );

        Intent intent;
        if(sharedPref.contains(TOKEN_KEY)){
            //TODO go to MainActivity
            intent = new Intent(this, MainActivity.class);
        }else{
            //TODO go to LoginActivity
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
