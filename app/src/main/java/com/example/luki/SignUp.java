package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luki.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String typeOfUser;
    private MotionLayout animation;

    private Button nextBtn;

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
    private int birthDate;
    private int idCard;
    private int expDate;
    private String email;
    private String pass;
    private String repass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        nextBtn = findViewById(R.id.sign_btn_next);
        animation = findViewById(R.id.animation);

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
                        deactivateButtons();
                        getUserInfo();
                        if (checkData()) {
                            registerUser();
                        }


                        break;
                }


                break;//closes the button next listenel


        }//closes view switch

    }//closes onClick method


    private void registerUser() {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String id = mAuth.getCurrentUser().getUid();
                    User user = new User(id, name, idCard, birthDate, expDate, email, pass, typeOfUser);

                    mDatabase.child("users").child(idCard + "").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {

                                Intent toMain = new Intent();

                                switch (typeOfUser) {
                                    case "customer":
                                        toMain = new Intent(SignUp.this, MainCustomer.class);
                                        break;
                                    case "seller":
                                        toMain = new Intent(SignUp.this, MainSeller.class);
                                        break;
                                }

                                startActivity(toMain);

                                finish();


                            } else {
                                Toast.makeText(SignUp.this, "Hubo un problema almacenando tu información en la base de datos, intentalo mas tarde", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                } else {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    switch (errorCode) {

                        case "ERROR_INVALID_CUSTOM_TOKEN":
                            Toast.makeText(SignUp.this, "El formato del Token personalizado es incorrecto, revisa la documentación", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(SignUp.this, "El token personalizado corresponde a una audiencia distina", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(SignUp.this, "Las credenciales de autencación están malformadas o han expirado", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(SignUp.this, "El email tiene un formato invalido", Toast.LENGTH_LONG).show();
                            emailField.setError("El email tiene un formato invalido");
                            emailField.requestFocus();
                            break;

                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(SignUp.this, "La contraseña es invalida o el usuario no tiene una contraseña.", Toast.LENGTH_LONG).show();
                            passField.setError("contraseña incorrecta");
                            passField.requestFocus();
                            passField.setText("");
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(SignUp.this, "Las credenciales no corresponen al usuario anteriormente logeado.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_REQUIRES_RECENT_LOGIN":
                            Toast.makeText(SignUp.this, "Esta operación es delicada, es necesario que haga un login de manera reciente", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(SignUp.this, "Ya existe una cuenta registrada con este email pero con otras credenciales, intente inciar sesion con una cuenta que use este email", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(SignUp.this, "Ya hay una cuenta registrada con este correo", Toast.LENGTH_LONG).show();
                            emailField.setError("Ya hay una cuenta registrada con este correo");
                            emailField.requestFocus();
                            break;

                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(SignUp.this, "Estas credenciales están asociadas a una cuenta ya existente", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(SignUp.this, "La cuenta de usuario ha sido desactivada por el administrador.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_TOKEN_EXPIRED":
                            Toast.makeText(SignUp.this, "Se cerró sesión, inicie nuevamente", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(SignUp.this, "No hay información acerca de este usuario, la cuenta pudo haber sido borrada", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_OPERATION_NOT_ALLOWED":
                            Toast.makeText(SignUp.this, "Está operación no está permitida, debe configurarse en la consola", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_WEAK_PASSWORD":
                            Toast.makeText(SignUp.this, "La contraseña es muy debil", Toast.LENGTH_LONG).show();
                            passField.setError("La contraseña no es valida, debe contener al menos 6 caracteres");
                            passField.requestFocus();
                            break;

                    }
                }//closes error catch


            }//closes onComplete
        });
    }//closes register user


    private boolean checkData() {
        if (!name.isEmpty() && String.valueOf(idCard).length() > 0 && String.valueOf(birthDate).length() > 0 && String.valueOf(expDate).length() > 0 && !email.isEmpty() && !pass.isEmpty() && !repass.isEmpty()) {
            if (String.valueOf(idCard).length() == 10) {
                if (String.valueOf(birthDate).length() == 8) {
                    if (String.valueOf(expDate).length() == 8) {
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

    private void getUserInfo() {
        name = nameField.getText().toString().trim().toUpperCase() + " " + lastNameField.getText().toString().trim().toUpperCase();
        idCard = Integer.parseInt(idCardField.getText().toString().trim());
        birthDate = Integer.parseInt(birthDayField.getText().toString().trim() + birthMonthField.getText().toString().trim() + birthYearField.getText().toString().trim());
        expDate = Integer.parseInt(expDayField.getText().toString().trim() + expMonthField.getText().toString().trim() + expYearfield.getText().toString().trim());
        email = emailField.getText().toString().trim().toLowerCase();
        pass = passField.getText().toString();
        repass = repassField.getText().toString();

    }//closes getUserInfo

    private void deactivateButtons() {
        nextBtn.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nextBtn.setEnabled(true);

    }
}//closes SignUp class