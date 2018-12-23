package com.foodies.mohitgupta.foodyyoucantstsyhungry;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.iceteck.silicompressorr.SiliCompressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class UploadVideo extends Activity  {

    int request = 1;
    String time;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    public int Like = 0;
    public int Views = 0;
    String Username,ProfileImage;
    DatabaseReference myRef2;
    String date;
    Uri uri;
    ScrollView scroll;
    String uid,path;
    String VideoPath;
    DatabaseReference myRef;
    VideoView mSurfaceView;
    MediaController media;
    FrameLayout frame;
    File inputFile,OutputFile;
    private float y1;
    private float x2;
    private float y2;
    private float x1;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        scroll = (ScrollView) findViewById(R.id.scroll);
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid().toString();
        myRef2 = FirebaseDatabase.getInstance().getReference("User_Data");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                if (dataSnapshot.hasChild(uid)) {
                    Username = dataSnapshot.child(uid).child("Name").getValue().toString();
                    ProfileImage=dataSnapshot.child(uid).child("ProfileImage").getValue().toString();
                } else {
                    Username = "Username";
                  //  ProfileImage=MainActivity.PhotoUrl2.toString();
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });

       // GiraffeCompressor.init(UploadVideo.this);
        mSurfaceView = (VideoView) findViewById(R.id.videoSurface);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        time = timeFormat.format(c);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
        date = dateFormat.format(c);
        myRef = FirebaseDatabase.getInstance().getReference("Video_Upload");
        int width = displayMetrics.widthPixels;
        media = new MediaController(this);
        frame = (FrameLayout) findViewById(R.id.videoSurfaceContainer);
    }

    public void videotext( View v ) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("video/*");
        startActivityForResult(i, request);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == request) {
                uri = data.getData();
                mSurfaceView.setVideoURI(uri);
                //MediaController media1=new MediaController(this);
                media.setAnchorView(mSurfaceView);
                mSurfaceView.setMediaController(media);
                mSurfaceView.start();
                path=getPathFromUri(UploadVideo.this,uri).toString();
                //Toast.makeText(UploadVideo.this,path,Toast.LENGTH_LONG).show();

            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    public Uri getImageUri( Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path.toString());
    }



