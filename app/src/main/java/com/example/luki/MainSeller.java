package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MainSeller extends Fragment implements View.OnClickListener, ThumbnailsAdapter.ThumbnailHandler {


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

    private int amountOfProducts;
    private int productsLoaded;

    private boolean tutorialFinished;


    public static MainSeller newInstance() {
        MainSeller fragment = new MainSeller();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_main_seller, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("GLOBAL_PREFERENCES", Context.MODE_PRIVATE);
        tutorialFinished = pref.getBoolean("hasFinishedTutorial", false);


        animation = root.findViewById(R.id.mainSeller_animation);
        if (tutorialFinished) {
            animation.transitionToState(R.id.end);
        }


        lukiCredits = root.findViewById(R.id.mainSeller_LukiCredits);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        mStorageRef = FirebaseStorage.getInstance().getReference();

        addProductBtn = root.findViewById(R.id.mainSeller_imgBtn_addProduct);
        myProductsBtn = root.findViewById(R.id.mainSeller_imgBtn_myProducts);

        addProductBtn.setOnClickListener(this);
        myProductsBtn.setOnClickListener(this);

        myProducts = new ArrayList<ImageButton>();

        productsContainer = root.findViewById(R.id.mainSeller_RecyclerView);
        productsContainer.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        productsContainer.setLayoutManager(layoutManager);
        adapter = new ThumbnailsAdapter(R.layout.my_products, R.id.myProducts_thumbnail);
        adapter.setObserver(this);
        productsContainer.setAdapter(adapter);

        noProducts = root.findViewById(R.id.mainSeller_noProducsPanel);
        noProducts.setAlpha(0);

        loadingBar = root.findViewById(R.id.mainSeller_loadingBar);
        Glide.with(this).load(R.drawable.loading_gif).into(loadingBar);
        loadingBar.setAlpha(0f);

        getMyProductsID();


        return root;
    }//closes onCreateView


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.mainSeller_imgBtn_addProduct:


                if (!tutorialFinished) {
                    animation.transitionToState(R.id.end);
                    tutorialFinished = true;
                    SharedPreferences preferences = getActivity().getSharedPreferences("GLOBAL_PREFERENCES", Context.MODE_PRIVATE);
                    preferences.edit().putBoolean("hasFinishedTutorial", true).apply();
                }

                Intent addProduct = new Intent(getActivity(), newProduct.class);
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
                    noProducts.setAlpha(0);
                    loadingBar.setAlpha(1f);
                    amountOfProducts = (int) dataSnapshot.getChildrenCount();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RepresentProducts(snapshot.getKey());
                    }
                } else if (!dataSnapshot.exists() || (dataSnapshot.exists() && dataSnapshot.getChildrenCount() == 0)) {
                    loadingBar.setAlpha(0f);
                    noProducts.setAlpha(1);
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

                Thumbnail thumb = new Thumbnail(thumbnail);
                if (!adapter.getThumbnailsUris().contains(thumb.getThumbnailUri()))
                    adapter.AddThumbnail(thumb);

                productsLoaded++;
                if (productsLoaded == amountOfProducts) {
                    loadingBar.setAlpha(0f);
                    productsLoaded = 0;
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getActivity(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }//closes RepresentProducts method


    private void deactivateBtns() {
        addProductBtn.setEnabled(false);
        myProductsBtn.setEnabled(false);
    }//closes deactivateBtns method


    @Override
    public void onResume() {
        super.onResume();

        addProductBtn.setEnabled(true);
        myProductsBtn.setEnabled(true);

    }//closes onResume method


    @Override
    public void SetMainImage(Uri uri) {

    }
}//closes MainSeller class