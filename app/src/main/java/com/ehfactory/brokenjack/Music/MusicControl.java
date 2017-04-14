package com.ehfactory.brokenjack.Music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ehfactory.brokenjack.Music.MusicElements.Song;


import java.util.ArrayList;

/**
 * Crée par Erik H. (moi) le 05/04/2017 à 14:44.
 * Ce fichier doit probablement etre extremement cool.
 */

public class MusicControl {




    // PlayBack Control
    private static final PlaybackStatus status = new PlaybackStatus();

    public static void resume() {
        if (!status.isPlaying && status.trackInit)
            Log.i("Music Control", "resume");
            Player.startMP();
            status.setIsPlaying(true);
    }
    public static void pause(){
        if (status.isPlaying && status.trackInit){
            Player.pauseMP();
            status.setIsPlaying(false);
        }
    }
    public static void toogle_playing(){
        if (status.isPlaying) pause();
        else resume();
    }

    public static void update_song(){
        Player.setDataSourceMP(status.getSongList().get(status.getActualPos()));
    }

    public static void next_track(){
        if (status.getActualPos()+1 < status.getSongList().size()) {
            status.setActualPos(status.actualPos + 1);
            update_song();
        }

    }
    public static void previous_track(){
        status.setActualPos(status.actualPos-1);
    }

    @Nullable
    public static Song getActualSong(){
        try {return MusicGetter.get(MusicGetter.SONG_TYPE, status.songList.get(status.actualPos));}
        catch (NullPointerException e) {return null;}
    }

    public static boolean isPlaying(){return status.isPlaying();}

    public static void setSongList(ArrayList<Long> songList, int pos) {
        status.setSongList(songList);
        status.setActualPos(pos);
        Player.setDataSourceMP(songList.get(pos));
    }
    public static void setSongList(ArrayList<Long> songList) {
        setSongList(songList, 0);
    }



    public static class MediaListener implements
            MediaPlayer.OnCompletionListener,
            MediaPlayer.OnErrorListener,
            MediaPlayer.OnPreparedListener,
            AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            status.setIsPlaying(false);
            status.setTrackInit(false);
            next_track();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.i("MEDIA PLAYER ERROR WTF", "onError: "+what);
            status.setTrackInit(false);
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            status.setTrackInit(true);
            resume();
        }

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                pause();
            } else if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                pause();
            } else if (focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                pause();
            } else if (focusChange==AudioManager.AUDIOFOCUS_GAIN){
                resume();
            }
        }
    }

    // Stat Listener

    private static final ArrayList<OnStatChangedListener> listeners = new ArrayList<>();


    public static void add_listener(OnStatChangedListener listener){
        listeners.add(listener);
    }
    public static void call_listeners(){
        for (OnStatChangedListener oscl: listeners)
            oscl.onStatChanged(status);
    }

    public static class PlaybackStatus {

        private ArrayList<Long> songList;
        private int actualPos;

        private boolean isPlaying = false;
        private boolean firstSong = true;
        private boolean trackInit = false;

        public void setIsPlaying(boolean isPlaying)     {
            this.isPlaying = isPlaying;
            firstSong = false;
            call_listeners();
        }
        public void setSongList(ArrayList<Long> songList) {
            this.songList = songList;
            firstSong = false;
            call_listeners();
        }
        public void setActualPos(int actualPos) {
            this.actualPos = actualPos;
            trackInit = false;
            call_listeners();
        }
        public void setTrackInit(boolean trackInit) {
            this.trackInit = trackInit;
            firstSong = false;
            call_listeners();
        }

        public boolean isFirstSong() {
            return firstSong;
        }
        public ArrayList<Long> getSongList() {
            return songList;
        }
        public int getActualPos() {
            return actualPos;
        }
        public boolean isPlaying() {
            return isPlaying;
        }
        public boolean isTrackInit() {
            return trackInit;
        }

    }

    public interface OnStatChangedListener {
        void onStatChanged(PlaybackStatus status);
    }

}
