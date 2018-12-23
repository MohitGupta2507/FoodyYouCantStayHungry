package com.foodies.mohitgupta.foodyyoucantstsyhungry;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserImageChange extends AppCompatActivity {

    android.support.v7.widget.Toolbar  Context;
    Uri uri;
    ImageView image;
    DatabaseReference myref;
    StorageReference mstorage;
    FirebaseUser mAuth;
    String downloadUrl;
    ProgressBar progressBar;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image_change);
        //ActionBar actionBar=getSupportActionBar();
        image=(ImageView)findViewById(R.id.Image);
        mAuth=FirebaseAuth.getInstance().getCurrentUser();
        myref= FirebaseDatabase.getInstance().getReference().child("User_Data").child(mAuth.getUid());
        mstorage= FirebaseStorage.getInstance().getReference().child("ProfileImage").child(mAuth.getUid());
        progressBar=(ProgressBar)findViewById(R.id.progressBar3);
        Context=(android.support.v7.widget.Toolbar )findViewById(R.id.toolbarContext);
        setSupportActionBar(Context);

    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_image, menu);
        if(getParent()!=null)
        {
            return getParent().onCreateOptionsMenu(menu);
        }
        else
        {

            return true;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        myref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                if(dataSnapshot.hasChild("ProfileImage")) {
                    String url = dataSnapshot.child("ProfileImage").getValue().toString();
                    Picasso.with(UserImageChange.this).load(url).fit().centerCrop()
                            .into(image, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError() {

                                }
                            });

                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        image.setImageDrawable(getResources().getDrawable(R.drawable.food2, getApplicationContext().getTheme()));
                    } else {
                        image.setImageDrawable(getResources().getDrawable(R.drawable.food2));
                    }
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });
    }



        @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ChangeProfileImage) {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 0);
            intent.putExtra("aspectY", 0);
            intent.putExtra("return-data", true);
            startActivityForResult(intent,1);
            return true;
        }
        if(id==R.id.Share)
        {
           /* Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Share Via");
            startActivity(intent);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            if (requestCode==1)
            {

                uri=data.getData();
                image.setImageURI(uri);
                MainArea.Profile.setImageURI(uri);
                mstorage.child(uri.getLastPathSegment().toString()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                        downloadUrl=taskSnapshot.getDownloadUrl().toString();
                        myref.child("ProfileImage").setValue(downloadUrl);

                    }
                });


            }
        }
    }


}
