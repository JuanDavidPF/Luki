package com.example.luki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.luki.CatalogueConstructor.CatalogueView;
import com.example.luki.CatalogueConstructor.CategoriesAdapter;
import com.example.luki.CatalogueConstructor.ProductCategory;
import com.example.luki.model.Product;

public class Catalogue extends AppCompatActivity implements CategoriesAdapter.onAdapterResponse {


    private RecyclerView categoriesViewList;
    private LinearLayoutManager layoutManager;
    private CategoriesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);


        categoriesViewList = findViewById(R.id.categoriesViewList);
        categoriesViewList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        categoriesViewList.setLayoutManager(layoutManager);
        adapter = new CategoriesAdapter();
        categoriesViewList.setAdapter(adapter);

        adapter.addCategory(new ProductCategory("Calzado", R.drawable.calzado_background_card));
        adapter.addCategory(new ProductCategory("Accesorios", R.drawable.accesorios_background_card));
        adapter.addCategory(new ProductCategory("Ropa", R.drawable.ropa_background_card));
        adapter.addCategory(new ProductCategory("Tecnología", R.drawable.tecnologia_background_card));
        adapter.addCategory(new ProductCategory("Maquillaje", R.drawable.maquillaje_background_card));
        adapter.setListener(this);

    }

    @Override
    public void toStore(String categoria) {
        Intent toStore = new Intent(this, Store.class);
        toStore.putExtra("categoria", categoria);
        startActivity(toStore);
        categoriesViewList.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoriesViewList.setEnabled(true);
    }


}