package com.example.luki;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    private MainSeller mainSeller;
    private MainCustomer mainCustomer;
    private Catalogue catalogue;
    private Profile profile;
    private BottomNavigationView navigationBottom;
    private String typeOfUser;
    private Fragment main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationBottom = findViewById(R.id.home_bottomNB);

        mainSeller = MainSeller.newInstance();
        mainCustomer = MainCustomer.newInstance();
        catalogue = Catalogue.newInstance();
        profile = Profile.newInstance();

        SharedPreferences pref = this.getSharedPreferences("GLOBAL_PREFERENCES", Context.MODE_PRIVATE);
        typeOfUser = pref.getString("user_type", null);

        switch (typeOfUser) {
            case "customer":
                main = mainCustomer;
                break;

            case "seller":
                main = mainSeller;
                break;
        }


        navigationBottom.setOnNavigationItemSelectedListener(
                (menuItem) -> {

                    switch (menuItem.getItemId()) {

                        case R.id.mainMenu_profile:
                            showFragment(profile);
                            break;

                        case R.id.mainMenu_catalogue:
                            showFragment(catalogue);
                            break;

                        case R.id.mainMenu_home:
                            showFragment(main);

                            break;

                        case R.id.mainMenu_wallet:

                            break;

                        case R.id.mainMenu_notifications:

                            break;
                    }
                    return true;
                }
        );

        navigationBottom.setSelectedItemId(R.id.mainMenu_home);
    }//closes on create method


    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_fragmentContainer, fragment);
        transaction.commit();

    }//closes showFragment method

    @Override
    public void onResume() {
        super.onResume();
        catalogue.resumeFragment();
    }//closes onResume method

}//closes Home method