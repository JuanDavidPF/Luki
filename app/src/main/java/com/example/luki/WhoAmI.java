package com.example.luki;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WhoAmI extends AppCompatActivity implements View.OnClickListener {

    private Button customerBtn;
    private Button sellerBtn;
    public static Activity WHOAMI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_am_i);

        customerBtn = findViewById(R.id.whoAmI_btn_customer);
        sellerBtn = findViewById(R.id.whoAmI_btn_seller);

        customerBtn.setOnClickListener(this);
        sellerBtn.setOnClickListener(this);

    }//closes onCreate method


    @Override
    public void onClick(View view) {
        deactivateButtons();
        Intent toSign = new Intent(this.getApplicationContext(), SignUp.class);
        switch (view.getId()) {

            case R.id.whoAmI_btn_customer:
                toSign.putExtra("typeOfUser", "customer");
                break;

            case R.id.whoAmI_btn_seller:
                toSign.putExtra("typeOfUser", "seller");
                break;
        }

        startActivity(toSign);
        finish();
    }//closes onClick method


    private void deactivateButtons() {
        customerBtn.setEnabled(false);
        sellerBtn.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        customerBtn.setEnabled(true);
        sellerBtn.setEnabled(true);
    }
}