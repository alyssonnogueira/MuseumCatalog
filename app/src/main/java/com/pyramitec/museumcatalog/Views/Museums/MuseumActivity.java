package com.pyramitec.museumcatalog.Views.Museums;

import   android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.pyramitec.museumcatalog.R;

public class MuseumActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_museum);
        mToolbar.setTitle("Escolha o Museu");
        //setSupportActionBar(mToolbar);

        // FRAGMENT
        MuseumFragment frag = (MuseumFragment) getSupportFragmentManager().findFragmentByTag("museumFrag");
        if (frag == null) {
            frag = new MuseumFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, "museumFrag");
            ft.commit();
        }
    }
}
