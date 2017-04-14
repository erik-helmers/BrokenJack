package com.ehfactory.brokenjack.Music.MusicElements;

import java.util.ArrayList;

/**
 * Crée par Erik H. (moi) le 04/03/2017 à 12:36.
 * Ce fichier doit probablement etre extremement cool.
 */

public class Playlist extends MusicElement {

    private final ArrayList<Long> songs = new ArrayList<>();
    private int count;

    public Playlist(String name, Long id, int count) {
        super(name, id);
        this.count = count;
    }

    public void add_song(Long id, int pos){songs.add(pos, id);}
    public ArrayList<Long> getSongs(){return songs;}
    public int getSize(){return songs.size();}
}
