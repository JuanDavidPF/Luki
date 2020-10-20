package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luki.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Store extends AppCompatActivity {

    private String category;
    private TextView title;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private ArrayList<Product> products;
    private ArrayList<String> productId;
    private ArrayList<Uri> productsPictures;
    private ImageView productPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        category = getIntent().getExtras().getString("categoria");
        title = findViewById(R.id.storeTitle);
        title.setText(category);
        category = category.toLowerCase();

        products = new ArrayList<Product>();
        productId = new ArrayList<String>();
        productsPictures = new ArrayList<Uri>();


        mDatabase.child("productsGlobal").child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        productId.add(snapshot.getKey());
                        products.add(snapshot.getValue(Product.class));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//closes firebase retrieve
    }//closes OnCreate

}