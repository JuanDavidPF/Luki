package com.example.luki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class CheckOut extends AppCompatActivity implements View.OnClickListener {

    private Button returnBtn;
    private TextView payedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        payedAmount = findViewById(R.id.checkOut_finalAmount);
        returnBtn = findViewById(R.id.checkOut_Btn_return);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            FillRecipe(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        returnBtn.setOnClickListener(this);

    }//closes OnCreate method

    private void FillRecipe(JSONObject response, String monto) {

        payedAmount.setText("$" + monto);

    }//closes FillRecipe


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.checkOut_Btn_return:

                Intent returnToCatalog = new Intent(this, Catalogue.class);
                startActivity(returnToCatalog);

                break;

        }

    }//closes onClick method
}//closes CheckOut class