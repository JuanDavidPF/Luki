package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    private Button forgotBtn;
    private Button loginBtn;
    private Button signBtn;

    private EditText idcardField;
    private EditText passField;

    private String idCard;
    private String pass;
    private String email;
    private String UUID;
    private String userType;

    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            getUserType();

        }
    }//close onStartMethod

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        idcardField = findViewById(R.id.login_editText_user);
        passField = findViewById(R.id.login_editText_pass);

        forgotBtn = findViewById(R.id.login_btn_forgotpass);
        loginBtn = findViewById(R.id.login_btn_login);
        signBtn = findViewById(R.id.login_btn_signup);

        forgotBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        signBtn.setOnClickListener(this);

    }//closes onCreate method

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_btn_forgotpass:
                //deactivateButtons();
                break;

            case R.id.login_btn_login:
                deactivateButtons();
                getUserID();
                break;


            case R.id.login_btn_signup:
                Intent toWhoAmI = new Intent(this.getApplicationContext(), WhoAmI.class);
                startActivity(toWhoAmI);
                deactivateButtons();
                break;
        }
    }//closes method onClick

    private void logInUser() {

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Intent toMain = new Intent();

                    switch (userType) {
                        case "customer":
                            toMain = new Intent(Login.this, Catalogue.class);
                            break;
                        case "seller":
                            toMain = new Intent(Login.this, MainSeller.class);
                            break;
                    }

                    startActivity(toMain);
                    finish();

                } else {
                    Toast.makeText(Login.this, "Fallo el inicio de sesion: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    activateButtons();
                }//closes error catch

            }
        });

    }//closes logInUser

    private void getUserType() {

        mDatabase.child(currentUser.getUid()).child("user_type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    Intent toMain = new Intent();
                    userType = dataSnapshot.getValue().toString();

                    switch (userType) {
                        case "customer":
                            toMain = new Intent(Login.this, Catalogue.class);
                            break;
                        case "seller":
                            toMain = new Intent(Login.this, MainSeller.class);
                            break;
                    }
                    startActivity(toMain);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, "Hubo un problema al iniciar sesion: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                activateButtons();
            }
        });

    }

    private void getUserID() {

        idCard = idcardField.getText().toString().trim();
        pass = passField.getText().toString();

        if (!idCard.isEmpty() && !pass.isEmpty()) {
            mDatabase.child("idReference").child(idCard).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        UUID = dataSnapshot.child("UUID").getValue().toString();
                        getUserEmail();
                    } else {
                        Toast.makeText(Login.this, "No existe un usuario registrado con esta cedula", Toast.LENGTH_LONG).show();
                        activateButtons();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Login.this, "Hubo un problema al iniciar sesion: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    activateButtons();
                }
            });

        } else {
            Toast.makeText(this.getApplicationContext(), "Por favor complete los campos necesarios", Toast.LENGTH_LONG).show();
            activateButtons();
        }
    }

    private void getUserEmail() {

        mDatabase.child("users").child(UUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    email = dataSnapshot.child("email").getValue().toString();
                    userType = dataSnapshot.child("user_type").getValue().toString();
                    logInUser();
                } else {
                    activateButtons();
                    Toast.makeText(Login.this, "No existe un usuario registrado con esta cedula", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, "Hubo un problema al iniciar sesion: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                activateButtons();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        forgotBtn.setEnabled(true);
        signBtn.setEnabled(true);
        loginBtn.setEnabled(true);
    }

    private void deactivateButtons() {
        forgotBtn.setEnabled(false);
        signBtn.setEnabled(false);
        loginBtn.setEnabled(false);
    }

    private void activateButtons() {
        forgotBtn.setEnabled(true);
        signBtn.setEnabled(true);
        loginBtn.setEnabled(true);
    }

}//closes class Login