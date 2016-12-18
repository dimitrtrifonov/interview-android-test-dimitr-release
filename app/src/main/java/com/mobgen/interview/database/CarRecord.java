package com.mobgen.interview.database;

import java.util.List;

/**
 * Created by LenovoY700 on 12/17/2016.
 */

public class CarRecord {

    int id;
    private String title;
    private String date;
    private String owner;
    private List<String> images;
    private String pdf;

    public CarRecord(){

    }


    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getOwner(){
        return this.owner;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }

    public List<String> getImageList(){
        return this.images;
    }

    public void setImageList(List<String> images){
        this.images = images;
    }

    public String getPdf(){
        return this.pdf;
    }

    public void setPdf(String pdf){
        this.pdf = pdf;
    }
}
