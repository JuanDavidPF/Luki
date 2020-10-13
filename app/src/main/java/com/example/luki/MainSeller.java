package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainSeller extends AppCompatActivity implements View.OnClickListener {


    private DatabaseReference mDatabase;


    private ImageButton addProductBtn;
    private ImageButton myProductsBtn;

    private ArrayList<ImageButton> myProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addProductBtn = findViewById(R.id.mainSeller_imgBtn_addProduct);
        myProductsBtn = findViewById(R.id.mainSeller_imgBtn_myProducts);

        addProductBtn.setOnClickListener(this);
        myProductsBtn.setOnClickListener(this);

        myProducts = new ArrayList<ImageButton>();
        getMyProducts();
    }//closes onCreate method

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.mainSeller_imgBtn_addProduct:

                Intent addProduct = new Intent(this, newProduct.class);
                startActivity(addProduct);
                deactivateBtns();

                break;


            case R.id.mainSeller_imgBtn_myProducts:


                break;
        }


    }

    private void getMyProducts() {
   

    }


    private void deactivateBtns() {
        addProductBtn.setEnabled(false);
        myProductsBtn.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        addProductBtn.setEnabled(true);
        myProductsBtn.setEnabled(true);

    }

}//closes MainSeller class