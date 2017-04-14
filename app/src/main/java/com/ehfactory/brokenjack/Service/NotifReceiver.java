package com.ehfactory.brokenjack.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ehfactory.brokenjack.Music.MusicControl;
import com.ehfactory.brokenjack.Service.MusicService;

import static com.ehfactory.brokenjack.Service.MusicService.PAUSE_ACTION;

/**
 * Crée par Erik H. (moi) le 07/04/2017 à 12:12.
 * Ce fichier doit probablement etre extremement cool.
 */

public class NotifReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("Notifreceiver", "onReceive: ");
        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
        if (action.equals(PAUSE_ACTION)){
            MusicControl.pause();
        }
    }
}
