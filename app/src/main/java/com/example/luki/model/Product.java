package com.example.luki.model;

import android.net.Uri;

import java.net.URI;
import java.util.ArrayList;

public class Product {

    private String product_reference;
    private String product_description;
    private float product_price;


    private String seller_id;


    public Product() {
    }

    public Product(String product_reference, String product_description, float product_price, String seller_id) {
        this.product_reference = product_reference;
        this.product_description = product_description;
        this.product_price = product_price;
        this.seller_id = seller_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }



    public String getProduct_reference() {
        return product_reference;
    }

    public void setProduct_reference(String product_reference) {
        this.product_reference = product_reference;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }


}
