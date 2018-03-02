package com.pyramitec.museumcatalog.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pyramitec.museumcatalog.Controllers.MuseumController;
import com.pyramitec.museumcatalog.Models.Masterpiece;
import com.pyramitec.museumcatalog.Models.Museum;
import com.pyramitec.museumcatalog.R;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        getSupportActionBar().hide(); // tirar toolbar

        Intent intent = getIntent();
        final long museumId = intent.getLongExtra("museumId", 0);
        Toast.makeText(getApplicationContext(), "idMuseum" + String.valueOf(museumId), Toast.LENGTH_LONG).show();
        final int masterpieceId = intent.getIntExtra("masterpieceId", 0);
        MuseumController museumController = new MuseumController();
        Museum museum = museumController.getMuseumById(museumId);
        Toast.makeText(getApplicationContext(), "idItem" + String.valueOf(masterpieceId), Toast.LENGTH_LONG).show();
        Masterpiece masterpiece = museum.getMasterpieceFromList(masterpieceId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(masterpiece.getName());
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        String text = "";

        if(masterpiece.getDescription() != null)
            text = text + "Descricao: " + masterpiece.getDescription() + "\n\n";
        if(masterpiece.getYear()!= null)
            text = text + "Ano de nascimento: " + masterpiece.getYear() + "\n\n";
        if(masterpiece.getAuthor()!= null)
            text = text + "Autor: " + masterpiece.getAuthor() + "\n\n";
        if(masterpiece.getStatus()!= null)
            text = text + "Status: " + masterpiece.getStatus() + "\n\n";
        if(masterpiece.getConservation_state()!= null)
            text = text + "Descricao: " + masterpiece.getConservation_state() + "\n\n";

        TextView textView = (TextView) findViewById(R.id.content);
        textView.setText(text);

        ImageButton buttonBack = (ImageButton) findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