/*    public  void VideoCompress(File inputFile,File OutputFile)
    {

        GiraffeCompressor.create() //two implementations: mediacodec and ffmpeg,default is mediacodec
                .input(inputFile) //set video to be compressed
                .output(OutputFile) //set compressed video output
                .bitRate(2073600)//set bitrate 码率
                .resizeFactor(Float.parseFloat(String.valueOf(1.0)))//set video resize factor 分辨率缩放,默认保持原分辨率
               // .watermark("/sdcard/videoCompressor/watermarker.png")//add watermark(take a long time) 水印图片(需要长时间处理)
                .ready()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GiraffeCompressor.Result>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(UploadVideo.this,"Compressing",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(UploadVideo.this,e.toString(),Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNext(GiraffeCompressor.Result s) {
                        Toast.makeText(UploadVideo.this,"Compressed",Toast.LENGTH_LONG).show();
                    }
                });

    }*/



      /************/
    //Uploading Data To Firebase

    /***************/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void UploadButtonClicked( View view ) {
        Dialog MyDialog = new Dialog(UploadVideo.this);
        final EditText VideoName = (EditText) findViewById(R.id.VideoName);
        final EditText ShortDesc = (EditText) findViewById(R.id.ShortDesc);
        final EditText LongDesc = (EditText) findViewById(R.id.LongDesc);
        Spinner spinner = (Spinner) findViewById(R.id.Category);
        String Category = spinner.getSelectedItem().toString();
        if ((TextUtils.isEmpty(VideoName.getText().toString())) || (TextUtils.isEmpty(ShortDesc.getText().toString())) || (TextUtils.isEmpty(LongDesc.getText().toString())) || (TextUtils.isEmpty((Category)))) {
            Toast.makeText(UploadVideo.this, "Please Fill All Sections", Toast.LENGTH_SHORT).show();
        } else {
            if (Category.equals("Street")) {
                mSurfaceView.pause();
                MyDialog = new Dialog(UploadVideo.this);
                MyDialog.setContentView(R.layout.uploading_data);
                ProgressBar progressBar=(ProgressBar)MyDialog.findViewById(R.id.progressBar2);
                progressBar.setIndeterminate(true);
                MyDialog.setCancelable(false);
                MyDialog.show();
                final String filepath = uri.getLastPathSegment().toString() + ".mp4";
                final StorageReference SR = storageReference.child("Video_Upload" + "/" + "Street" + "/" + date + "/" + time + "/" + filepath);
                SR.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                        Uri url = taskSnapshot.getDownloadUrl();
                        VideoPath = url.toString();
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(VideoPath, new HashMap<String, String>());
                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000);
                        OutputStream fout = null;

                       /* Uri uri_thumb = getImageUrii(UploadVideo.this, bitmap);
                        File file = new File(uri_thumb.getPath());
                        Bitmap Bitmap2 = null;
                        try {
                            Bitmap2 = new Compressor(UploadVideo.this).compressToBitmap(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        Uri uri_thumb2 = getImageUrii(UploadVideo.this, bitmap);
                        final StorageReference Sr2 = storageReference.child("Video_Upload" + "/" + "Street" + "/" + date + "/" + time + "/" + filepath + "/" + "Video_thumb");
                        Sr2.putFile(uri_thumb2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                                String url_thumb = taskSnapshot.getDownloadUrl().toString();
                                myRef.child("Street").child((date + " " + time).toString()).child("Video_Thumb").setValue(url_thumb);
                                myRef.child("Street").child((date + " " + time).toString()).child("UserName").setValue(Username);
                                myRef.child("Street").child((date + " " + time).toString()).child("ProfileImage").setValue(ProfileImage);
                                myRef.child("Street").child((date + " " + time).toString()).child("VideoName").setValue(VideoName.getText().toString());
                                myRef.child("Street").child((date + " " + time).toString()).child("Short_Desc").setValue(ShortDesc.getText().toString());
                                myRef.child("Street").child((date + " " + time).toString()).child("Long_Desc").setValue(LongDesc.getText().toString());
                                myRef.child("Street").child((date + " " + time).toString()).child("Likes").setValue(Like);
                                myRef.child("Street").child((date + " " + time).toString()).child("Views").setValue(Views);
                                myRef.child("Street").child((date + " " + time).toString()).child("DateTime").setValue(date + " " + time);
                                myRef.child("Street").child((date + " " + time).toString()).child("VideoPath").setValue(VideoPath).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete( @NonNull Task<Void> task ) {
                                        if (task.isComplete()) {
                                            myRef.child("Street").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded( DataSnapshot dataSnapshot, String s ) {
                                                    PendingIntent pi = PendingIntent.getActivity(UploadVideo.this, 0, new Intent(UploadVideo.this, MainArea.class), 0);
                                                    Notification notification = new NotificationCompat.Builder(UploadVideo.this)
                                                            .setSmallIcon(R.mipmap.foody_icon)
                                                            .setContentTitle("New Video Added to Street Section")
                                                            .setContentIntent(pi)
                                                            .setAutoCancel(true)
                                                            .build();

                                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    notificationManager.notify(0, notification);

                                                }

                                                @Override
                                                public void onChildChanged( DataSnapshot dataSnapshot, String s ) {

                                                }

                                                @Override
                                                public void onChildRemoved( DataSnapshot dataSnapshot ) {

                                                }

                                                @Override
                                                public void onChildMoved( DataSnapshot dataSnapshot, String s ) {

                                                }

                                                @Override
                                                public void onCancelled( DatabaseError databaseError ) {

                                                }
                                            });
                                            Intent i = new Intent(UploadVideo.this, MainArea.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(UploadVideo.this, "Post Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                        Toast.makeText(UploadVideo.this, "Video Added", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Category.equals("Kitchen")) {
                mSurfaceView.pause();
                MyDialog.setContentView(R.layout.uploading_data);
                MyDialog.setCancelable(false);
                MyDialog.show();
                final String filepath = uri.getLastPathSegment().toString() + ".mp4";
                StorageReference SR = storageReference.child("Video_Upload" + "/" + "Kitchen" + "/" + date + "/" + time + "/" + filepath);
                SR.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                        Uri Video = taskSnapshot.getDownloadUrl();
                        VideoPath = Video.toString();
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(VideoPath, new HashMap<String, String>());
                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000);
                        OutputStream fout = null;
                       /* Uri uri_thumb = getImageUrii(UploadVideo.this, bitmap);
                        File file = new File(uri_thumb.getPath());
                        Bitmap Bitmap2 = null;
                        try {
                            Bitmap2 = new Compressor(UploadVideo.this).compressToBitmap(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        Uri uri_thumb2 = getImageUrii(UploadVideo.this, bitmap);
                        final StorageReference Sr2 = storageReference.child("Video_Upload" + "/" + "Street" + "/" + date + "/" + time + "/" + filepath + "/" + "Video_thumb");
                        Sr2.putFile(uri_thumb2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                              String uri_thumb22=taskSnapshot.getDownloadUrl().toString();
                                Toast.makeText(UploadVideo.this, "Video Uploaded Success Fully", Toast.LENGTH_SHORT).show();
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("Video_Thumb").setValue(uri_thumb22);
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("UserName").setValue(Username);
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("ProfileImage").setValue(ProfileImage);
                                //myRef.child("Kitchen").child((date+" "+time).toString()).child("UserName").setValue(Username);
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("VideoName").setValue(VideoName.getText().toString());
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("Short_Desc").setValue(ShortDesc.getText().toString());
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("Long_Desc").setValue(LongDesc.getText().toString());
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("Likes").setValue(Like);
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("Views").setValue(Views);
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("DateTime").setValue(date+" "+time);
                                myRef.child("Kitchen").child((date+" "+time).toString()).child("VideoPath").setValue(VideoPath).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete( @NonNull Task<Void> task ) {
                                        myRef.child("Kitchen").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded( DataSnapshot dataSnapshot, String s ) {
                                                PendingIntent pi=PendingIntent.getActivity(UploadVideo.this,0,new Intent(UploadVideo.this,MainArea.class),0);
                                                Notification notification=new NotificationCompat.Builder(UploadVideo.this)
                                                        .setSmallIcon(R.drawable.foody_icon)
                                                        .setContentTitle("New Video Added to Kitchen Section")
                                                        .setContentIntent(pi)
                                                        .setAutoCancel(true)
                                                        .build();

                                                NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                notificationManager.notify(0,notification);

                                            }

                                            @Override
                                            public void onChildChanged( DataSnapshot dataSnapshot, String s ) {

                                            }

                                            @Override
                                            public void onChildRemoved( DataSnapshot dataSnapshot ) {

                                            }

                                            @Override
                                            public void onChildMoved( DataSnapshot dataSnapshot, String s ) {

                                            }

                                            @Override
                                            public void onCancelled( DatabaseError databaseError ) {

                                            }
                                        });
                                        Intent i = new Intent(UploadVideo.this, MainArea.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });

                            }
                        });


                    }
                });

            } else {
                MyDialog.cancel();
                Toast.makeText(UploadVideo.this, "Please Select a Category", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Uri getImageUrii(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}


