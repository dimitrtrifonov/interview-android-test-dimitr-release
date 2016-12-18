package com.mobgen.interview.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LenovoY700 on 12/15/2016.
 */

public class GsonParsed { //parse GSON
    @SerializedName("cars")
    private List<Car> cars  = new ArrayList<Car>();

    public List<Car> getCars(){
        return cars;
    }

}