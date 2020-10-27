package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luki.StoreConstructor.ProductsAdapter;
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

    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private String category;
    private TextView title;


    private RecyclerView productsContainer;
    private GridLayoutManager layoutManager;
    private ProductsAdapter productsAdapter;


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

        productsContainer = findViewById(R.id.store_RV_productContainer);
        productsContainer.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        productsAdapter = new ProductsAdapter();

        productsContainer.setLayoutManager(layoutManager);
        productsContainer.setAdapter(productsAdapter);

        GetProductInfo();


    }//closes OnCreate


    private void GetProductInfo() {

        //Get Product info
        mDatabase.child("products").child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        RepresentProducts(snapshot.getValue(Product.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//closes firebase retrieve

    }//closes Get Product info


    private void RepresentProducts(Product product) {


        mStorageRef.child(product.getProduct_id()).child(product.getProduct_id() + "-0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri thumbnail) {
                productsAdapter.NewProduct(product, thumbnail);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(Store.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }//closes RepresentProducts method


}