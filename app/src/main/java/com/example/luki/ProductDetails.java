package com.example.luki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luki.Config.Config;
import com.example.luki.Gallery.ThumbnailsAdapter;
import com.example.luki.model.Product;
import com.example.luki.model.Thumbnail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDetails extends AppCompatActivity implements ThumbnailsAdapter.ThumbnailHandler, View.OnClickListener {

    private static final int PAYPAL_REQUEST_CODE = 2512;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    private StorageReference mStorageRef;

    private RecyclerView thumbnailsRV;
    private LinearLayoutManager linearLayoutManager;
    private ThumbnailsAdapter adapter;

    private Product product;
    private TextView productName;
    private TextView productDescription;
    private TextView productPrice;
    private ImageView mainPicture;
    private Button checkoutBtn;

    private ImageView loading;
    private String monto;

    private int thumbnailsLoaded = 0;

    private MotionLayout animation;


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        product = (Product) getIntent().getSerializableExtra("Product");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        thumbnailsRV = findViewById(R.id.productDetail_RV_photos);
        thumbnailsRV.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        thumbnailsRV.setLayoutManager(linearLayoutManager);
        adapter = new ThumbnailsAdapter(R.layout.thumnail, R.id.thumbnailView_thumbnail);
        adapter.setObserver(this);
        thumbnailsRV.setAdapter(adapter);

        productName = findViewById(R.id.productDetail_txtView_productName);
        productDescription = findViewById(R.id.productDetail_txtView_productDesctiption);
        productPrice = findViewById(R.id.productDetail_txtVw_price);
        mainPicture = findViewById(R.id.productDetail_imgView_mainPhoto);
        mainPicture.setClipToOutline(true);
        productName.setText(CapitalizeInitials(product.getProduct_reference()));
        productDescription.setText(product.getProduct_description());
        productPrice.setText("$" + (int) product.getProduct_price());

        checkoutBtn = findViewById(R.id.checkout_btn);
        checkoutBtn.setOnClickListener(this);

        loading = findViewById(R.id.ProductDetailsLoading);
        Glide.with(this).load(R.drawable.loading_gif).into(loading);

        animation = findViewById(R.id.productsDetailsAnimation);

        //fetch productsPictures
        GetProductsPictures();
    }//closes OnCreate method

    private String CapitalizeInitials(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();

    }//closes CapitalizeInitals method

    private void GetProductsPictures() {


        mStorageRef.child(product.getProduct_id()).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference item : listResult.getItems()) {

                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            adapter.AddThumbnail(new Thumbnail(uri));
                            thumbnailsLoaded += 1;
                            //if its the last picture
                            if (thumbnailsLoaded == listResult.getItems().size()) {
                                animation.transitionToState(R.id.end);
                            }
                        }
                    });
                }
            }
        });

        //set Main Image
        mStorageRef.child(product.getProduct_id()).child(product.getProduct_id() + "-0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri thumbnail) {
                Glide.with(ProductDetails.this).load(thumbnail).fitCenter().into(mainPicture);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(ProductDetails.this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }//closes GetProductsPictures

    @Override
    public void SetMainImage(Uri uri) {
        Glide.with(ProductDetails.this).load(uri).into(mainPicture);
    }//closes SetMainImage method

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.checkout_btn:

                ProductCheckOut();

                break;
        }
    }//closes ONClick method

    private void ProductCheckOut() {

        monto = (int) product.getProduct_price() + "";
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(monto)), "USD", CapitalizeInitials(product.getProduct_reference()), PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);


                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, CheckOut.class).putExtra("PaymentDetails", paymentDetails).putExtra("Product",product).putExtra("PaymentAmount", monto));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Compra cancelada", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Compra o datos invalidos", Toast.LENGTH_SHORT).show();

    }//closes OnActivityResult method



    private void CreateOrder(){

    }//closes CreateOrder method

}//closes ProductDetails class