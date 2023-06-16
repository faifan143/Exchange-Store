package com.faifan143.exchange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.faifan143.exchange.activities.HomeActivity;
import com.faifan143.exchange.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain shared preferences object
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("is_logged_in" , false); // The default value is false
        Intent intent;
        if (isLoggedIn==true) {
            intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (isLoggedIn==false) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
