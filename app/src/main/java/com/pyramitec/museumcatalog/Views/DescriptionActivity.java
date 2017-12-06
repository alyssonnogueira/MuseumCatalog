package com.pyramitec.museumcatalog.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.pyramitec.museumcatalog.Controllers.MuseumController;
import com.pyramitec.museumcatalog.Models.Masterpiece;
import com.pyramitec.museumcatalog.Models.Museum;
import com.pyramitec.museumcatalog.R;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();
        final long museumId = intent.getLongExtra("museumId", 0);
        final int masterpieceId = intent.getIntExtra("masterpieceId", 0);
        MuseumController museumController = new MuseumController();
        Museum museum = museumController.getMuseumById(museumId);
        Masterpiece masterpiece = museum.getMasterpieceFromList(masterpieceId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(masterpiece.getName());
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView) findViewById(R.id.content);
        textView.setText(masterpiece.getDescription() + masterpiece.getYear() + masterpiece.getAuthor());
    }
}
