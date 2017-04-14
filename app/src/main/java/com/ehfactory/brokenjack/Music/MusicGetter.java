package com.ehfactory.brokenjack.Music;

import com.ehfactory.brokenjack.Music.MusicElements.Album;
import com.ehfactory.brokenjack.Music.MusicElements.Artist;
import com.ehfactory.brokenjack.Music.MusicElements.MusicElement;
import com.ehfactory.brokenjack.Music.MusicElements.Playlist;
import com.ehfactory.brokenjack.Music.MusicElements.Song;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Crée par Erik H. (moi) le 03/04/2017 à 13:34.
 * Ce fichier doit probablement etre extremement cool.
 */

public class MusicGetter {

    public static final Class<Song> SONG_TYPE = Song.class;
    public static final Class<Album> ALBUM_TYPE = Album.class;
    public static final Class<Artist> ARTIST_TYPE = Artist.class;
    public static final Class<Playlist> PLAYLIST_TYPE = Playlist.class;



    static HashMap<Long, Song> songs = new HashMap<>();
    static HashMap<Long, Album> albums = new HashMap<>();
    static HashMap<Long, Artist> artists = new HashMap<>();
    static HashMap<Long, Playlist> playlists = new HashMap<>();
    static HashMap<String, Long> artists_link = new HashMap<>();

    private static  <T extends MusicElement>  void check(Class<T> t){
        if (t==SONG_TYPE && MusicLoader.all_songs_init) throw new RuntimeException("Songs not initialized");
        else if (t==ALBUM_TYPE && MusicLoader.all_albums_init) throw new RuntimeException("Albums not initialized");
        else if (t==SONG_TYPE && MusicLoader.all_artists_init) throw new RuntimeException("Artists not initialized");
    }

    private static HashMap<Long, ? extends MusicElement> getHashByType(Class type){
        if (type==SONG_TYPE) return songs;
        else if (type==ALBUM_TYPE) return albums;
        else if (type==ARTIST_TYPE) return artists;
        else if (type==PLAYLIST_TYPE) return playlists;

        throw new IllegalArgumentException("Must be a type");
    }

    public static ArrayList<Song> sort_by_tracks(List<Song> s){

        Collections.sort(s, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return Integer.compare(o1.getAlbum_pos(), o2.getAlbum_pos());
            }
        });

        return (ArrayList<Song>)s;
    }
    public static <T extends MusicElement> ArrayList<T> sort_by_names(ArrayList<T> l){
        final Collator cltor = Collator.getInstance();
        cltor.setStrength(1);
        Collections.sort(l, new Comparator<MusicElement>() {
            @Override
            public int compare(MusicElement o1, MusicElement o2) {
                return cltor.compare(o1.getName(), o2.getName());
            }
        });

        return l;
    }
    public static <T extends MusicElement> ArrayList<Long> sort_by_names(Class<T> type, ArrayList<Long> l){
        ArrayList<T> m = new ArrayList<>();
        for (Long x: l){
            m.add(get(type, x));
        }
        ArrayList<Long> output = new ArrayList<>();
        for (T x: sort_by_names(m)){
            output.add(x.getId());
        }
        return output;
    }

    public static <T extends MusicElement> ArrayList<T> get_all(Class<T> type, ArrayList<Long> ids){
        ArrayList<T> output = new ArrayList<>();
        for (Long id: ids){
            output.add(get(type, id));
        }
        return output;
    }
    public static <T extends MusicElement> T get(Class<T> type, Long id){
        T x = type.cast(getHashByType(type).get(id));
        if (x==null) throw new RuntimeException("not found "+id);
        return x;
    }

    public static <T extends MusicElement> ArrayList<Long> getAllIds(Class<T> type) {

        return new ArrayList<>(getHashByType(type).keySet());
    }
    public static <T extends MusicElement> ArrayList<T> getAllDB(Class<T> type) {
        ArrayList<T> o = new ArrayList<>();
        for (MusicElement x: getHashByType(type).values()){
            o.add(type.cast(x));
        }
        return o;
    }
    public static  <T extends MusicElement> ArrayList<Long> extract_ids(ArrayList<T> l){
        ArrayList<Long> o = new ArrayList<>();
        for (MusicElement i:l){
            o.add(i.getId());
        }
        return o;
    }



}
