package com.jp.band.com.smartkube;

/**
 * Created by kai on 8/4/16.
 */
public class Item {

    String header;
    String description;
    String image;
    String old_price;
    String new_price;

    public void setHeader(String header) {
        this.header = header;
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

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getDescription() {
        return description;
    }

    public String getHeader() {
        return header;
    }


    public String getNew_price() {
        return new_price;
    }


    public String getold_price() {
        return old_price;
    }
}
