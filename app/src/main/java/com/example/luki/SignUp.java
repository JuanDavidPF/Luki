package com.example.luki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luki.JSSE.MailSender;
import com.example.luki.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout cameraViewHolder;
    SurfaceView cameraView;
    TextView textScanned;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    TextRecognizer textRecognizer;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String typeOfUser;
    private MotionLayout animation;

    private Button nextBtn;
    private ImageButton backBtn;


    private EditText nameField;
    private EditText lastNameField;
    private EditText birthDayField;
    private EditText idCardField;
    private EditText expDayField;
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

    private String secretCode = "";
    private String userScretCode = "";

    private Button number1, number2, number3, number4, number5, number6, number7, number8, number9, number0;
    private ImageButton deleteNumber;

    private TextView confirmation1, confirmation2, confirmation3, confirmation4;

    String id;
    User user;

    final Calendar birthdayCalendar = Calendar.getInstance();
    final Calendar expeditionCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener birthdayDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            birthdayCalendar.set(Calendar.YEAR, year);
            birthdayCalendar.set(Calendar.MONTH, monthOfYear);
            birthdayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(birthDayField, birthdayCalendar);
        }
    };

    DatePickerDialog.OnDateSetListener expeditionDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            expeditionCalendar.set(Calendar.YEAR, year);
            expeditionCalendar.set(Calendar.MONTH, monthOfYear);
            expeditionCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(expDayField, expeditionCalendar);
        }

    };


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
        birthDayField = findViewById(R.id.signup_editText_bd);
        expDayField = findViewById(R.id.signup_editText_expedition_day);

        idCardField = findViewById(R.id.signup_et_cc);
        emailField = findViewById(R.id.sign_et_email);
        passField = findViewById(R.id.sign_et_pass);
        repassField = findViewById(R.id.sign_et_repass);


        typeOfUser = getIntent().getExtras().getString("typeOfUser");
        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        birthDayField.setOnClickListener(this);
        expDayField.setOnClickListener(this);

        cameraViewHolder = findViewById(R.id.sign_camera);
        cameraView = findViewById(R.id.signUpIDScanner);
        textScanned = findViewById(R.id.sign_textScanned);

        CreateNumpad();

        CreateCameraScanner();
        idCardField.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (idCardField.getRight() - idCardField.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        textScanned.setText("Enfoque su documento de identidad con la cámara");
                        animation.transitionToState(R.id.cameraModal);
                        return true;
                    }
                }
                return false;
            }
        });

    }//closes onCreateMethod

    private void CreateNumpad() {

        number0 = findViewById(R.id.register_numpad_0);
        number1 = findViewById(R.id.register_numpad_1);
        number2 = findViewById(R.id.register_numpad_2);
        number3 = findViewById(R.id.register_numpad_3);
        number4 = findViewById(R.id.register_numpad_4);
        number5 = findViewById(R.id.register_numpad_5);
        number6 = findViewById(R.id.register_numpad_6);
        number7 = findViewById(R.id.register_numpad_7);
        number8 = findViewById(R.id.register_numpad_8);
        number9 = findViewById(R.id.register_numpad_9);
        deleteNumber = findViewById(R.id.register_numpad_erase);

        number0.setOnClickListener(this::EditCode);
        number1.setOnClickListener(this::EditCode);
        number2.setOnClickListener(this::EditCode);
        number3.setOnClickListener(this::EditCode);
        number4.setOnClickListener(this::EditCode);
        number5.setOnClickListener(this::EditCode);
        number6.setOnClickListener(this::EditCode);
        number7.setOnClickListener(this::EditCode);
        number8.setOnClickListener(this::EditCode);
        number9.setOnClickListener(this::EditCode);
        deleteNumber.setOnClickListener(this::EditCode);

        confirmation1 = findViewById(R.id.sign_vt_confirmation_1);
        confirmation2 = findViewById(R.id.sign_vt_confirmation_2);
        confirmation3 = findViewById(R.id.sign_vt_confirmation_3);
        confirmation4 = findViewById(R.id.sign_vt_confirmation_4);

    }

    private void updateLabel(EditText input, Calendar calendary) {
        String myFormat = "MMMM - dd - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.forLanguageTag("es-ES"));
        input.setText(sdf.format(calendary.getTime()));

    }


    private void EditCode(View v) {

        if (v.getId() != R.id.register_numpad_erase && userScretCode.length() < 4) {
            Button btn = (Button) v;

            switch (userScretCode.length()) {

                case 0:
                    confirmation1.setText(btn.getText().toString().trim());
                    break;

                case 1:
                    confirmation2.setText(btn.getText().toString().trim());
                    break;

                case 2:
                    confirmation3.setText(btn.getText().toString().trim());
                    break;

                case 3:
                    confirmation4.setText(btn.getText().toString().trim());
                    break;
            }

            userScretCode += btn.getText().toString().trim();

        } else if (v.getId() == R.id.register_numpad_erase && userScretCode.length()>0) {

            switch (userScretCode.length()) {

                case 1:
                    confirmation1.setText("");
                    break;

                case 2:
                    confirmation2.setText("");
                    break;

                case 3:
                    confirmation3.setText("");
                    break;

                case 4:
                    confirmation4.setText("");
                    break;
            }

            userScretCode =  userScretCode.substring(0,userScretCode.length()-1);
        }

    }//closes EditCode method


    private void CreateCameraScanner() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational())
            Log.w("SignUp", "Las dependencias del detector no está disponible");
        else {
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1280, 1024).setRequestedFps(2.0f).setAutoFocusEnabled(true).build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder holder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SignUp.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {
                        textScanned.post(new Runnable() {
                            @Override
                            public void run() {

                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);

                                    if (item.getValue().contains("NUMERO")) {

                                        String cedula = item.getValue().substring(7, 20);
                                        cedula = cedula.replace(".", "");
                                        textScanned.setText(cedula);
                                        idCardField.setText(cedula);
                                        animation.transitionToState(R.id.end);

                                        break;
                                    }
                                }
                            }
                        });
                    }
                }
            });

        }
    }//closes CreateCameraScanner method

    @Override
    public void onClick(View view) {

        switch ((view.getId())) {

            case R.id.signup_editText_bd:


                new DatePickerDialog(this, birthdayDate, birthdayCalendar
                        .get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH),
                        birthdayCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;


            case R.id.signup_editText_expedition_day:
                new DatePickerDialog(this, expeditionDate, expeditionCalendar
                        .get(Calendar.YEAR), expeditionCalendar.get(Calendar.MONTH),
                        expeditionCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;

            case R.id.sign_btn_next:


                switch (animation.getCurrentState()) {

                    case R.id.end:

                        animation.transitionToState(R.id.secondPhase);
                        break;

                    case R.id.secondPhase:

                        if (checkData()) {
                            animation.transitionToState(R.id.thirdPhase);
                            SendCode(GenerateCode());
                        }
                        break;

                    case R.id.thirdPhase:

                        //finishes the signUp
                        if (secretCode.equals(userScretCode)) {

                            deactivateButtons();
                            registerUser();
                        } else
                            Toast.makeText(this, "El código que ingresaste no coincide con el que enviamos a tu correo", Toast.LENGTH_LONG).show();

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

    private String GenerateCode() {
        String digit1 = String.valueOf(new Random().nextInt(10 - 0) + 0);
        String digit2 = String.valueOf(new Random().nextInt(10 - 0) + 0);
        String digit3 = String.valueOf(new Random().nextInt(10 - 0) + 0);
        String digit4 = String.valueOf(new Random().nextInt(10 - 0) + 0);

        secretCode = digit1 + digit2 + digit3 + digit4;

        return secretCode;
    }//closes GenerateCode method


    private void SendCode(String code) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MailSender sender = new MailSender("equipoluki@gmail.com", "Amnesia2018");
                    sender.sendMail("Autentifica tu cuenta Luki",
                            "Tu código de activación Luki es: " + secretCode,
                            "equipoluki@gmail.com",
                            email);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();


    }//closes SendCode method


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
                    toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


        if (!isEmpty(nameField) && !isEmpty(lastNameField) && !isEmpty(idCardField) && !isEmpty(birthDayField) && !isEmpty(expDayField) && !isEmpty(emailField) && !isEmpty(passField) && !isEmpty(repassField)) {

            name = nameField.getText().toString().trim().toUpperCase() + " " + lastNameField.getText().toString().trim().toUpperCase();

            try {
                String idValue = idCardField.getText().toString();
                idCard = Integer.parseInt(idValue);

            } catch (NumberFormatException e) {
                // handle the exception
            }


            birthDate = birthDayField.getText().toString();
            expDate = expDayField.getText().toString();
            email = emailField.getText().toString().trim().toLowerCase();
            pass = passField.getText().toString();
            repass = repassField.getText().toString();

            if (String.valueOf(idCard).length() >= 8) {

                if (pass.equals(repass)) {

                    return true;
                } else
                    Toast.makeText(this.getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                return false;

            } else
                Toast.makeText(this.getApplicationContext(), "El documento de identidad ingresado no se valido", Toast.LENGTH_LONG).show();
            return false;
        } else
            Toast.makeText(this.getApplicationContext(), "Por favor complete todos los campos de registro", Toast.LENGTH_LONG).show();
        return false;
    }//closes createUser Method

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }//closes isEmpty Method

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case RequestCameraPermissionID:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }//closes onRequestPermissionsResult Method

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }//closes overridePendingTransition Method


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