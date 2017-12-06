package com.pyramitec.museumcatalog.Controllers;

import android.content.Context;

import com.pyramitec.museumcatalog.Models.Masterpiece;
import com.pyramitec.museumcatalog.Models.Museum;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by alyss on 02/12/2017.
 */

public class MuseumController {

    public final String TAG = this.getClass().getSimpleName();

    Realm realm;
    RealmConfiguration realmConfig;
    Context mContext;

    public MuseumController () {
        //mContext = context;
        //realmConfig = new RealmConfiguration.Builder()
         //       .name("museumCatalog.realm")
           //     .build();
        //Realm.setDefaultConfiguration(realmConfig);
        // Get a Realm instance for this thread
        //realm = Realm.getInstance(realmConfig);
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public RealmConfiguration getRealmConfig() {
        return realmConfig;
    }

    public void setRealmConfig(RealmConfiguration realmConfig) {
        this.realmConfig = realmConfig;
    }

    public Museum save(final Museum museum){
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Museum museum1 = realm.copyToRealmOrUpdate(museum);
            realm.commitTransaction();
            this.closeInstance();
            return museum1;
    }

    public void addMasterpieceToMuseumList(final Museum museum, final Masterpiece masterpiece){
        //try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    museum.setMasterpiece(masterpiece);
                }
            });
        //}catch(Exception e){
          //  e.printStackTrace();
        //}finally {
         //   this.closeInstance();
        //}
    }

    public void clearMuseums(){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    realm.delete(Museum.class);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            this.closeInstance();
        }
    }

    public List<Museum> getMuseums() {
        List<Museum> museumList = null;
        try {
            realm = Realm.getDefaultInstance();
            museumList = realm.where(Museum.class).findAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //realm.close();
        }
        return museumList;
    }

    public Museum getMuseumById(long idMuseum) {
        Museum museum = null;
        try {
            realm = Realm.getDefaultInstance();
            museum = realm.where(Museum.class).equalTo("idMuseum", idMuseum).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //realm.close();
        }
        return museum;
    }

    public void closeInstance(){
        if (!realm.isClosed()) {
            realm.close();
        }
    }
}
