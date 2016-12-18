package com.mobgen.interview.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper { //used to handle database with cars

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "cars_record_db";
    private static final String TABLE_NAME="cars_records";
    private static final String COLUMN1_NAME="title";
    private static final String COLUMN2_NAME="owner";
    private static final String COLUMN3_NAME="date";
    private static final String COLUMN4_NAME="image1";
    private static final String COLUMN5_NAME="image2";
    private static final String COLUMN6_NAME="image3";
    private static final String COLUMN7_NAME="pdf";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create CarRecords table
        String CREATE_CARS_TABLE = "CREATE TABLE "+TABLE_NAME+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN1_NAME+" TEXT, "+
                COLUMN2_NAME+" TEXT, "+
                COLUMN3_NAME+" TEXT, "+
                COLUMN4_NAME+" TEXT, "+
                COLUMN5_NAME+" TEXT, "+
                COLUMN6_NAME+" TEXT, "+
                COLUMN7_NAME+" TEXT )";

        // create CarRecords table
        db.execSQL(CREATE_CARS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older CarRecords table if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        // create fresh CarRecords table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) a car record + get all car records + delete all car records
     */

    // Car records Table Columns names
    private static final String KEY_ID = "id";

    private static final String[] COLUMNS = {KEY_ID,COLUMN1_NAME,COLUMN2_NAME,COLUMN3_NAME,
            COLUMN4_NAME,COLUMN5_NAME,COLUMN6_NAME,COLUMN7_NAME};

    public void addCarRecord(CarRecord carRecord){
        //Log.d("addCarRecord", carRecord.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COLUMN1_NAME, carRecord.getTitle()); // get title
        values.put(COLUMN2_NAME, carRecord.getOwner()); // get owner
        values.put(COLUMN3_NAME, carRecord.getDate()); // get date
        values.put(COLUMN4_NAME, carRecord.getImageList().get(0)); // get image 1
        if(carRecord.getImageList().size()>1)values.put(COLUMN5_NAME, carRecord.getImageList().get(1)); // get image 2 (if exists)
        if(carRecord.getImageList().size()>2)values.put(COLUMN6_NAME, carRecord.getImageList().get(2)); // get image 3 (if exists)
        values.put(COLUMN7_NAME, carRecord.getPdf()); // get pdf

        // 3. insert
        db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public CarRecord getCarRecord(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_NAME, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build carRecord object
        CarRecord carRecord = new CarRecord();
        carRecord.setId(Integer.parseInt(cursor.getString(0)));
        carRecord.setTitle(cursor.getString(1));
        carRecord.setOwner(cursor.getString(2));
        carRecord.setDate(cursor.getString(3));
        List<String> images = new ArrayList<String>();
        images.add(cursor.getString(4));
        images.add(cursor.getString(5));
        images.add(cursor.getString(6));
        carRecord.setImageList(images);
        carRecord.setPdf(cursor.getString(7));

        //Log.d("getCarRecord(" + id + ")", carRecord.toString());

        // 5. return carRecord
        return carRecord;
    }

    // Get All Car Records
    public List<CarRecord> getAllCarRecords() {
        List<CarRecord> carRecordsList = new LinkedList<CarRecord>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build carRecord and add it to list
        CarRecord carRecord = null;
        if (cursor.moveToFirst()) {
            do {
                carRecord = new CarRecord();
                carRecord.setId(Integer.parseInt(cursor.getString(0)));
                carRecord.setTitle(cursor.getString(1));
                carRecord.setOwner(cursor.getString(2));
                carRecord.setDate(cursor.getString(3));
                List<String> images = new ArrayList<String>();
                images.add(cursor.getString(4));
                images.add(cursor.getString(5));
                images.add(cursor.getString(6));
                carRecord.setImageList(images);
                carRecord.setPdf(cursor.getString(7));
                // Add Car Record to Car Records
                carRecordsList.add(carRecord);
            } while (cursor.moveToNext());
        }

        //Log.d("getAllCarRecords()", carRecordsList.toString());


        return carRecordsList;
    }

}