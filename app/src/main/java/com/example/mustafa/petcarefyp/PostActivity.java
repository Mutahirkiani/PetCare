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

public class PostActivity extends AppCompatActivity {
    ImageButton imageButton;
    Button button;
    EditText title,description;
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
        setContentView(R.layout.post_activity);

        //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progress = new ProgressDialog(this);
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mStorage = FirebaseStorage.getInstance().getReference();

        imageButton = (ImageButton)findViewById(R.id.imageButton);
        button = (Button) findViewById(R.id.submit);
        description = (EditText) findViewById(R.id.post_description);
        title = (EditText) findViewById(R.id.post_tite);

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
                startUploading();
            }


        });
    }

    private void startUploading() {
        progress.setMessage("Uploading Wait");
        progress.show();
        final String titleValue = title.getText().toString();
        final String descriptionValue = description.getText().toString();

        if(!TextUtils.isEmpty(titleValue)&& !TextUtils.isEmpty(descriptionValue) && imageUri!=null){
            StorageReference mPAthReference = mStorage.child("Blog_images").child(imageUri.getLastPathSegment());
            mPAthReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uId=currentFirebaseUser.getUid();

                            DatabaseReference newPost = mDataBase.push();
                            newPost.child("title").setValue(titleValue);
                            newPost.child("description").setValue(descriptionValue);
                            newPost.child("image").setValue(downloadUrl.toString());
                            newPost.child("id").setValue(uId);
                            progress.dismiss();
                            Intent intent = new Intent(PostActivity.this,BlogActivity.class);
                            startActivity(intent);
                        }

                    });
                }
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
