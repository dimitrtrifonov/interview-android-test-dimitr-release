package com.mobgen.interview.activity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobgen.interview.database.CarRecord;
import com.mobgen.interview.database.DatabaseHandler;
import com.mobgen.interview.util.AsyncResponse;

import com.mobgen.interview.R;
import com.mobgen.interview.model.Car;
import com.mobgen.interview.model.GsonParsed;
import com.mobgen.interview.util.RecyclerAdapter;
import com.mobgen.interview.util.ShowPDF;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AsyncResponse ,RecyclerAdapter.OnRecyclerViewItemClickListener{

	public final String TAG = getClass().getSimpleName();

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<Car> cars = new ArrayList<Car>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.list_view);

        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        List<CarRecord> dbList = db.getAllCarRecords();

        if(dbList.size()==0){//case app is opened first time ever - load information from car_list.json
            DownloadDataAndParseGSON downloadDataAndParseGSON = new DownloadDataAndParseGSON((AsyncResponse)this);
            downloadDataAndParseGSON.execute();
        }
        else {//case cars are previously added to database
            loadCarsFromDatabase();
            Toast.makeText(getApplicationContext(), "Cars Data loaded from Database", Toast.LENGTH_LONG).show();
        }
	}

    @Override
    public void onRecyclerViewItemClick(int index) {
        //Toast.makeText(this,"clicked="+ index,Toast.LENGTH_SHORT).show();
        new ShowPDF(this,getAssets(),cars.get(index).getPdf());
    }

    private class DownloadDataAndParseGSON extends AsyncTask<String, Void, Void> { //get cars information from car_list.json and parse

        AsyncResponse asyncResponse;
        List<Car> _cars;

        public DownloadDataAndParseGSON(AsyncResponse asyncResponse){
            this.asyncResponse=asyncResponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            GsonParsed gsonParsed;
            AssetManager assetManager = getAssets();
            try {
                InputStream ims = assetManager.open("car_list.json");
                Reader reader = new InputStreamReader(ims);
                Gson gson = new Gson();
                gsonParsed = gson.fromJson(reader, GsonParsed.class);
                _cars = gsonParsed.getCars();
                //Log.i(TAG,gsonParsed.getCars().get(0).getOwner());
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            asyncResponse.processFinished(_cars);
        }
    }

    @Override
    public void processFinished(List<Car> cars) { //load information from car_list.json finished
        //Log.i(TAG,cars.get(0).getImages().get(0));

        this.cars=cars;

        setRecyclerViewAdapter();//display cars

        for(int i=0;i<cars.size();i++){
            addCarRecordToDatabase(i,this.cars.get(i)); //add information from car_list.json to database
        }

        Toast.makeText(getApplicationContext(), "Added Cars Data to Database", Toast.LENGTH_LONG).show();
    }

    void addCarRecordToDatabase(int id,Car car){//after
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        CarRecord carRecord = new CarRecord(); //create new record for the database
        carRecord.setId(id);
        carRecord.setTitle(car.getTitle());
        carRecord.setOwner(car.getOwner());
        carRecord.setDate(car.getDate());
        carRecord.setImageList(car.getImages());
        carRecord.setPdf(car.getPdf());

        db.addCarRecord(carRecord);
    }

    void loadCarsFromDatabase(){//load car records from database and fill car array
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        List<CarRecord> dbList = db.getAllCarRecords();
        for(int i=0;i<dbList.size();i++){
            Car car = new Car();
            car.setTitle(dbList.get(i).getTitle());
            car.setOwner(dbList.get(i).getOwner());
            car.setDate(dbList.get(i).getDate());
            List<String> images = new ArrayList<String>();
            List<String> imagesDB = dbList.get(i).getImageList();
            for(int j=0;j<imagesDB.size();j++){
                if(imagesDB.get(j)!=null)images.add(imagesDB.get(j));
            }
            car.setImages(images);
            car.setPdf(dbList.get(i).getPdf());

            cars.add(car);
            setRecyclerViewAdapter();
        }
    }

    void setRecyclerViewAdapter(){
        recyclerAdapter = new RecyclerAdapter(this,cars);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }

}
