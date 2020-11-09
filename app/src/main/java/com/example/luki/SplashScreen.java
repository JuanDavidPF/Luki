package com.example.luki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {


    private Button starBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        starBtn = findViewById(R.id.splash_btn_cta);

        starBtn.setOnClickListener(v -> {
            starBtn.setEnabled(false);


            SharedPreferences pref = this.getSharedPreferences("GLOBAL_PREFERENCES", Context.MODE_PRIVATE);
            boolean isFirstTime = pref.getBoolean("isFirstTime", true);


            if (isFirstTime) {
                Intent toOurValue = new Intent(this, OurValue.class);
                startActivity(toOurValue);
                finish();
            } else {
                Intent toLogin = new Intent(this, Login.class);
                startActivity(toLogin);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        starBtn.setEnabled(true);
    }


}//closes the SplashCreen class