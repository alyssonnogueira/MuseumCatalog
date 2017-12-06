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

public class MasterpieceController {

    public final String TAG = this.getClass().getSimpleName();

    Realm realm;
    RealmConfiguration realmConfig;

    public MasterpieceController (Context context) {

        realmConfig = new RealmConfiguration.Builder()
                .name("museumCatalog.realm")
                .build();
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

    public Masterpiece save(final Masterpiece masterpiece){
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Masterpiece master = realm.copyToRealmOrUpdate(masterpiece);
            realm.commitTransaction();
            return master;
    }

    public void clearMasterpiece(){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    realm.delete(Masterpiece.class);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            realm.close();
        }
    }
    /*public List<UserModel> getUser() {
        RealmQuery<UserModel> query = realm.where(UserModel.class);
        return query.findAll();
    }

    public void addAccList(final AccModel accModel, final ActModel actModel){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                actModel.setAccModel(accModel);
            }
        });
    }

    public void setIdActivityMongoDB(final ActModel actModel,final String idActivityMongoDB){
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                actModel.setIdActivityMongoDB(idActivityMongoDB);
            }
        });
    }*/
}
