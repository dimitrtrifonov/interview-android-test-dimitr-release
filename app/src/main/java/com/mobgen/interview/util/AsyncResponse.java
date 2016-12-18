package com.mobgen.interview.util;

import com.mobgen.interview.model.Car;

import java.util.List;

/**
 * Created by LenovoY700 on 12/15/2016.
 */

public interface AsyncResponse {
    void processFinished(List<Car> cars);
}
