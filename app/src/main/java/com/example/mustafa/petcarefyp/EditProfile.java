package com.example.mustafa.petcarefyp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by raja m afzal on 3/7/2018.
 */

public class EditProfile extends AppCompatActivity {

    EditText name,phoneNo,address,email;
    String name1,phoneNo1,address1,email1;
    Button done;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        name=(EditText)findViewById(R.id.name);
        phoneNo=(EditText)findViewById(R.id.phoneNo);
        address=(EditText)findViewById(R.id.address);
        email=(EditText)findViewById(R.id.email);



        FirebaseUser currentUserId = FirebaseAuth.getInstance().getCurrentUser();
        String userid = currentUserId.getUid();
        final DatabaseReference saveData= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        done=(Button)findViewById(R.id.Done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name1=name.getText().toString();
                phoneNo1=phoneNo.getText().toString();
                address1=address.getText().toString();
                email1=email.getText().toString();

                saveData.child("name").setValue(name1);
                saveData.child("PhoneNo").setValue(phoneNo1);
                saveData.child("Address").setValue(address1);
                saveData.child("Email").setValue(email1);

                name.setText("");
                phoneNo.setText("");
                address.setText("");
                email.setText("");

                Intent intent1=new Intent(getApplicationContext(),Profile.class);
                startActivity(intent1);
            }
        });

    }
}
