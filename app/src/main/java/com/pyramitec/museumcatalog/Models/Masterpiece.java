package com.pyramitec.museumcatalog.Models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by alyss on 02/12/2017.
 */

public class Masterpiece extends RealmObject {
    @PrimaryKey
    private long idMasterpiece;
    private String name;
    private String description;
    private String author;
    private String imagePath;
    private String videoPath;
    private String audioPath;
    private String year;
    private String status;
    private String conservation_state;


    public Masterpiece() {
    }

    public Masterpiece(String name, String description, String author, String imagePath, String videoPath, String audioPath, String year) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.audioPath = audioPath;
        this.year = year;
    }

    public Masterpiece(int id, String name, String description, String imagePath, String videoPath, String audioPath, String year,
                       String status, String conservation_state, String biography) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
        this.audioPath = audioPath;
        this.year = year;
    }

    public long getIdMasterpiece() {
        return idMasterpiece;
    }

    public void setIdMasterpiece(long idMasterpiece) {
        this.idMasterpiece = idMasterpiece;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return this.status;
    }

    public String getConservation_state() {return this.conservation_state;}
}
