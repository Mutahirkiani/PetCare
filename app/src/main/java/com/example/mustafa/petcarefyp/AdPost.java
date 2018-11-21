package com.example.mustafa.petcarefyp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Zubair on 8/27/2017.
 */

public class AdPost extends AppCompatActivity {
    ImageButton imageButton;
    Button button;
    EditText title,number,city,country,additional,price;
    Uri imageUri = null;
    StorageReference mStorage;
    private static final int GALLERY_REQ_CODE = 1;
    ProgressDialog progress;
    DatabaseReference mDataBase;
    //DatabaseReference mDatabaseUsers;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_ads);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progress = new ProgressDialog(this);
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Ads");

        mStorage = FirebaseStorage.getInstance().getReference();

        title=(EditText)findViewById(R.id.title);
        number=(EditText)findViewById(R.id.post_description);
        city=(EditText)findViewById(R.id.city);
        country=(EditText)findViewById(R.id.country);
        additional=(EditText)findViewById(R.id.additional);
        price=(EditText)findViewById(R.id.price);
        button=(Button)findViewById(R.id.submit);
        imageButton=(ImageButton)findViewById(R.id.imageButton);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.rg);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQ_CODE);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setMessage("Uploading Wait");
                progress.show();
                final String titleValue = title.getText().toString();
                final String numberValue = number.getText().toString();
                final String cityValue = city.getText().toString();
                final String countryValue = country.getText().toString();
                final String priceValue = price.getText().toString();
                final String additionalValue = additional.getText().toString();
                final int selectedRadioButtonID = rg.getCheckedRadioButtonId();
                if(!TextUtils.isEmpty(titleValue)|| !TextUtils.isEmpty(priceValue) || !TextUtils.isEmpty(numberValue) || imageUri!=null){
                    StorageReference mPAthReference = mStorage.child("Ads" +
                            "_images").child(imageUri.getLastPathSegment());
                    mPAthReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uId=currentFirebaseUser.getUid();

                            DatabaseReference newPost = mDataBase.push();
                            newPost.child("title").setValue(titleValue);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("id").setValue(uId);
                            newPost.child("number").setValue(numberValue);
                            newPost.child("city").setValue(cityValue);
                            newPost.child("country").setValue(countryValue);
                            newPost.child("price").setValue(priceValue);
                            newPost.child("additional").setValue(additionalValue);
                            int selectedRadioButtonID = rg.getCheckedRadioButtonId();

                            // If nothing is selected from Radio Group, then it return -1
                            if (selectedRadioButtonID != -1) {

                                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                                String selectedRadioButtonText = selectedRadioButton.getText().toString();

                                newPost.child("gender").setValue(selectedRadioButtonText+"");
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Please Select Gender",Toast.LENGTH_LONG).show();
                            }
                            progress.dismiss();
                            Intent intent = new Intent(AdPost.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Title, Price, Picture and number must be filled",Toast.LENGTH_LONG).show();
                    progress.hide();
                }
            }
        });

    }

    private void startUploading() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQ_CODE&&resultCode==RESULT_OK){
            imageUri= data.getData();
            imageButton.setImageURI(imageUri);
        }
    }
}
