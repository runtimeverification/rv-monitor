package com.example.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MonitoredApp extends Activity {
  private MediaPlayer mplay=null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mplay = MediaPlayer.create(this, R.raw.music);
        mplay.setOnErrorListener(
            new MediaPlayer.OnErrorListener() {
              @Override 
              public boolean onError(MediaPlayer mp, int what, int extra){
                Log.e("Monitor","MediaPlayer.OnErrorListener: error");
                return false;
              }
            });

          //This is the proper way to play music, by setting the 
          //player to run in the OnPreparedListender
          //mplay.setOnPreparedListener(new OnPreparedListener() {
          //     @Override
          //     public void onPrepared(MediaPlayer mp){
          //       mp.start();
          //     }
          //}
          //);

        //here we call start directly, which is possibly dangerous because
        //the media may not be prepared
        mplay.start();
    }
}

