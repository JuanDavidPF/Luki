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
    private DatabaseReference mDatabase;

    private Button forgotBtn;
    private Button loginBtn;
    private Button signBtn;

    private EditText idcardField;
    private EditText passField;

    private String idCard;
    private String pass;
    private String email;
    private String userType;

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            Intent intent = new Intent(this.getApplicationContext(), MainCustomer.class);
            startActivity(intent);
            finish();
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
                getUserCredentials();
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
                            toMain = new Intent(Login.this, MainCustomer.class);
                            break;
                        case "seller":
                            toMain = new Intent(Login.this, MainSeller.class);
                            break;
                    }

                    startActivity(toMain);
                    finish();

                } else {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    switch (errorCode) {

                        case "ERROR_INVALID_CUSTOM_TOKEN":
                            Toast.makeText(Login.this, "El formato del Token personalizado es incorrecto, revisa la documentación", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(Login.this, "El token personalizado corresponde a una audiencia distina", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(Login.this, "Las credenciales de autencación están malformadas o han expirado", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(Login.this, "El email tiene un formato invalido", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(Login.this, "La contraseña es invalida o el usuario no tiene una contraseña.", Toast.LENGTH_LONG).show();
                            passField.setError("contraseña incorrecta");
                            passField.requestFocus();
                            passField.setText("");
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(Login.this, "Las credenciales no corresponen al usuario anteriormente logeado.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_REQUIRES_RECENT_LOGIN":
                            Toast.makeText(Login.this, "Esta operación es delicada, es necesario que haga un login de manera reciente", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(Login.this, "Ya existe una cuenta registrada con este email pero con otras credenciales, intente inciar sesion con una cuenta que use este email", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(Login.this, "Ya hay una cuenta registrada con este correo", Toast.LENGTH_LONG).show();

                            break;

                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(Login.this, "Estas credenciales están asociadas a una cuenta ya existente", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(Login.this, "La cuenta de usuario ha sido desactivada por el administrador.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_TOKEN_EXPIRED":
                            Toast.makeText(Login.this, "Se cerró sesión, inicie nuevamente", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(Login.this, "No hay información acerca de este usuario, la cuenta pudo haber sido borrada", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_OPERATION_NOT_ALLOWED":
                            Toast.makeText(Login.this, "Está operación no está permitida, debe configurarse en la consola", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_WEAK_PASSWORD":
                            Toast.makeText(Login.this, "La contraseña es muy debil", Toast.LENGTH_LONG).show();
                            passField.setError("La contraseña no es valida, debe contener al menos 6 caracteres");
                            passField.requestFocus();
                            break;
                    }
                }//closes error catch

            }
        });

    }//closes logInUser

    private void getUserCredentials() {

        idCard = idcardField.getText().toString().trim();
        pass = passField.getText().toString();

        if (!idCard.isEmpty() && !pass.isEmpty()) {
            mDatabase.child("users").child(idCard).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        email = dataSnapshot.child("email").getValue().toString();
                        userType = dataSnapshot.child("user_type").getValue().toString();
                        logInUser();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(this.getApplicationContext(), "Por favor complete los campos necesarios", Toast.LENGTH_LONG).show();

        }
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

}//closes class Login