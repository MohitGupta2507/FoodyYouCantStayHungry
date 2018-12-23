package com.foodies.mohitgupta.foodyyoucantstsyhungry;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class KitchenVideoView extends AppCompatActivity {

    private String pos,pos2;
    int a,b;
    DatabaseReference myRef1,myRef2;
    VideoView video;
    TextView Title,LongDesc2,Views,Likes,StartVideo,TotalVideo;
    MediaController m;
    ImageView play;
    boolean isPlaying=false;
    ProgressBar pvideo,pmain;
    int current=0;
    int duration=0;
    public ViewProgresss vvv;



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_video_view);
        StartVideo = (TextView) findViewById(R.id.Startt);
        TotalVideo = (TextView) findViewById(R.id.VideoTimee);
        video = (VideoView) findViewById(R.id.Video22);
        Title = (TextView) findViewById(R.id.Title22);
        LongDesc2 = (TextView) findViewById(R.id.LongDesc22);
        Views = (TextView) findViewById(R.id.Views22);
      //  Likes = (TextView) findViewById(R.id.Likes22);
        pmain = (ProgressBar) findViewById(R.id.bufferingg);
        pvideo = (ProgressBar) findViewById(R.id.progressVideoo);
        play = (ImageView) findViewById(R.id.Play_Pausee);
        pos = getIntent().getStringExtra("position").toString();
        pos2 = pos.substring(pos.lastIndexOf("/") + 1).toString();
        pvideo.setMax(100);
        myRef1 = FirebaseDatabase.getInstance().getReference().child("Video_Upload").child("Kitchen");
        Toast.makeText(KitchenVideoView.this, pos2, Toast.LENGTH_LONG).show();
        vvv=new ViewProgresss();
        myRef2 = myRef1.child(pos2).child("Views");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                String value;
                value = dataSnapshot.getValue().toString();
                a = Integer.valueOf(value);
                b = a + 1;
                Integer a = Integer.valueOf(b);
                myRef2.setValue(a);


            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });

        myRef1.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                pmain = (ProgressBar) findViewById(R.id.bufferingg);
                Title.setText(dataSnapshot.child(pos2).child("VideoName").getValue().toString());
                LongDesc2.setText(dataSnapshot.child(pos2).child("Long_Desc").getValue().toString());
                Views.setText(dataSnapshot.child(pos2).child("Views").getValue().toString());
//                Likes.setText(dataSnapshot.child(pos2).child("Likes").getValue().toString());
                String path = dataSnapshot.child(pos2).child("VideoPath").getValue().toString();
                playy p=new playy();
                p.execute(path);

            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });
    }


    class playy extends AsyncTask<String,Void,Uri>
    {

        @Override
        protected Uri doInBackground(String... strings) {
            Uri u=Uri.parse(strings[0]);
            return u;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            vvv=new ViewProgresss();
            video.setVideoURI(uri);
            video.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if (what == mp.MEDIA_INFO_VIDEO_RENDERING_START) {
                        pmain.setVisibility(View.GONE);
                        return true;
                   } else if (what == mp.MEDIA_INFO_BUFFERING_START) {
                        pmain.setVisibility(View.VISIBLE);
                        return true;
                    } else if (what == mp.MEDIA_INFO_BUFFERING_END) {
                        pmain.setVisibility(View.INVISIBLE);
                        return true;
                    }
                    return false;
                }
            });
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    video.requestFocus();
                    video.start();
                    isPlaying = true;
                    play.setImageResource(R.drawable.ic_media_play);
                    vvv.execute();
                    duration = mp.getDuration() / 1000;
                    String durationString = String.format("%02d:%02d", duration / 60, duration % 60);
                    TotalVideo.setText(durationString);
                }
            });

        }
    }





    public void PlayClicked(View view)
    {
        if(isPlaying==true)
        {
            video.pause();
            isPlaying=false;
            play.setImageResource(R.drawable.ic_media_pause);
        }
        else if(isPlaying==false)
        {
            video.start();
            isPlaying=true;
            play.setImageResource(R.drawable.ic_media_play);
        }
    }


    public class ViewProgresss extends AsyncTask<Void,Integer,Void>
    {

        @Override
        protected Void doInBackground( Void... voids ) {

            do{
                if(isCancelled()){
                    break;}
                    current = video.getCurrentPosition() / 1000;
                    try {
                      publishProgress(current);
                    } catch (Exception e) {
                    }

            }while(pvideo.getProgress()<=100);
            return null;

        }

        @Override
        protected void onProgressUpdate( Integer... values ) {
            super.onProgressUpdate(values);

            try{
                int currentPro=current* 100/duration;
                pvideo.setProgress(currentPro);
                String CurrentTime=String.format("%02d:%02d",values[0]/60,values[0]%60);
                StartVideo.setText(CurrentTime);
            }catch (Exception e)
            {

            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        vvv.cancel(true);
        video.stopPlayback();
        isPlaying=false;
        }

/*    @Override
    protected void onStop() {
        super.onStop();
        video.pause();
        isPlaying=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        video.start();
        isPlaying=true;
    }*/
}