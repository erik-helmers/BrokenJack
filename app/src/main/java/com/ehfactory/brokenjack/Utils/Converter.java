package com.ehfactory.brokenjack.Utils;

import java.util.ArrayList;

/**
 * Crée par Erik H. (moi) le 04/04/2017 à 14:07.
 * Ce fichier doit probablement etre extremement cool.
 */

public class Converter {

    public static long[] toPrimitiv(ArrayList<Long> l){
        if (l==null) return new long[0];
        long[] o = new long[l.size()];
        for (int i=0; i<l.size(); i++){
            o[i] = l.get(i);
        }
        return o;
    }

    public static ArrayList<Long> toArray(long[] l){
        ArrayList<Long> o = new ArrayList<>();
        for (Long i: l) o.add(i);
        return o;
    }
}
