package com.ehfactory.brokenjack.Music.MusicElements;


import com.ehfactory.brokenjack.Music.MusicGetter;

import java.util.Locale;

/**
 * Crée par Erik H. (moi) le 25/02/2017 à 13:55.
 * Ce fichier doit probablement etre extremement cool.
 */

public class Song extends MusicElement{

    private long artist_id;
    private long album_id;

    private int album_pos;
    private int duration;


    public Song(String name, Long thisId, long artist_id, long album_id, int album_pos, int duration) {
        super(name, thisId);
        this.artist_id = artist_id;
        this.album_id = album_id;
        this.album_pos = album_pos;
        this.duration = duration;
    }

    public long getArtist_id() {
        return artist_id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public int getAlbum_pos() {
        return album_pos;
    }

    public int getDuration() {
        return duration;
    }


    public String toString(){
        return String.format(Locale.FRANCE, "%s - %d by %d in %d",
                name, id, artist_id, album_id);
    }
    public String details(){
        return MusicGetter.get(MusicGetter.ARTIST_TYPE, artist_id).getName()+ " • "+MusicGetter.get(MusicGetter.ALBUM_TYPE, album_id).getName();
    }
}
