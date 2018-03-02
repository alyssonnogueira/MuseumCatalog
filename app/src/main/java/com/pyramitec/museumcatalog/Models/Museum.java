package com.pyramitec.museumcatalog.Models;

import android.support.annotation.VisibleForTesting;

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
    private String description;
    private String address;
    private String phone;
    private String opening_hours;

    public Museum() {
    }

    public Museum(String idPlataformMuseum, String name, String logoPath) {
        this.idPlataformMuseum = idPlataformMuseum;
        this.name = name;
        this.logoPath = logoPath;
    }

    public Museum(int id, String idPlataformMuseum, String name, String logoPath, String description, String address, String phone, String opening_hours) {
        this.idMuseum = id;
        this.idPlataformMuseum = idPlataformMuseum;
        this.name = name;
        this.logoPath = logoPath;
        this.description =description;
        this.address = address;
        this.phone = phone;
        this.opening_hours = opening_hours;
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

    public String getText()
    {
        String text = this.name + "\n\n";
        if(this.description!= null)
            text = text + this.description + "\n\n";

        return text;
    }

    public String getInfo()
    {
        String text = "";
        if(this.opening_hours!= null)
            text = text + "Aberto: " + this.opening_hours + "\n";
        if(this.address!= null)
            text = text + "Endereco: " + this.address + "\n";
        if(this.phone!= null)
            text = text + "Fone: " + this.phone + "\n";

        return text;
    }
}
