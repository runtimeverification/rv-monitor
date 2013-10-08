package com.example.myapp;
import android.media.MediaPlayer;
import android.util.Log;
import android.media.MediaPlayer.OnPreparedListener;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.lang.ref.*;
import org.aspectj.lang.*;

aspect SetOnPreparedListenerAspect {
  before(Activity a): execution(void Activity+.onCreate(..) ) && this(a) {
    SetOnPreparedListenerRuntimeMonitor.onCreateActivity(a);
  }

  after(): call(void MediaPayer.setOnPreparedListener(..)) {
    SetOnPreparedListenerRuntimeMonitor.setOnPreparedListenerEvent();
  }

  void around(MediaPlayer m): call(void MediaPlayer.start()) && target(m) {
    SetOnPreparedListenerRuntimeMonitor.startEvent(m);
    if(!SetOnPreparedListenerRuntimeMonitor.skipEvent){
      proceed(m);
    }
  }
}

