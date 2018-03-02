package com.pyramitec.museumcatalog.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pyramitec.museumcatalog.Controllers.MasterpieceController;
import com.pyramitec.museumcatalog.Controllers.MuseumController;
import com.pyramitec.museumcatalog.Models.Masterpiece;
import com.pyramitec.museumcatalog.Models.Museum;
import com.pyramitec.museumcatalog.R;
import com.pyramitec.museumcatalog.Views.Museums.MuseumActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

                Masterpiece masterpiece = new Masterpiece("estatua", "bonita", "chuchu", "oneDay", "oneDay", "oneDay", "data");
                masterpiece = masterpieceController.save(masterpiece);
                Masterpiece masterpiece2 = new Masterpiece("estatua2", "bonita2", "chuchu2", "oneDay", "oneDay", "oneDay", "data");
                masterpiece2 = masterpieceController.save(masterpiece2);

                Museum museum = new Museum("1", "Museu do Doce", "muito bonito tudo");
                museum = museumController.save(museum);
                //Museum museum2 = new Museum("2", "Museu da Baronesa", "muito bonito tudo");
                //museum2 = museumController.save(museum2);
                museumController.addMasterpieceToMuseumList(museum, masterpiece);
                museumController.addMasterpieceToMuseumList(museum, masterpiece2);

                String museumData = readerFromFile(R.raw.spartan_museums_1519619254361);
                String collectionData = readerFromFile(R.raw.spartan_collections_1519619257699);
                String itemsData = readerFromFile(R.raw.spartan_items_1519619261430);

                ArrayList<Museum> museumList= new ArrayList<>();
                //Log.d("Teste", jsonString);
                try {
                    JSONArray museumJA = new JSONArray(museumData);
                    for (int i = 0; i < museumJA.length(); i++)
                    {
                        String id = museumJA.getJSONObject(i).getString("id");
                        String name = museumJA.getJSONObject(i).getString("name");
                        String address = museumJA.getJSONObject(i).getString("address");
                        String phone = museumJA.getJSONObject(i).getString("phone");
                        String hours = museumJA.getJSONObject(i).getString("opening_hours");
                        String desc = museumJA.getJSONObject(i).getString("description");
                        String logo = "Logo";
                        Museum newMuseum = new Museum(i, id, name, logo, desc, address, phone, hours);

                        museumList.add(museumController.save(newMuseum));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Teste", "erro3");
                }

                ArrayList<Integer> collec_mus = new ArrayList<>();

                try {
                    JSONArray collectionJA = new JSONArray(collectionData);

                    for (int i = 0; i < collectionJA.length(); i++)
                    {
                        //id sempre começam em 1
                        collec_mus.add(collectionJA.getJSONObject(i).getInt("museum_id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Teste", "erro3");
                }

                Masterpiece item = new Masterpiece();
                try {
                    JSONArray itemsJA = new JSONArray(itemsData);
                    for (int i = 0; i < itemsData.length(); i++)
                    {
                        int id = itemsJA.getJSONObject(i).getInt("id");
                        String name = itemsJA.getJSONObject(i).getString("name");
                        int collec_id = itemsJA.getJSONObject(i).getInt("collection_id");
                        String year = itemsJA.getJSONObject(i).getString("year");
                        String status = itemsJA.getJSONObject(i).getString("status");
                        String cons_state = itemsJA.getJSONObject(i).getString("conservation_state");
                        String biography = itemsJA.getJSONObject(i).getString("biography");
                        String desc = itemsJA.getJSONObject(i).getString("description");

                        String image = "image";
                        String video = "video";
                        String audio = "audio";

                        Masterpiece newItem = new Masterpiece(id, name, desc, image, video, audio, year, status, cons_state, biography);

                        item = masterpieceController.save(newItem);

                        museumController.addMasterpieceToMuseumList(museumList.get(collec_mus.get(collec_id - 1) - 1), item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Teste", "erro3");
                }

                Museum mMuseum = museumController.getMuseumById(0);
                Log.d("Tam1", String.valueOf(mMuseum.getGallery().size()));
                mMuseum = museumController.getMuseumById(1);
                Log.d("Tam2", String.valueOf(mMuseum.getGallery().size()));
                mMuseum = museumController.getMuseumById(2);
                Log.d("Tam3", String.valueOf(mMuseum.getGallery().size()));

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

    private String readerFromFile(int file) {
        InputStream is = getResources().openRawResource(file);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("Teste", "erro1");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Teste", "erro2");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String data = writer.toString();
        return data;
    }


}