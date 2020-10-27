package com.example.luki.StoreConstructor;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.luki.ProductDetails;
import com.example.luki.R;
import com.example.luki.model.Product;

public class ProductView extends RecyclerView.ViewHolder implements View.OnClickListener {


    private ConstraintLayout root;
    private ImageView productPicture;
    private TextView productName;
    private TextView productPrice;
    private Product product;


    public ProductView(ConstraintLayout root) {
        super(root);
        this.root = root;
        productPicture = root.findViewById(R.id.productView_ImageView_productPicture);
        productName = root.findViewById(R.id.productView_TextView_productName);
        productPrice = root.findViewById(R.id.productView_TextView_productPrice);

        productPicture.setOnClickListener(this);
    }//closes ProductView method


    public ConstraintLayout getRoot() {
        return root;
    }

    public ImageView getProductPicture() {
        return productPicture;
    }

    public TextView getProductName() {
        return productName;
    }

    public TextView getProductPrice() {
        return productPrice;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.productView_ImageView_productPicture:

                Intent toProductDetails = new Intent(root.getContext(), ProductDetails.class);
                toProductDetails.putExtra("Product",product);
                root.getContext().startActivity(toProductDetails);
                break;

        }
    }//closes onClickListener


    public void setData(Product product) {
        this.product = product;
    }


}//closes product view
