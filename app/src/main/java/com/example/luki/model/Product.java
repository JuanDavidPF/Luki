package com.example.luki.model;

import android.net.Uri;


import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;

public class Product implements Serializable {

    private String seller_id;
    private String product_category;
    private String product_id;
    private String product_reference;
    private String product_description;
    private float product_price;


    public Product() {
    }

    public Product(String seller_id, String product_category, String product_id, String product_reference, String product_description, float product_price) {
        this.seller_id = seller_id;
        this.product_category = product_category;
        this.product_id = product_id;
        this.product_reference = product_reference;
        this.product_description = product_description;
        this.product_price = product_price;
    }//closes product constructor


    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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
}//closes product class
