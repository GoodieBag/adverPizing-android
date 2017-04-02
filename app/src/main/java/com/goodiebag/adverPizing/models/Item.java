package com.goodiebag.adverPizing.models;

/**
 * Created by kai on 8/4/16.
 */
public class Item {

    String name;
    String date;
    String description;
    String image;
    String place;
    String lastDate;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

}
