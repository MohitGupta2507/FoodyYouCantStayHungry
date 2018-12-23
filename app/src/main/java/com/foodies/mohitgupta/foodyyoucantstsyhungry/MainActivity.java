package com.foodies.mohitgupta.foodyyoucantstsyhungry;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {


    AdapterImageSlider adapter;
    ViewPager vv;
    FirebaseAuth mAuth;
    EditText phoneNumber;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public String phone;
    private String mVerificationId;
    public static String PhotoUrl2 = null;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String n;
    private PhoneAuthCredential nn;
    private Dialog myDialog;
    private EditText verificationCode;
    private Button Verify;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef2;
    StorageReference mRef;
    private static int MY_PERMISSIONS_REQUEST_Camera=1;

    public MainActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
             Window w=this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        w.setStatusBarColor(this.getResources().getColor(R.color.statusColor));
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        vv=(ViewPager)findViewById(R.id.ViewPager);
        adapter=new AdapterImageSlider(this);
        vv.setAdapter(adapter);
        vv.setBackgroundColor(Color.BLACK);
        CircleIndicator cc=(CircleIndicator)findViewById(R.id.indicator);
        cc.setViewPager(vv);
        myRef = FirebaseDatabase.getInstance().getReference("User_Data");
        myRef2=FirebaseDatabase.getInstance().getReference().child("Mobile_Number");
        mAuth=FirebaseAuth.getInstance();
        phoneNumber=(EditText)findViewById(R.id.editText);
        phoneNumber.getBackground().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_IN);
        phone = phoneNumber.getText().toString();
        database = FirebaseDatabase.getInstance();

        mRef= FirebaseStorage.getInstance().getReference().child("ProfileImage");
    }


    public void buttonClicked(View view ) {

        phone = phoneNumber.getText().toString();
        if (phone.matches("")) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            /*if () {
                signInWithPhoneAuthCredential();
            } else {*/
                myDialog = new Dialog(MainActivity.this);
                myDialog.setContentView(R.layout.verification_resourcefile);
                myDialog.setCancelable(true);
                myDialog.show();
                CreateAUser();
            }
       // }
    }

    //}

    public void CreateAUser() {

        //Button b=(Button)findViewById(R.id.button);
        //b.setEnabled(false);
        phone = phoneNumber.getText().toString();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("Sucess", "onVerificationCompleted:" + credential);
                Toast.makeText(MainActivity.this,"Auto Verification Complete",Toast.LENGTH_LONG).show();
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("Tag",e);
                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();

                // Show a message and update the UI
                // ...
            }

           @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

           }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                MainActivity.this,  // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }


    /***
     * Sign In the User
     *=     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        final boolean[] haschild = {false};
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( @NonNull final Task<AuthResult> task ) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            final String uid = mAuth.getCurrentUser().getUid();
                            final int[] key = {0};
                            DatabaseReference mRef22 = myRef2.child(uid);
                            final int[] finalHasChild = {0};
                            StorageReference mref2;
                            mref2 = mRef.child(uid);
                            Uri uri = Uri.parse("android.resource://com.foodies.mohitgupta.foodyyoucantstsyhungry/drawable/food2");
                            mref2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                                    String PhotoUrl = taskSnapshot.getDownloadUrl().toString();
                                    PhotoUrl2=PhotoUrl.toString();
                                    DatabaseReference r2=myRef.child(uid);
                                    r2.child("ProfileImage").setValue(PhotoUrl);
                                    r2.child("Name").setValue("Username");
                                    r2.child("Phone").setValue(phone);
                                    myRef2.child(uid).child("Key").setValue("1");
                                    myRef2.child(uid).child("Number").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete( @NonNull Task<Void> task ) {
                                            Intent i = new Intent(MainActivity.this, MainArea.class);
                                            startActivity(i);
                                            finish();


                                        }
                                    });


                                }
                            });

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void signinn( View view ) {
        verificationCode = (EditText) myDialog.findViewById(R.id.Verification);
        n = verificationCode.getText().toString();
        if (TextUtils.isEmpty(n)) {
            Toast.makeText(this, "Please Enter the verification Code", Toast.LENGTH_SHORT).show();
        } else {
            Dialog myDialog2 = new Dialog(MainActivity.this);
            myDialog2.setContentView(R.layout.logging_in);
            myDialog2.setCancelable(true);
            myDialog2.show();
            PhoneAuthCredential nn = PhoneAuthProvider.getCredential(mVerificationId, n);
            signInWithPhoneAuthCredential(nn);

        }



    }


    @Override
    protected void onStart() {
        super.onStart();
        MobileDataCheck();
        checkPermissionInternet(this);
        checkPermissionReadStorage(this);
        checkPermissionStorage(this);
        checkPermissionNetwork(this);
        checkPermissionCalender(this);
        if(mAuth.getCurrentUser()!=null)
        {
            Intent MainAreaa=new Intent(MainActivity.this,MainArea.class);
            startActivity(MainAreaa);
            finish();
        }
    }

    public void MobileDataCheck()
    {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }

        if(mobileDataEnabled==false)
        {
            final Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.internet);
            dialog.setCancelable(false);
            Button b=(Button)dialog.findViewById(R.id.buttonn);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

    public static boolean checkPermissionStorage( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean checkPermissionReadStorage( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read Storage permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean checkPermissionInternet( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.INTERNET)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Internet permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick( DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.INTERNET}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean checkPermissionNetwork( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_NETWORK_STATE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Internet permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick( DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean checkPermissionCalender( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_CALENDAR)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Internet permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick( DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_CALENDAR}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_CALENDAR}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
