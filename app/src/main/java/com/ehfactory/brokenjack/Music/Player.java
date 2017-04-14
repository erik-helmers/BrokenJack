package com.ehfactory.brokenjack.Music;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import com.ehfactory.brokenjack.Service.MusicService;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Crée par Erik H. (moi) le 05/04/2017 à 14:44.
 * Ce fichier doit probablement etre extremement cool.
 */

public class Player{

    final private static MediaPlayer player = new MediaPlayer();

    //Listener
    final private static MusicControl.MediaListener mediaListener = new MusicControl.MediaListener();
    private static AudioManager audioManager;

    // Service Connection
    private static MusicService musicSrv;
    private static Intent playIntent;
    private static boolean musicBound=false;

    public static boolean isOnNotif = false;

    private static MediaObserver observer = null;

    public static void init(Context context){
        initMusicPlayer(context);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private static void initMusicPlayer(Context context){
        player.setWakeMode(context,
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(mediaListener);
        player.setOnCompletionListener(mediaListener);
        player.setOnErrorListener(mediaListener);
    }

    public static void start(Context context){
        if(playIntent==null){
            playIntent = new Intent(context, MusicService.class);
            context.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            context.startService(playIntent);
        }
    }

    public static void destroy(Context context){
        try {
            musicBound = false;
            context.unbindService(musicConnection);
        }
        catch (RuntimeException e){
            Log.e("MusicServiceDestroy", "destroy: ca a pa marché", e);
        }
    }

    private static ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();            //pass list
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public static boolean setDataSourceMP(Long id){
        player.reset();

        Uri trackUri = ContentUris.withAppendedId
                (MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);



        try {
            player.setDataSource(musicSrv.getApplicationContext(), trackUri);
        } catch (Exception e){
            return false;
        }

        player.prepareAsync();
        return true;
    }

    static void startMP(){
        if (!player.isPlaying())
        {
            int result = audioManager.requestAudioFocus(mediaListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                return;
            player.start();
            observer = new MediaObserver();
            new Thread(observer).start();
        }


    }
    static void pauseMP(){
        if (player.isPlaying())
        {
            audioManager.abandonAudioFocus(mediaListener);
            player.pause();
            observer.stop();
        }
    }
    public static void seekToMP(int msec){player.seekTo(msec);}

    public static void onScreenVisible(){
        if (isOnNotif) musicSrv.removeNotif();
    }
    public static void onScreenInvisible(){
        if (MusicControl.isPlaying())
        {
            musicSrv.createNotification();
            isOnNotif = true;
        }
    }



    private static class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);
        public void stop() {
            stop.set(true);
        }

        @Override public void run() {
            while (!stop.get()) {
                try {
                    if (player.isPlaying())
                        sendMsgToUI(player.getCurrentPosition(),
                                player.getDuration());
                } catch (Exception e){e.printStackTrace();}
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    private static void sendMsgToUI(int position, int duration) {
        TrackProgressUpdate event = new TrackProgressUpdate(position, duration);
        EventBus.getDefault().post(event);
    }

    public static class TrackProgressUpdate
    {
        public final int pos;
        public final int max;
        TrackProgressUpdate(int pos, int duration){
            this.pos = pos;
            this.max = duration;
        }
    }


}
