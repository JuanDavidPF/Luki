package com.example.luki.CatalogueConstructor;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luki.R;
import com.example.luki.Store;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CatalogueView> implements CatalogueView.onCatalogueChosen {

    private ArrayList<ProductCategory> categories;
    private onAdapterResponse listener;

    public CategoriesAdapter() {
        categories = new ArrayList<ProductCategory>();
    }

    public void addCategory(ProductCategory category) {
        categories.add(category);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CatalogueView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.cataloguerow, null);
        ConstraintLayout rowRoot = (ConstraintLayout) row;
        CatalogueView catalogueView = new CatalogueView(rowRoot);
        catalogueView.setListener(this);
        return catalogueView;
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogueView holder, int position) {
        holder.getBackgroundImage().setImageResource(categories.get(position).getBackground());
        holder.getCategory().setText(categories.get(position).getId());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    @Override
    public void toStore(String categoria) {

        if (listener != null) listener.toStore(categoria);
    }

    public void setListener(onAdapterResponse listener) {
        this.listener = listener;
    }

    public interface onAdapterResponse {

        void toStore(String categoria);
    }


}
