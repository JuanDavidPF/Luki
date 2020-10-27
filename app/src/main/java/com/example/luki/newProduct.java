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
import java.util.HashMap;
import java.util.UUID;

public class newProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private ArrayList<Uri> productPictures;
    private int photosUploaded = 0;
    private File file;
    private int numberOfPhotos = 0;
    private ImageButton[] addPhoto;
    public static final int PERMISSIONS_CALLBACK = 153;
    public static final int CAMERA_CALLBACK = 921;

    private EditText productNameField;
    private EditText productDescriptionField;
    private EditText productPriceField;

    private String productName;
    private String productDescription;
    private float productPrice;

    private Spinner categoriesInput;
    private String productCategory;

    private Button btnAdd;

    private String sellerID;
    private String productId;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        ActivityCompat.requestPermissions(
                this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CALLBACK);

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
        addPhoto[0] = findViewById(R.id.newProduct_imgBtn_1);
        addPhoto[0].setOnClickListener(this);
        addPhoto[1] = findViewById(R.id.newProduct_imgBtn_2);
        addPhoto[1].setOnClickListener(this);
        addPhoto[2] = findViewById(R.id.newProduct_imgBtn_3);
        addPhoto[2].setOnClickListener(this);
        addPhoto[3] = findViewById(R.id.newProduct_imgBtn_4);
        addPhoto[3].setOnClickListener(this);
        addPhoto[4] = findViewById(R.id.newProduct_imgBtn_5);
        addPhoto[4].setOnClickListener(this);
        addPhoto[5] = findViewById(R.id.newProduct_imgBtn_6);
        addPhoto[5].setOnClickListener(this);


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
        productCategory = "";
    }

    private void takePicture() {

        if (numberOfPhotos < 6) {
            Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + UUID.randomUUID().toString() + ".png");
            Uri uri = FileProvider.getUriForFile(this, getPackageName(), file);
            productPictures.add(uri);
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takePhoto, CAMERA_CALLBACK);
        } else {
            Toast.makeText(this.getApplicationContext(), "Ya has llegado al limite de fotos por producto", Toast.LENGTH_LONG);
        }
    }//closes takePicture method

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.newProduct_btn_add:

                if (getProductData()) {
                    deactivateButtons();
                    uploadProduct();
                }

                break;

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


        }//closes ViewGetId() switch
    }//closes onclick

    private boolean getProductData() {

        if (!isEmpty(productNameField) && !isEmpty(productDescriptionField) && !isEmpty(productPriceField) && productPictures.size() > 0 && productCategory.length() > 0) {
            productName = productNameField.getText().toString().trim().toLowerCase();
            productDescription = productDescriptionField.getText().toString().trim().toLowerCase();
            productPrice = Float.parseFloat(productPriceField.getText().toString().trim());
            return true;
        } else {
            Toast.makeText(this, "Por favor complete todos los campos de información", Toast.LENGTH_LONG).show();
            return false;
        }
    }//closes getProductname

    private void uploadProduct() {

        sellerID = mAuth.getCurrentUser().getUid();
        productId = UUID.randomUUID().toString();
        product = new Product(sellerID, productCategory, productId, productName, productDescription, productPrice);

        uploadProductToDatabase();
    }//closes upload product

    private void uploadProductToDatabase() {

        mDatabase.child("products").child(productCategory).child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    uploadProductReferenceToSeller();
                } else {
                    activateButtons();
                    Toast.makeText(newProduct.this, "No se pudó postear el producto a la base de datos. " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }//closes uploadProductToDatabase

    private void uploadProductReferenceToSeller() {
        HashMap<String, Object> productMetaData = new HashMap<>();
        productMetaData.put("UUID", productId);
        productMetaData.put("category", productCategory);

        mDatabase.child("users").child(sellerID).child("products").child(productId).setValue(productMetaData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    uploadPictures();
                } else {
                    activateButtons();
                    Toast.makeText(newProduct.this, "No se pudó postear el producto en tu cuenta" + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }//close upload Product reference to seller;

    private void uploadPictures() {

        photosUploaded = 0;
        Toast.makeText(newProduct.this, "Las fotos del producto se están subiendo, por favor espere", Toast.LENGTH_LONG).show();

        //upload pictures to storage
        for (int i = 0; i < productPictures.size(); i++) {

            StorageReference productRef = mStorageRef.child(productId).child(productId + "-" + i);

            productRef.putFile(productPictures.get(i))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            photosUploaded += 1;

                            if (photosUploaded >= productPictures.size()) {
                                Toast.makeText(newProduct.this, "El producto se ha subido exitosamente", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(newProduct.this, "Se han subido " + photosUploaded + " de " + productPictures.size() + " fotos.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            activateButtons();
                            Toast.makeText(newProduct.this, "Hubo un problema al subir las fotos del producto. " + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }//closes for of every picture
    }//closes uploadPictures

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
    }//closes OnRequestPermissionsResulT

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void deactivateButtons() {
        btnAdd.setEnabled(false);

        for (int i = 0; i < addPhoto.length; i++) {
            addPhoto[i].setEnabled(false);
        }
    }//close deactivate buttons

    private void activateButtons() {
        btnAdd.setEnabled(true);

        for (int i = 0; i < addPhoto.length; i++) {
            addPhoto[i].setEnabled(true);
        }
    }


}//closes new product class