package com.example.mustafa.petcarefyp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

/**
 * Created by raja m afzal on 3/7/2018.
 */

public class OtherProfile extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherprofile);

        final TextView name=(TextView)findViewById(R.id.name);
        final TextView phoneNo=(TextView)findViewById(R.id.phoneNo);
        final TextView address=(TextView)findViewById(R.id.address);
        final TextView email=(TextView)findViewById(R.id.email);
        final ImageView profile=(ImageView)findViewById(R.id.imageView3);

        Intent intent=getIntent();
        String current_user_id=intent.getStringExtra("key");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petcarefyp.appspot.com/").child("Profile Pictures" + "/"+current_user_id+".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profile.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        } catch (IOException e) {
        }


        FirebaseUser currentUserId = FirebaseAuth.getInstance().getCurrentUser();
        String userid = currentUserId.getUid();
        DatabaseReference getData= FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

        getData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name1 = dataSnapshot.child("name").getValue().toString();
                final String phoneNo1 = dataSnapshot.child("PhoneNo").getValue().toString();
                String address1 = dataSnapshot.child("Address").getValue().toString();
                String email1 = dataSnapshot.child("Email").getValue().toString();
                // String image1=dataSnapshot.child("image").getValue().toString();

                name.setText(name1);
                phoneNo.setText(phoneNo1);
                address.setText(address1);
                email.setText(email1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}