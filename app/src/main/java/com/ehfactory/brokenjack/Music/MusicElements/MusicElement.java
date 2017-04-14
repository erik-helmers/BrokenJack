package com.ehfactory.brokenjack.Music.MusicElements;

/**
 * Crée par Erik H. (moi) le 25/02/2017 à 21:17.
 * Ce fichier doit probablement etre extremement cool.
 */

public class MusicElement {


    protected final Long id;
    protected final String name;


    MusicElement(String name, Long id) {
        this.name = name;
        this.id = id;
    }



    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name+"\n";
    }

    public Long getId() {
        return id;
    }





}
