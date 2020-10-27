package com.example.luki.StoreConstructor;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luki.R;
import com.example.luki.model.Product;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProductsAdapter extends RecyclerView.Adapter<ProductView> {

    private ArrayList<Product> products;
    private ArrayList<Uri> productsThumbnails;

    public ProductsAdapter() {
        products = new ArrayList<Product>();
        productsThumbnails = new ArrayList<Uri>();
    }//closes ProductsAdapter constructor


    public void NewProduct(Product product, Uri thumbnail) {
        products.add(product);
        productsThumbnails.add(thumbnail);
        this.notifyDataSetChanged();

    }//closes NewProduct method


    @NonNull
    @Override
    public ProductView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View productXML = inflater.inflate(R.layout.product_view, null);
        ConstraintLayout productViewLayout = (ConstraintLayout) productXML;
        ProductView productView = new ProductView(productViewLayout);

        return productView;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductView holder, int position) {

        holder.setData(products.get(position));
        holder.getProductName().setText(CapitalizeInitials(products.get(position).getProduct_reference()));
        holder.getProductPrice().setText(FormatPrice(products.get(position).getProduct_price()));

        Glide.with(holder.getProductPicture().getContext()).load(productsThumbnails.get(position)).fitCenter().into(holder.getProductPicture());
        holder.getProductPicture().setClipToOutline(true);


    }

    @Override
    public int getItemCount() {
        return products.size();

    }//closes GetItemCount method


    private String CapitalizeInitials(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    private String FormatPrice(float price) {

        String priceToString = "$" + (int) price;

        return priceToString;

    }//closes FormatPrice

}//closes ProductsAdapter class
