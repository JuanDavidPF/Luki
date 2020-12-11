package com.example.luki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.luki.Bills.BillsAdapter;
import com.example.luki.CatalogueConstructor.CatalogueView;
import com.example.luki.CatalogueConstructor.CategoriesAdapter;
import com.example.luki.CatalogueConstructor.ProductCategory;
import com.example.luki.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Catalogue extends Fragment implements CategoriesAdapter.onAdapterResponse {


    private RecyclerView categoriesViewList;
    private LinearLayoutManager layoutManager;
    private CategoriesAdapter adapter;
    private boolean categoryClicked = false;


    public static Catalogue newInstance() {
        Catalogue fragment = new Catalogue();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }//closes newInstance method

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_catalogue, container, false);

        categoriesViewList = root.findViewById(R.id.categoriesViewList);
        categoriesViewList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        categoriesViewList.setLayoutManager(layoutManager);
        adapter = new CategoriesAdapter();
        categoriesViewList.setAdapter(adapter);

        adapter.addCategory(new ProductCategory("Calzado", R.drawable.calzado_background_card));
        adapter.addCategory(new ProductCategory("Accesorios", R.drawable.accesorios_background_card));
        adapter.addCategory(new ProductCategory("Ropa", R.drawable.ropa_background_card));
        adapter.addCategory(new ProductCategory("Tecnología", R.drawable.tecnologia_background_card));
        adapter.addCategory(new ProductCategory("Maquillaje", R.drawable.maquillaje_background_card));
        adapter.setListener(this);

        return root;
    }//closes onCreateView method


    @Override
    public void toStore(String categoria) {
        if (!categoryClicked) {
            categoryClicked = true;
            Intent toStore = new Intent(getActivity(), Store.class);
            toStore.putExtra("categoria", categoria);
            startActivity(toStore);

        }
    }//cloese toStore

    public void resumeFragment(){

        categoryClicked = false;

    }//cloese resumeFragment method


}//closes Catalogue class