package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luki.Gallery.ThumbnailsAdapter;
import com.example.luki.model.Thumbnail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainSeller extends AppCompatActivity implements View.OnClickListener, ThumbnailsAdapter.ThumbnailHandler {

    private MotionLayout animation;

    private ConstraintLayout lukiCredits;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private StorageReference mStorageRef;

    private ImageButton addProductBtn;
    private ImageButton myProductsBtn;

    private ArrayList<ImageButton> myProducts;

    private RecyclerView productsContainer;
    private GridLayoutManager layoutManager;
    private ThumbnailsAdapter adapter;

    private ImageView loadingBar;
    private ConstraintLayout noProducts;

    private boolean tutorialFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        animation = findViewById(R.id.mainSeller_animation);
        lukiCredits =findViewById(R.id.mainSeller_LukiCredits);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        mStorageRef = FirebaseStorage.getInstance().getReference();

        addProductBtn = findViewById(R.id.mainSeller_imgBtn_addProduct);
        myProductsBtn = findViewById(R.id.mainSeller_imgBtn_myProducts);

        addProductBtn.setOnClickListener(this);
        myProductsBtn.setOnClickListener(this);

        myProducts = new ArrayList<ImageButton>();

        productsContainer = findViewById(R.id.mainSeller_RecyclerView);
        productsContainer.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        productsContainer.setLayoutManager(layoutManager);
        adapter = new ThumbnailsAdapter(R.layout.my_products, R.id.myProducts_thumbnail);
        adapter.setObserver(this);
        productsContainer.setAdapter(adapter);

        noProducts = findViewById(R.id.mainSeller_noProducsPanel);
        noProducts.setVisibility(View.INVISIBLE);

        loadingBar = findViewById(R.id.mainSeller_loadingBar);
        Glide.with(this).load(R.drawable.loading_gif).into(loadingBar);


        SharedPreferences pref = this.getSharedPreferences("GLOBAL_PREFERENCES", Context.MODE_PRIVATE);
        tutorialFinished = pref.getBoolean("hasFinishedTutorial", false);

        if (!tutorialFinished) {
            animation.transitionToEnd();
        }

        getMyProductsID();


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

            case R.id.mainSeller_animation:


                break;
        }


    }//closes onClick method

    private void getMyProductsID() {

        //Get Product info
        mDatabase.child("users").child(currentUser.getUid()).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RepresentProducts(snapshot.getKey());
                    }
                } else if (!dataSnapshot.exists() || (dataSnapshot.exists() && dataSnapshot.getChildrenCount() == 0)) {
                    productsContainer.setEnabled(false);
                    Glide.with(MainSeller.this).clear(loadingBar);
                    loadingBar.setEnabled(false);
                    noProducts.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });//closes firebase retrieve
    }//closes getMyProductsID method


    private void RepresentProducts(String productId) {

        //set Main Image
        mStorageRef.child(productId).child(productId + "-0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri thumbnail) {

                adapter.AddThumbnail(new Thumbnail(thumbnail));

                Glide.with(MainSeller.this).clear(loadingBar);
                loadingBar.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(MainSeller.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }//closes RepresentProducts method


    private void deactivateBtns() {
        addProductBtn.setEnabled(false);
        myProductsBtn.setEnabled(false);
    }//closes deactivateBtns method


    @Override
    protected void onResume() {
        super.onResume();
        addProductBtn.setEnabled(true);
        myProductsBtn.setEnabled(true);

    }//closes onResume method


    @Override
    public void SetMainImage(Uri uri) {

    }
}//closes MainSeller class