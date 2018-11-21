package com.example.mustafa.petcarefyp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by raja m afzal on 4/10/2018.
 */

public class SingleAd extends AppCompatActivity {

    public String user;
    Button delete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ads_activity);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Ads");
        final String blogId = getIntent().getExtras().getString("blogId");
        final TextView title=(TextView)findViewById(R.id.title);
        final TextView gender=(TextView)findViewById(R.id.gender);
        final TextView phoneNo=(TextView)findViewById(R.id.phoneNo);
        final TextView city=(TextView)findViewById(R.id.city);
        final TextView country=(TextView)findViewById(R.id.country);
        final TextView price=(TextView)findViewById(R.id.price);
        final ImageView adImage=(ImageView)findViewById(R.id.adImage);
        final TextView author=(TextView)findViewById(R.id.author);

        delete=(Button)findViewById(R.id.delete);

        databaseReference.child(blogId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title1=(String) dataSnapshot.child("title").getValue();
                String number=(String) dataSnapshot.child("number").getValue();
                String city1=(String) dataSnapshot.child("city").getValue();
                String country1=(String) dataSnapshot.child("country").getValue();
                String price1=(String) dataSnapshot.child("price").getValue();
                String img=(String) dataSnapshot.child("image").getValue();
                String gender1=(String) dataSnapshot.child("gender").getValue();
                title.setText(title1);
                phoneNo.setText(number);
                city.setText(city1);
                country.setText(country1);
                price.setText(price1);
                gender.setText(gender1);
                Picasso.with(getApplicationContext()).load(img).into(adImage);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uId=currentFirebaseUser.getUid();
        DatabaseReference myDatabase=FirebaseDatabase.getInstance().getReference().child("Ads").child(blogId);
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user =(String) dataSnapshot.child("id").getValue();
                if(uId.equals(user)){
                    delete.setVisibility(View.VISIBLE);
                }
                DatabaseReference getData= FirebaseDatabase.getInstance().getReference().child("Users").child(user);



                getData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name1 = dataSnapshot.child("name").getValue().toString();
                        author.setText(name1);
                        author.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent1=new Intent(getApplicationContext(),OtherProfile.class);
                                Intent intent11=intent1.putExtra("key",user);
                                startActivity(intent1);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myData=FirebaseDatabase.getInstance().getReference().child("Ads");
                myData.child(blogId).
                        removeValue();
                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });



    }
}
