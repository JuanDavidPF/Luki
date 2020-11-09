package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.luki.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String typeOfUser;
    private MotionLayout animation;

    private Button nextBtn;
    private ImageButton backBtn;


    private EditText nameField;
    private EditText lastNameField;
    private EditText birthDayField;
    private EditText birthMonthField;
    private EditText birthYearField;
    private EditText idCardField;
    private EditText expDayField;
    private EditText expMonthField;
    private EditText expYearfield;
    private EditText emailField;
    private EditText passField;
    private EditText repassField;

    private String name;
    private String birthDate;
    private int idCard;
    private String expDate;
    private String email;
    private String pass;
    private String repass;

    String id;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        backBtn = findViewById(R.id.signUp_btn_back);
        nextBtn = findViewById(R.id.sign_btn_next);
        animation = findViewById(R.id.store_animation);

        nameField = findViewById(R.id.signup_et_name);
        lastNameField = findViewById(R.id.signup_et_lastname);
        birthDayField = findViewById(R.id.signup_editText_bd_dd);
        birthMonthField = findViewById(R.id.signup_editText_bd_mm);
        birthYearField = findViewById(R.id.signup_editText_bd_yyyy);
        idCardField = findViewById(R.id.signup_et_cc);
        expDayField = findViewById(R.id.signup_editText_expedition_day);
        expMonthField = findViewById(R.id.signup_editText_expedition_month);
        expYearfield = findViewById(R.id.signup_editText_expedition_year);
        emailField = findViewById(R.id.sign_et_email);
        passField = findViewById(R.id.sign_et_pass);
        repassField = findViewById(R.id.sign_et_repass);


        typeOfUser = getIntent().getExtras().getString("typeOfUser");
        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }//closes onCreateMethod

    @Override
    public void onClick(View view) {

        switch ((view.getId())) {

            case R.id.sign_btn_next:


                switch (animation.getCurrentState()) {

                    case R.id.end:

                        animation.transitionToState(R.id.secondPhase);
                        break;

                    case R.id.secondPhase:

                        animation.transitionToState(R.id.thirdPhase);

                        break;

                    case R.id.thirdPhase:

                        //finishes the signUp

                        if (checkData()) {
                            deactivateButtons();
                            registerUser();
                        }

                        break;
                }

                break;//closes the button next listener


            case R.id.signUp_btn_back:


                switch (animation.getCurrentState()) {

                    case R.id.end:

                        finish();

                        break;
                    case R.id.secondPhase:

                        animation.transitionToState(R.id.end);

                        break;

                    case R.id.thirdPhase:

                        animation.transitionToState(R.id.secondPhase);
                        break;
                }

                break;

        }//closes view switch

    }//closes onClick method

    private void registerUser() {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    id = mAuth.getCurrentUser().getUid();
                    user = new User(id, name, idCard, birthDate, expDate, email, pass, typeOfUser);
                    registerUserOnDatabase();

                } else {
                    activateButtons();
                    Toast.makeText(SignUp.this, "Falló el registro del usuario. " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }//closes register user

    private void registerUserOnDatabase() {

        mDatabase.child("users").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    idReference();




                } else {
                    activateButtons();
                    Toast.makeText(SignUp.this, "Falló el registro en la base de datos " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }//closes registerUserOnDatabase method

    private void idReference() {

        mDatabase.child("idReference").child(idCard + "").child("UUID").setValue(id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Intent toMain = new Intent();

                    switch (typeOfUser) {
                        case "customer":
                            toMain = new Intent(SignUp.this, Catalogue.class);
                            break;
                        case "seller":
                            toMain = new Intent(SignUp.this, MainSeller.class);
                            break;
                    }

                    SharedPreferences preferences = getSharedPreferences("GLOBAL_PREFERENCES", Context.MODE_PRIVATE);
                    preferences.edit().putString("user_type", typeOfUser).commit();
                    preferences.edit().putBoolean("isFirstTime", false).commit();
                    preferences.edit().putBoolean("hasFinishedTutorial", false).commit();
                    startActivity(toMain);
                    finish();

                } else {
                    activateButtons();
                    Toast.makeText(SignUp.this, "Falló el registro en la base de datos " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }//closes idReference method

    private boolean checkData() {


        if (!isEmpty(nameField) && !isEmpty(lastNameField) && !isEmpty(idCardField) && !isEmpty(birthDayField) &&
                !isEmpty(birthMonthField) && !isEmpty(birthYearField) && !isEmpty(expDayField) &&
                !isEmpty(expMonthField) && !isEmpty(expYearfield) && !isEmpty(emailField) && !isEmpty(passField) && !isEmpty(repassField)) {

            name = nameField.getText().toString().trim().toUpperCase() + " " + lastNameField.getText().toString().trim().toUpperCase();
            idCard = Integer.parseInt(idCardField.getText().toString().trim());
            birthDate = birthDayField.getText().toString().trim() + birthMonthField.getText().toString().trim() + birthYearField.getText().toString().trim();
            expDate = expDayField.getText().toString().trim() + expMonthField.getText().toString().trim() + expYearfield.getText().toString().trim();
            email = emailField.getText().toString().trim().toLowerCase();
            pass = passField.getText().toString();
            repass = repassField.getText().toString();

            Log.e("<<<", birthDate + "");

            if (String.valueOf(idCard).length() == 10) {
                if (birthDate.length() == 8) {
                    if (expDate.length() == 8) {
                        if (pass.equals(repass)) {

                            return true;
                        } else
                            Toast.makeText(this.getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                        return false;
                    } else
                        Toast.makeText(this.getApplicationContext(), "La fecha de expedición ingresada no es valida", Toast.LENGTH_LONG).show();
                    return false;
                } else
                    Toast.makeText(this.getApplicationContext(), "La fecha de nacimiento ingresada no es valida", Toast.LENGTH_LONG).show();
                return false;
            } else
                Toast.makeText(this.getApplicationContext(), "El documento de identidad ingresado no se valido", Toast.LENGTH_LONG).show();
            return false;
        } else
            Toast.makeText(this.getApplicationContext(), "Por favor complete todos los campos de registro", Toast.LENGTH_LONG).show();
        return false;
    }//closes the createUser Method

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void deactivateButtons() {
        nextBtn.setEnabled(false);
    }

    private void activateButtons() {
        nextBtn.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nextBtn.setEnabled(true);

    }
}//closes SignUp class