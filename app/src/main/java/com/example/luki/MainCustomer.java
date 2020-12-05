package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.luki.Bills.BillsAdapter;
import com.example.luki.Gallery.ThumbnailsAdapter;
import com.example.luki.model.Bill;
import com.example.luki.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainCustomer extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView billsList;
    private LinearLayoutManager layoutManager;
    private BillsAdapter adapter;

    private Button catalogueBtn;
    private int amountOfBills;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        billsList = findViewById(R.id.main_customer_bill_list);
        billsList.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        billsList.setLayoutManager(layoutManager);

        adapter = new BillsAdapter();
        billsList.setAdapter(adapter);

        catalogueBtn = findViewById(R.id.mainCustomer_catalogue_btn);
        catalogueBtn.setOnClickListener(this::GoToCatalogue);

        GetBillsID();

    }//closes OnCreate method

    private void GetBillsID() {


        //Get Product info
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GetBillData(snapshot.getKey());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//closes firebase retrieve
    }//closes GetBills method

    private void GetBillData(String billID) {


        mDatabase.child("orders").child(billID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String id = dataSnapshot.child("product_id").getValue(String.class);
                    String category = dataSnapshot.child("product_category").getValue(String.class);
                    String date = dataSnapshot.child("purchase_date").getValue(String.class);

                    GetProductData(id, billID,category, date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//closes firebase retrieve
    }//closes GetBillData method


    private void GetProductData(String productID, String orderId, String productCategory, String date) {

        mDatabase.child("products").child(productCategory).child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String name = dataSnapshot.child("product_reference").getValue(String.class);

                    String price = dataSnapshot.child("product_price").getValue(Long.class).toString();
                    Bill bill = new Bill(orderId, name, date, price);

                    if (!adapter.getBills().contains(bill)) {

                        adapter.addBill(bill);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//closes firebase retrieve
    }//closes GetProductData method

    private void GoToCatalogue(View v) {
        Intent goToCatalogue = new Intent(this, Catalogue.class);
        startActivity(goToCatalogue);
    }


}//closes MainCustomer class