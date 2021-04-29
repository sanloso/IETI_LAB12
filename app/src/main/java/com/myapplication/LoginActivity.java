package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.myapplication.model.LoginWrapper;
import com.myapplication.model.Token;
import com.myapplication.service.AuthService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void VerifyEmailPassword(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);

        if(email.getText().toString().isEmpty()){
            email.setError("Email is mandatory");
        }else if(password.getText().toString().isEmpty()){
            password.setError("Password is mandatory");
        }else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http:/10.0.2.2:8080") //localhost for emulator
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final AuthService authService = retrofit.create(AuthService.class);

            executorService.execute( new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Response<Token> response = authService.authentication( new LoginWrapper( email.getText().toString(), password.getText().toString() ) ).execute();
                        Token token = response.body();
                        if (token != null){
                            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE );
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("TOKEN_KEY",token.getToken());
                            editor.commit();
                            System.out.println(sharedPref+ "------------");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            finish();
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    email.setError("check your data!");
                                }
                            });
                        }
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            } );
        }
    }
}