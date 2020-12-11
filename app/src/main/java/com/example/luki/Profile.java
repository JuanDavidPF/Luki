package com.example.luki;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView name;
    private TextView type;

    private Button logOut;

    public static Profile newInstance() {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        logOut = root.findViewById(R.id.profile_logOut);

        name = root.findViewById(R.id.profile_name);
        type = root.findViewById(R.id.profile_type);

        logOut.setOnClickListener(this::ClosesSession);

        GetUserData();
        return root;
    }


    private void GetUserData() {


        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name.setText(dataSnapshot.child("name").getValue().toString().trim().split(" ")[0]);
                type.setText(dataSnapshot.child("user_type").getValue().toString().trim());


                if (dataSnapshot.child("user_type").getValue().toString().trim().equals("seller"))
                    type.setText("Vendedor");
                else type.setText("Comprador");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void ClosesSession(View v) {

        mAuth.signOut();
        Intent resume = new Intent();
        resume = new Intent(getActivity(), Login.class);
        resume.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(resume);
        getActivity().finish();
    }//closes ClosesSession method

}//closes Profile constructor