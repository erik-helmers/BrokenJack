package com.ehfactory.brokenjack.Activitys.Intro;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ehfactory.brokenjack.MainActivity;
import com.ehfactory.brokenjack.R;

import java.security.Permission;
import java.security.Permissions;

public class Tutorial extends AppCompatActivity implements View.OnClickListener {

    public static final int MY_PERMISSION_REQUEST_READ_CONTACTS = 666;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.next_button);
        fab.setOnClickListener(this);
    }


    @Override
    @TargetApi(23)
    public void onClick(View v) {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                MY_PERMISSION_REQUEST_READ_CONTACTS);
    }

    public void onBackPressed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(this, MainActivity.class));
                    }
                }
            }
    }

}
