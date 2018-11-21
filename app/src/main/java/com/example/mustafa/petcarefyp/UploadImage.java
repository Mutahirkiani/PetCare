package com.example.mustafa.petcarefyp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mustafa.petcarefyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

public class UploadImage extends Activity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 234;
    Uri filePath;
    Button select;
    Button upload;
    ImageView btn_scan_qr;
    ImageView profilePicture;
    private StorageReference mStorageRef;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageupload);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        select=(Button)findViewById(R.id.select);
        upload=(Button)findViewById(R.id.upload);
        profilePicture=(ImageView)findViewById(R.id.profile_image);
        select.setOnClickListener(this);
        upload.setOnClickListener(this);

    }

    public void uploadFile(){
        if(filePath!=null) {
            final ProgressDialog progressdialog=new ProgressDialog(this);
            progressdialog.setTitle("Uploading.....");
            progressdialog.show();
            Intent intent=getIntent();
            String name= intent.getStringExtra("user");
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            String current_user_id = mCurrentUser.getUid();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference riversRef = storage.getReferenceFromUrl("gs://petcarefyp.appspot.com/").child("Profile Pictures" + "/"+current_user_id+".jpg");


            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Upload Successfull",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressdialog.dismiss();
                            Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressdialog.setMessage(((int) progress) + " % Upload...");
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                final String download_url = task.getResult().getDownloadUrl().toString();
                               FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                                String current_user_id = mCurrentUser.getUid();
                                DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);
                                data.child("image").setValue(download_url);
                            }
                        }
                    })
            ;
        }else{
        }
    }
    public void showFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose an Image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filePath=data.getData();
            try {
                Bitmap bit= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profilePicture.setImageBitmap(bit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onClick(View view) {
        if (view==select){
            showFileChooser();
        }
        else if(view==upload){
            uploadFile();
        }
    }

}