package com.example.luki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OurValue extends AppCompatActivity implements View.OnClickListener {

    private Button omitirBtn;
    private Button continueBtn;
    private MotionLayout animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_value);

        omitirBtn = findViewById(R.id.ourValues_btn_skip);
        continueBtn = findViewById(R.id.ourValues_btn_next);
        animation = findViewById(R.id.ourValues_motionLayout);


        omitirBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);

    }//closes onCreate method

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ourValues_btn_skip:
                animation.transitionToState(R.id.lastValue);
                break;

            case R.id.ourValues_btn_next:

                Intent goToLogin = new Intent(this,Login.class);
                startActivity(goToLogin);
                finish();
                break;


        }
    }//closes onClick method
}//closes OurValue method