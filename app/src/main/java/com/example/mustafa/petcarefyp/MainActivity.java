package com.example.mustafa.petcarefyp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mustafa.petcarefyp.R.id.textView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView mNavigationView;

    RecyclerView mBlogList;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Ads");

        databaseReference.keepSynced(true);
        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mBlogList.setLayoutManager(mLayoutManager);


        mNavigationView=(NavigationView)findViewById(R.id.nav_view);
        final TextView userName=(TextView)mNavigationView.getHeaderView(0).findViewById(R.id.textView);
        final CircleImageView profile_image =(CircleImageView)mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);

       FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = currentUser.getUid();



        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                userName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_user_id = mCurrentUser.getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petcarefyp.appspot.com/").child("Profile Pictures" + "/"+current_user_id+".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profile_image.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        } catch (IOException e) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(item.getItemId()==R.id.action_add){
            Intent intent = new Intent(getApplicationContext(),AdPost.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


       if (id == R.id.settings) {
           Intent intent=new Intent(getApplicationContext(),Profile.class);
           startActivity(intent);

        } else if (id == R.id.blogs) {
           Intent intent1=new Intent(getApplicationContext(),BlogActivity.class);
           startActivity(intent1);
        }
       else if (id == R.id.maps) {
           Intent intent1=new Intent(getApplicationContext(),MapsActivity.class);
           startActivity(intent1);
       }
       else if (id == R.id.lagout) {
           FirebaseAuth.getInstance().signOut();
           Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
           startActivity(startIntent);
           finish();
       }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Ads,MainActivity.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Ads, MainActivity.BlogViewHolder>(
                Ads.class,
                R.layout.ads_row,
                MainActivity.BlogViewHolder.class,
                databaseReference)
        {
            @Override
            protected void populateViewHolder(MainActivity.BlogViewHolder viewHolder, Ads model, int position) {

                final String postKey = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(),model.getImage());

               viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),SingleAd.class);
                        intent.putExtra("blogId",postKey);
                        startActivity(intent);
                    }
                });
            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }



    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public  void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.post_tite);
            post_title.setText(title);
        }
        public  void setPrice(String price){
            TextView post_description = (TextView)mView.findViewById(R.id.post_description);
            post_description.setText(price);
        }
        public  void setImage(Context context , String image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(context).load(image).into(post_image);
        }
    }
}
