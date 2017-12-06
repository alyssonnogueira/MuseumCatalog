package com.pyramitec.museumcatalog.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.pyramitec.museumcatalog.Controllers.MasterpieceController;
import com.pyramitec.museumcatalog.Controllers.MuseumController;
import com.pyramitec.museumcatalog.Models.Masterpiece;
import com.pyramitec.museumcatalog.Models.Museum;
import com.pyramitec.museumcatalog.R;
import com.pyramitec.museumcatalog.Views.Museums.MuseumActivity;

import java.util.Date;

/**
 * Created by alyss on 02/12/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private static String TAG = "LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {

                MasterpieceController masterpieceController = new MasterpieceController(getApplicationContext());
                masterpieceController.clearMasterpiece();
                MuseumController museumController = new MuseumController();
                museumController.clearMuseums();

                Masterpiece masterpiece = new Masterpiece("estatua", "bonita", "chuchu", "oneDay", "oneDay", "oneDay", new Date(System.currentTimeMillis()));
                masterpiece = masterpieceController.save(masterpiece);
                Masterpiece masterpiece2 = new Masterpiece("estatua2", "bonita2", "chuchu2", "oneDay", "oneDay", "oneDay", new Date(System.currentTimeMillis()));
                masterpiece2 = masterpieceController.save(masterpiece2);

                Museum museum = new Museum("1", "Museu do Doce", "muito bonito tudo");
                museum = museumController.save(museum);
                //Museum museum2 = new Museum("2", "Museu da Baronesa", "muito bonito tudo");
                //museum2 = museumController.save(museum2);
                museumController.addMasterpieceToMuseumList(museum, masterpiece);
                museumController.addMasterpieceToMuseumList(museum, masterpiece2);

                mostrarLogin();
                /*DatabaseMediator mediator = new DatabaseMediator(SplashScreenActivity.this);
                if (mediator.getUser().size() == 0) {
                    mediator.close();
                    mostrarLogin();
                } else {
                    Log.d(TAG, "Usuario Logado" + mediator.getUser().get(0).getNameUser());
                    Intent intent = new Intent(SplashScreenActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    mediator.close();
                    finish();
                }*/
            }
        }, 2000);
    }

    private void mostrarLogin() {
        Intent intent = new Intent(SplashScreenActivity.this,
                MuseumActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}