package com.mobgen.interview.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LenovoY700 on 12/15/2016.
 */

public class Car {

    @SerializedName("title")
    private String title;
    @SerializedName("date")
    private String date;
    @SerializedName("pdf")
    private String pdf;
    @SerializedName("owner")
    private String owner;
    @SerializedName("images")
    private List<String> images  = new ArrayList<String>();

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date=date;
    }

    public String getPdf(){
        return pdf;
    }

    public void setPdf(String pdf){
        this.pdf=pdf;
    }

    public String getOwner(){
        return owner;
    }

    public void setOwner(String owner){
        this.owner=owner;
    }

    public List<String> getImages(){
        return images;
    }

    public void setImages(List<String> images){
        this.images=images;
    }
}
