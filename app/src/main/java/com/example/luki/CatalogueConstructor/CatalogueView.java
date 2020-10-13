package com.example.luki.CatalogueConstructor;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luki.Catalogue;
import com.example.luki.R;
import com.example.luki.Store;

public class CatalogueView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ConstraintLayout root;
    private ImageButton backgroundImage;
    private TextView category;
    private onCatalogueChosen listener;

    public CatalogueView(ConstraintLayout root) {
        super(root);
        this.root = root;


        backgroundImage = root.findViewById(R.id.categoryBackground);
        category = root.findViewById(R.id.categoryName);

        backgroundImage.setOnClickListener(this);
    }

    public ConstraintLayout getRoot() {
        return root;
    }

    public ImageView getBackgroundImage() {
        return backgroundImage;
    }

    public TextView getCategory() {
        return category;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) listener.toStore(category.getText().toString());
    }

    public void setListener(onCatalogueChosen listener) {
        this.listener = listener;
    }

    public interface onCatalogueChosen {

        void toStore(String categoria);
    }

}
