package com.example.luki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.luki.model.Product;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CheckOut extends AppCompatActivity implements View.OnClickListener {

    private Button returnBtn;
    private TextView payedAmount;
    private TextView deliveryID;
    private TextView productName;
    private TextView productPrice;

    private TextView purchaseDate;
    private TextView purchaseHour;
    private Product product;

    private String date;
    private String hour;

    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);


        payedAmount = findViewById(R.id.checkOut_finalAmount);
        deliveryID = findViewById(R.id.checkOut_productID);
        productName = findViewById(R.id.checkOut_productName);
        productPrice = findViewById(R.id.checkOut_productPrice);

        purchaseDate = findViewById(R.id.checkOut_Date);
        purchaseHour = findViewById(R.id.checkOut_Hour);

        returnBtn = findViewById(R.id.checkOut_Btn_return);

        orderID = UUID.randomUUID().toString();


        GetCalendarData();


        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("Product");

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
        deliveryID.setText(orderID);
        productName.setText(product.getProduct_reference());
        productPrice.setText("$" + monto);
        purchaseHour.setText(hour);
        purchaseDate.setText(date);
    }//closes FillRecipe


    private void GetCalendarData() {

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int timeOfDay = calendar.get(Calendar.AM_PM);

        if (timeOfDay == Calendar.AM) {
            hour = hours + ":" + minutes + " AM";
        } else {
            hour = hours + ":" + minutes + " PM";
        }

        String day = new SimpleDateFormat("d", Locale.getDefault()).format(new Date());
        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(new Date());
        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());


        date = day + " de " + month + " " + year;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.checkOut_Btn_return:

                Intent returnToCatalog = new Intent(this, Catalogue.class);
                startActivity(returnToCatalog);
                finish();
                break;

        }

    }//closes onClick method
}//closes CheckOut class