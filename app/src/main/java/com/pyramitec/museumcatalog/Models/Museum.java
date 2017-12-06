package com.pyramitec.museumcatalog.Models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alyss on 02/12/2017.
 */

public class Museum extends RealmObject {
    @PrimaryKey
    private long idMuseum;
    private String idPlataformMuseum;
    private String name;
    private String logoPath;
    private RealmList<Masterpiece> Gallery;
    private String informations;

    public Museum() {
    }

    public Museum(String idPlataformMuseum, String name, String logoPath) {
        this.idPlataformMuseum = idPlataformMuseum;
        this.name = name;
        this.logoPath = logoPath;
    }

    public long getIdMuseum() {
        return idMuseum;
    }

    public void setIdMuseum(long idMuseum) {
        this.idMuseum = idMuseum;
    }

    public String getIdPlataformMuseum() {
        return idPlataformMuseum;
    }

    public void setIdPlataformMuseum(String idPlataformMuseum) {
        this.idPlataformMuseum = idPlataformMuseum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public RealmList<Masterpiece> getGallery() {
        return Gallery;
    }

    public void setGallery(RealmList<Masterpiece> gallery) {
        Gallery = gallery;
    }

    public void setMasterpiece(Masterpiece masterpiece){
        this.Gallery.add(masterpiece);
    }

    public Masterpiece getMasterpieceFromList(int position){
        return this.Gallery.get(position);
    }
}
