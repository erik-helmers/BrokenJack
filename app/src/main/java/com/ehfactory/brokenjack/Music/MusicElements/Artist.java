package com.ehfactory.brokenjack.Music.MusicElements;


import com.ehfactory.brokenjack.Music.MusicGetter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Crée par Erik H. (moi) le 25/02/2017 à 22:38.
 * Ce fichier doit probablement etre extremement cool.
 */

public class Artist extends MusicElement {


    private Set<Long> albums_id = new HashSet<>();


    private int nbTracks ;
    private int nbAlbums ;

    public Artist(String name, Long id,  int nbTracks, int nbAlbums) {
        super(name, id);
        this.nbTracks = nbTracks;
        this.nbAlbums = nbAlbums;
    }

    public void add_album(Long id){albums_id.add(id);}

    public ArrayList<Long> getAlbums_id(){return new ArrayList<>(albums_id);}

    public ArrayList<Long> getSongs_id(){
        ArrayList<Long>  output = new ArrayList<>();
        for (Long album_id: albums_id){
           for (Long s : MusicGetter.get(MusicGetter.ALBUM_TYPE, album_id).getSongs_ids())
           {
               if (MusicGetter.get(MusicGetter.SONG_TYPE, s).getArtist_id()==getId())
               {
                   output.add(s);
               }
           }
        }
        return output;
    }


    public int getNbTracks() {
        return nbTracks;
    }

    public int getNbAlbums() {
        return nbAlbums;
    }


    public String getTrackStats(){return getNbTracks()+ " tracks • "+ getNbAlbums()+" albums";}
    public String getFullStats(){return toString()+"\n"+getTrackStats()+"\n"+getAlbums_id();}
}
