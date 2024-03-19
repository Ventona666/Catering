package com.example.catering.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.catering.Fragments.HomeFragment;
import com.example.catering.R;
import com.example.catering.Services.UtilsService;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private UtilsService utilsService = new UtilsService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            utilsService.replaceFragment(getSupportFragmentManager(), new HomeFragment());
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        clearCache();
        Log.d("Clear Cache", "Le cache a été vidé");
    }

    private void clearCache() {
        File cacheDir = getCacheDir();
        if (cacheDir.isDirectory()) {
            String[] files = cacheDir.list();
            if (files != null) {
                for (String file : files) {
                    File currentFile = new File(cacheDir, file);
                    currentFile.delete();
                }
            }
        }
    }

}