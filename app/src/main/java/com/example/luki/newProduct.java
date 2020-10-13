package com.example.luki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luki.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class newProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private Spinner categoriesInput;
    private File file;
    private int numberOfPhotos = 0;
    private ImageButton[] addPhoto;
    public static final int PERMISSIONS_CALLBACK = 153;
    public static final int CAMERA_CALLBACK = 921;
    private Button btnAdd;

    private EditText productNameField;
    private EditText productDescriptionField;
    private EditText productPriceField;

    private String productName;
    private String productDescription;
    private float productPrice;
    private String productCategory;

    private ArrayList<Uri> productPictures;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        productPictures = new ArrayList<Uri>();

        categoriesInput = findViewById(R.id.newProduct_spinner_categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesInput.setAdapter(adapter);
        categoriesInput.setOnItemSelectedListener(this);

        addPhoto = new ImageButton[6];
        for (int i = 0; i < addPhoto.length; i++) {
            int tag = 0;
            switch (i) {
                case 0:
                    tag = R.id.newProduct_imgBtn_1;
                    break;
                case 1:
                    tag = R.id.newProduct_imgBtn_2;

                    break;
                case 2:
                    tag = R.id.newProduct_imgBtn_3;

                    break;
                case 3:
                    tag = R.id.newProduct_imgBtn_4;

                    break;
                case 4:
                    tag = R.id.newProduct_imgBtn_5;

                    break;
                case 5:
                    tag = R.id.newProduct_imgBtn_6;
                    break;
            }

            addPhoto[i] = findViewById(tag);
            addPhoto[i].setOnClickListener(this);
        }


        productNameField = findViewById(R.id.newProduct_et_name);
        productDescriptionField = findViewById(R.id.newProduct_et_description);
        productPriceField = findViewById(R.id.newProduct_et_price);
        productCategory = "";

        btnAdd = findViewById(R.id.newProduct_btn_add);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0) {
            productCategory = "";
        } else {
            productCategory = adapterView.getItemAtPosition(i).toString().toLowerCase();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    private void takePicture() {

        if (numberOfPhotos < 6) {

            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/photo.png");
            Uri uri = FileProvider.getUriForFile(this, getPackageName(), file);
            productPictures.add(uri);
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePhoto, CAMERA_CALLBACK);
        } else {
            Toast.makeText(this.getApplicationContext(), "Ya has llegado al limite de fotos por producto", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.newProduct_btn_add) {

            getProductData();

            if (!productName.isEmpty() && !productDescription.isEmpty() && !productCategory.isEmpty() && productPrice + "".length() > 0 && productPictures.size() > 0) {

                uploadProduct();
            } else
                Toast.makeText(this.getApplicationContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();


        } else if (view.getId() != R.id.newProduct_btn_add) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CALLBACK);
        }

        switch (view.getId()) {

            case R.id.newProduct_imgBtn_1:
                if (numberOfPhotos == 0) {
                    takePicture();
                }
                break;

            case R.id.newProduct_imgBtn_2:
                if (numberOfPhotos == 1) {
                    takePicture();
                }
                break;

            case R.id.newProduct_imgBtn_3:
                if (numberOfPhotos == 2) {
                    takePicture();
                }
                break;

            case R.id.newProduct_imgBtn_4:
                if (numberOfPhotos == 3) {
                    takePicture();
                }
                break;

            case R.id.newProduct_imgBtn_5:
                if (numberOfPhotos == 4) {
                    takePicture();
                }
                break;

            case R.id.newProduct_imgBtn_6:
                if (numberOfPhotos == 5) {
                    takePicture();
                }
                break;

        }
    }//closes onclick

    private void getProductData() {
        productName = productNameField.getText().toString().trim().toLowerCase();
        productDescription = productDescriptionField.getText().toString().trim().toLowerCase();
        if (productPriceField.getText().toString().length() > 0)
            productPrice = Float.parseFloat(productPriceField.getText().toString().trim());
    }

    private void uploadProduct() {

        btnAdd.setEnabled(false);
        String id = mAuth.getCurrentUser().getUid();
        String productId = UUID.randomUUID().toString();

        for (int i = 0; i < productPictures.size(); i++) {

            StorageReference productRef = mStorageRef.child(productId + "(" + i + ")");

            productRef.putFile(productPictures.get(i))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri url = taskSnapshot.getUploadSessionUri();
                            //finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(newProduct.this, "Hubo un problema al subir las fotos, intente de nuevo mas tarde", Toast.LENGTH_LONG).show();
                            btnAdd.setEnabled(true);
                        }
                    });
        }
        btnAdd.setEnabled(false);

        Product product = new Product(productName, productDescription, productPrice, id);

        mDatabase.child("productsGlobal").child(productCategory).child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    finish();

                } else
                    btnAdd.setEnabled(true);
                Toast.makeText(newProduct.this, "Hubo un problema almacenando tu información en la base de datos, intentalo mas tarde", Toast.LENGTH_LONG).show();
            }
        });
        btnAdd.setEnabled(false);
        mDatabase.child("productosPropios").child(id).child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    finish();

                } else
                    Toast.makeText(newProduct.this, "Hubo un problema almacenando tu información en la base de datos, intentalo mas tarde", Toast.LENGTH_LONG).show();
                btnAdd.setEnabled(true);

            }
        });

    }//closes upload product


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CALLBACK && resultCode == RESULT_OK) {
            Bitmap image = BitmapFactory.decodeFile(file.getPath());
            Bitmap thumbnail = Bitmap.createScaledBitmap(image, image.getWidth() / 10, image.getHeight() / 10, true);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedThumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
            Bitmap rotatedImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
            addPhoto[numberOfPhotos].setImageBitmap(rotatedThumbnail);
            numberOfPhotos += 1;

        }
    }//close onActivityResult

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_CALLBACK) {
            boolean allGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allGranted = false;
                    Toast.makeText(this.getApplicationContext(), "Alera! No todos los permisos fueron concedidos", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }//closes OnRequestPermissionsResul
}//closes new product class