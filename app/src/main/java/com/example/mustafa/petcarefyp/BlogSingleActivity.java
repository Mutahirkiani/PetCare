package com.example.mustafa.petcarefyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogSingleActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    public String user;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);


        final ImageView blogImage=(ImageView)findViewById(R.id.post_image);
        final TextView post_title=(TextView)findViewById(R.id.post_tite);
        final TextView post_desc=(TextView)findViewById(R.id.post_description);
        final TextView author=(TextView)findViewById(R.id.textView2);
        delete=(Button)findViewById(R.id.delete);

        final String blogId = getIntent().getExtras().getString("blogId");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");

        mDatabase.child(blogId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title=(String) dataSnapshot.child("title").getValue();
                String desc=(String) dataSnapshot.child("description").getValue();
                String img=(String) dataSnapshot.child("image").getValue();

                post_desc.setText(desc);
                post_title.setText(title);
                Picasso.with(getApplicationContext()).load(img).into(blogImage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference myDatabase=FirebaseDatabase.getInstance().getReference().child("Blog").child(blogId);
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user =(String) dataSnapshot.child("id").getValue();
                DatabaseReference getData= FirebaseDatabase.getInstance().getReference().child("Users").child(user);

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String uId=currentFirebaseUser.getUid();

                if(uId.equals(user)){
                    delete.setVisibility(View.VISIBLE);
                }
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
                DatabaseReference myData=FirebaseDatabase.getInstance().getReference().child("Blog");
                myData.child(blogId).
                        removeValue();
                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });

    }

}

