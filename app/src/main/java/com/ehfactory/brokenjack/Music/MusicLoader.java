package com.ehfactory.brokenjack.Music;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.ehfactory.brokenjack.Music.MusicElements.Album;
import com.ehfactory.brokenjack.Music.MusicElements.Artist;
import com.ehfactory.brokenjack.Music.MusicElements.Song;

import java.util.ArrayList;

import static com.ehfactory.brokenjack.Music.MusicGetter.ALBUM_TYPE;
import static com.ehfactory.brokenjack.Music.MusicGetter.ARTIST_TYPE;
import static com.ehfactory.brokenjack.Music.MusicGetter.SONG_TYPE;
import static com.ehfactory.brokenjack.Music.MusicGetter.albums;
import static com.ehfactory.brokenjack.Music.MusicGetter.artists;
import static com.ehfactory.brokenjack.Music.MusicGetter.artists_link;
import static com.ehfactory.brokenjack.Music.MusicGetter.songs;

/**
 * Crée par Erik H. (moi) le 03/04/2017 à 14:23.
 * Ce fichier doit probablement etre extremement cool.
 */

public class MusicLoader {

    public static boolean all_songs_init;
    public static boolean all_artists_init;
    public static boolean all_albums_init;
    public static boolean all_playlists_init;

    public static ArrayList<Long> init_artist = new ArrayList<>();
    public static ArrayList<Long> init_albums = new ArrayList<>();




    private static int ARTIST_ID_COLUMN;

    public static void initialize_all_songs(ContentResolver musicResolver) {

        if (!all_albums_init) initialize_all_albums(musicResolver);

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        initialize_songs_with_cursor(musicCursor);

        all_songs_init = true;
    }
    private static void initialize_songs_with_cursor(Cursor musicCursor){

        if (all_songs_init) return;

        Log.i("MusicLoader\n", "=======================================initialize_songs: ========================================");
        if (musicCursor != null && musicCursor.moveToFirst()) {


            //get columns

            //=========IDS==============
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistIdColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST_ID);
            int albumIdColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ALBUM_ID);

            //==============KEYS================

            int keyColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE_KEY);
            int artistKeyColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST_KEY);

            //============TRACK RELATIVE ==============

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            int trackColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TRACK);

            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);

            //add songs to list
            do {

                //===============IDS===============
                long thisId = musicCursor.getLong(idColumn);
                long thisArtist_id = musicCursor.getLong(artistIdColumn);
                long thisAlbum_id = musicCursor.getLong(albumIdColumn);


                //============TRACK RELATIVE============
                String thisTitle = musicCursor.getString(titleColumn);
                int thisDuration = musicCursor.getInt(durationColumn);
                int thisTrack = musicCursor.getInt(trackColumn);


                if (!albums.containsKey(thisAlbum_id))
                {
                    Log.i("MusicLoaderException", "Did not found album id : "+thisAlbum_id+" and name : "+musicCursor.getString(albumColumn));

                    albums.put(thisAlbum_id, new Album // Case the album is not found
                        (musicCursor.getString(albumColumn), thisAlbum_id, null, thisArtist_id, 0));
                    artists.get(thisArtist_id).add_album(thisAlbum_id); // We add it to the artist
                }

                albums.get(thisAlbum_id).add_song(thisId);
                songs.put(thisId, new Song(thisTitle, thisId, thisArtist_id, thisAlbum_id, thisTrack, thisDuration));
                Log.i("MusicLoader", "initialize_songs_with_cursor: "+songs.get(thisId));

            } while (musicCursor.moveToNext());
            musicCursor.close();
        }
        Log.i("MusicLoader\n", "======================================= END ========================================");
    }

    public static void initialize_all_albums(ContentResolver contentResolver) {

        if (all_albums_init) return;

        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(albumUri, null, null, null, null);

        initialize_albums_with_cursor(cursor);

        all_albums_init = true;
    }
    public static void initialize_by_albums(ContentResolver contentResolver, Long id){
        if (all_songs_init || init_albums.contains(id)){
            return;
        }

        Album album = albums.get(id);

        String selectionA = MediaStore.Audio.Media.ALBUM_ID + "=?";
        String[] selectionArgsA= {String.valueOf(id)};

        Cursor songsCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selectionA, selectionArgsA, null);
        initialize_songs_with_cursor(songsCursor);

        init_albums.add(id);

    }
    private static void initialize_albums_with_cursor(Cursor cursor){
        if (cursor != null && cursor.moveToFirst()) {

            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int nameColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int yearColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR);
            int artColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            do {
                Long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int year = cursor.getInt(yearColumn);
                String art = cursor.getString(artColumn);
                String artist = cursor.getString(artistColumn);

                albums.put(id, new Album(name, id, art, artists_link.get(artist), year));
                artists.get(artists_link.get(artist)).add_album(id);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }


    public static void initialize_all_artists(ContentResolver musicResolver) {

        if (all_artists_init) return;

        Log.i("MusicLoader", " ================================= initialize_artist:  ==============================");
        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor artistCursor = musicResolver.query(artistUri, null, null, null, null);

        if (artistCursor != null && artistCursor.moveToFirst()) {

            int artistColumn = artistCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            int idColumn = artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            ARTIST_ID_COLUMN = idColumn;
            int numberOfAlbumsColumn = artistCursor.getColumnIndex
                    (MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS);
            int numberOfTracksColumn = artistCursor.getColumnIndex
                    (MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS);

            do {
                String artist = artistCursor.getString(artistColumn);
                Long artistId = artistCursor.getLong(idColumn);
                int nbAbm = artistCursor.getInt(numberOfAlbumsColumn);
                int nbTrcks = artistCursor.getInt(numberOfTracksColumn);

                artists.put(artistId, new Artist(artist, artistId, nbTrcks, nbAbm));
                artists_link.put(artist, artistId);

            } while (artistCursor.moveToNext());
            artistCursor.close();
        }

        all_artists_init = true;
        Log.i("MusicLoader", " ================================= END ==============================");
    }
    public static void initialize_by_artist(ContentResolver contentResolver, Long id) {

        if (all_songs_init || init_artist.contains(id)){
            return;
        }

        Artist artist = artists.get(id);

        if (!all_albums_init) {
            String selectionA = MediaStore.Audio.Albums.ARTIST + "=?";
            String[] selectionArgsA = {artist.getName()};
            Cursor albumCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, selectionA, selectionArgsA, null);
            initialize_albums_with_cursor(albumCursor);
        }


        String selectionB = MediaStore.Audio.Media.ARTIST_ID + "=?";
        String[] selectionArgsB = {String.valueOf(id)};
        Cursor songsCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selectionB, selectionArgsB, null);
        initialize_songs_with_cursor(songsCursor);

        init_artist.add(id);

    }
    public static void initialize_by_type(Class type, ContentResolver ctntRslvr){
        if (type==ARTIST_TYPE) initialize_all_artists(ctntRslvr);
        else if (type==ALBUM_TYPE) initialize_all_albums(ctntRslvr);
        else if (type==SONG_TYPE) initialize_all_songs(ctntRslvr);
    }
}
