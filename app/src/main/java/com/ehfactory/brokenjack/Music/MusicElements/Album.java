package com.ehfactory.brokenjack.Music.MusicElements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Crée par Erik H. (moi) le 25/02/2017 à 21:19.
 * Ce fichier doit probablement etre extremement cool.
 */

public class Album extends MusicElement{

    private Long artist;
    private String art_path;

    private int year;

    public Album(String name, Long id, String art_path, Long artist, int year) {
        super(name, id);
        this.art_path = art_path;
        this.artist = artist;
        this.year = year;
    }

    public String getArt_path() {
        return art_path;
    }
    public Long getArtist_id() {
        return artist;
    }
    public int getYear() {
        return year;
    }

    private Set<Long> songs_ids = new HashSet<>();

    public void add_song(Long id){songs_ids.add(id);} // Is MusicGetter access only
    public ArrayList<Long> getSongs_ids(){return new ArrayList<>(songs_ids);}

    @Override
    public String toString(){
        return super.toString()+" Elements :" + songs_ids;
    }
    public String details(){return super.toString()+" Artist : "+artist+"\n year : "+year+"\n path :"+art_path;}
}
