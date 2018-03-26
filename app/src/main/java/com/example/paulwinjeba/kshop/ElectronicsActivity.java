package com.example.paulwinjeba.kshop;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ElectronicsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView electronics_post;
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private int key=0;
    Button login,signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronics);

        //Change the database tree name :child
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post");
        mAuth = FirebaseAuth.getInstance();

        electronics_post = (RecyclerView) findViewById(R.id.electronics_view);
        electronics_post.setHasFixedSize(true);
        electronics_post.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Query electronics = databaseReference.orderByChild("Category").equalTo("Electronics");
        FirebaseRecyclerAdapter<Blog, ElectronicsActivity.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, ElectronicsActivity.BlogViewHolder>(
                Blog.class,
                R.layout.post_row,
                ElectronicsActivity.BlogViewHolder.class,
                electronics
        ) {
            @Override
            protected void populateViewHolder(ElectronicsActivity.BlogViewHolder viewHolder, Blog model, int position) {

                final String post_key = getRef(position).getKey().toString();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleBlogIntent = new Intent(ElectronicsActivity.this,BlogSingleActivity.class);
                        singleBlogIntent.putExtra("blog_id",post_key);
                        startActivity(singleBlogIntent);
                    }
                });
            }
        };

        electronics_post.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String Title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(Title);
        }

        public void setPrice(String Price){
            TextView post_price = (TextView) mView.findViewById(R.id.post_price);
            post_price.setText(Price);
        }

        public void setImage(Context ctx, String Image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx.getApplicationContext()).load(Image).into(post_image);
        }
    }


    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.home, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
            return true;
        }
        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if(id == R.id.searches){
                final Intent search = new Intent(ElectronicsActivity.this, SearchActivity.class);
                startActivity(search);

            } else
            if (id == R.id.electronics) {
                // Handle the electronics action
                final Intent electronic = new Intent(ElectronicsActivity.this,ElectronicsActivity.class);
                startActivity(electronic);

            } else if (id == R.id.clothes) {
                final Intent electronic = new Intent(ElectronicsActivity.this,ClothesActivity.class);
                startActivity(electronic);

            } else if (id == R.id.bike) {
                final Intent electronic = new Intent(ElectronicsActivity.this,BikesActivity.class);
                startActivity(electronic);

            } else if (id == R.id.book) {
                final Intent electronic = new Intent(ElectronicsActivity.this,BooksActivity.class);
                startActivity(electronic);

            } else if(id == R.id.miscellaneous){
                final Intent electronic = new Intent(this,MiscellaneousActivity.class);
                startActivity(electronic);

            }else if (id == R.id.donation){
                final Intent electronic = new Intent(ElectronicsActivity.this, DonationActivity.class);
                startActivity(electronic);
            } else if (id == R.id.myprofile) {
                final Intent upload = new Intent(ElectronicsActivity.this,MyProfileActivity.class);
                startActivity(upload);

            } else if(id == R.id.mypost){
                final Intent upload = new Intent(ElectronicsActivity.this,MyPostActivity.class);
                startActivity(upload);

            } else if (id == R.id.upload) {
                if (mAuth.getCurrentUser() != null) {
                    // User is logged in
                    final Intent upload = new Intent(ElectronicsActivity.this,PostActivity.class);
                    startActivity(upload);
                }
                else
                {
                    try {
                        LayoutInflater inflater = getLayoutInflater();
                        View alertLayout;
                        alertLayout = inflater.inflate(R.layout.activity_inflater, null);
                        AlertDialog.Builder alert = new AlertDialog.Builder(ElectronicsActivity.this);
                        //Set the button id
                        login = (Button) alertLayout.findViewById(R.id.log_in);
                        signin = (Button) alertLayout.findViewById(R.id.sign_in);
                        alert.setTitle("Login or Signin");
                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);

                        login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent nxtlogin = new Intent(ElectronicsActivity.this, PostLoginActivity.class);
                                key = 0;
                                nxtlogin.putExtra("type_key",key);
                                startActivity(nxtlogin);
                            }
                        });

                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Intent nxtsignin = new Intent(ElectronicsActivity.this, PostSigninActivity.class);
                                key = 0;
                                nxtsignin.putExtra("type_key",key);
                                startActivity(nxtsignin);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                            /*final Intent cancel = new Intent(HomeActivity.this, HomeActivity.class);
                            startActivity(cancel);*/

                            }
                        });
                        AlertDialog dialog = alert.create();
                        dialog.show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            } else if (id == R.id.gift){
                if (mAuth.getCurrentUser() != null) {
                    // User is logged in
                    final Intent upload = new Intent(ElectronicsActivity.this, DonateActivity.class);
                    startActivity(upload);
                } else {
                    try {
                        LayoutInflater inflater = getLayoutInflater();
                        View alertLayout;
                        alertLayout = inflater.inflate(R.layout.activity_inflater, null);
                        AlertDialog.Builder alert = new AlertDialog.Builder(ElectronicsActivity.this);
                        //Set the button id
                        login = (Button) alertLayout.findViewById(R.id.log_in);
                        signin = (Button) alertLayout.findViewById(R.id.sign_in);
                        alert.setTitle("Login or Signin");
                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);

                        login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent nxtlogin = new Intent(ElectronicsActivity.this, PostLoginActivity.class);
                                key=1;
                                nxtlogin.putExtra("type_key",key);
                                startActivity(nxtlogin);
                            }
                        });

                        signin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Log.d("sign in checking", "tested");
                                final Intent nxtsignin = new Intent(ElectronicsActivity.this, PostSigninActivity.class);
                                key=1;
                                nxtsignin.putExtra("type_key",key);
                                startActivity(nxtsignin);
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                            /*final Intent cancel = new Intent(HomeActivity.this, HomeActivity.class);
                            startActivity(cancel);*/

                            }
                        });
                        AlertDialog dialog = alert.create();
                        dialog.show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }else if (id == R.id.logout) {

                //End user session
                if(mAuth.getCurrentUser() != null){
                    //End users session
                    FirebaseAuth.getInstance().signOut();
                    Intent homeagain = new Intent(ElectronicsActivity.this, FirstpageActivity.class);
                    homeagain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(ElectronicsActivity.this,"Logged Out Successfully",Toast.LENGTH_LONG).show();
                    startActivity(homeagain);
                }
                else
                    Toast.makeText(ElectronicsActivity.this,"Log in to Log out !",Toast.LENGTH_LONG).show();


            } else if (id == R.id.about){
                final Intent about = new Intent(ElectronicsActivity.this, AboutActivity.class);
                startActivity(about);
            }else if (id == R.id.requirement){
                Intent requ = new Intent(ElectronicsActivity.this, ViewRequestsActivity.class);
                startActivity(requ);

            } else if (id == R.id.request){
                if (mAuth.getCurrentUser() != null) {
                    Intent request = new Intent(ElectronicsActivity.this, RequestActivity.class);
                    startActivity(request);
                }
                else
                    Toast.makeText(ElectronicsActivity.this, "Log in to request a requirement... !", Toast.LENGTH_LONG).show();
            } else if (id == R.id.termsncond){
                Intent tnc = new Intent(ElectronicsActivity.this, TermsAndConditionsActivity.class);
                startActivity(tnc);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
}
