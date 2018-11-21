package com.example.mustafa.petcarefyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogActivity extends AppCompatActivity {

    RecyclerView mBlogList;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    DatabaseReference databaseReferenceUSers;
    LinearLayoutManager mLayoutManager;
    TextView author;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloglist);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");
        databaseReferenceUSers = FirebaseDatabase.getInstance().getReference().child("Users");

        author=(TextView)findViewById(R.id.textView2);

        databaseReferenceUSers.keepSynced(true);
        databaseReference.keepSynced(true);
        mBlogList = (RecyclerView)findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
        mLayoutManager = new LinearLayoutManager(BlogActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mBlogList.setLayoutManager(mLayoutManager);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add){
            Intent intent = new Intent(getApplicationContext(),PostActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                databaseReference)
        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                final String postKey = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(),model.getImage());

               viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Intent intent = new Intent(getApplicationContext(),BlogSingleActivity.class);
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
        public  void setDesc(String desc){
            TextView post_description = (TextView)mView.findViewById(R.id.post_description);
            post_description.setText(desc);
        }

        public  void setImage(Context context ,String image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(context).load(image).into(post_image);
        }
    }

}
