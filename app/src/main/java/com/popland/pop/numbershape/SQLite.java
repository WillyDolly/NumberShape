package com.popland.pop.numbershape;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by hai on 30/08/2017.
 */

public class SQLite extends SQLiteOpenHelper {
Context context;
private static final String DBNAME = "NumSha.db";
private static final String DBLOCATION = "/data/data/com.popland.pop.numbershape/databases/";
String DBPath =  DBLOCATION + DBNAME;
SQLiteDatabase database;

    public SQLite(Context context){
        super(context,DBNAME,null,1);
        this.context = context;
    }

    public void check(){
        File DBFile = context.getDatabasePath(DBNAME);
        if(!DBFile.exists()){
            this.getReadableDatabase();
            if(copyDB())
                Toast.makeText(context,"copy ok",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"not copy ",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean copyDB(){
        try {
            InputStream inputStream = context.getAssets().open(DBNAME);
            FileOutputStream fos = new FileOutputStream(DBPath);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes))>0 )
                fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void openDB(){
        if(database!=null && database.isOpen())
            return;
        database = SQLiteDatabase.openDatabase(DBPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDB(){
        if(database.isOpen())
            database.close();
    }

    public ArrayList<byte[]> DBtoArrayList(){
        openDB();
        ArrayList<byte[]> ImageArray = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM NuIm",null);
        while(cursor.moveToNext())
            ImageArray.add(cursor.getBlob(1));
        closeDB();
        return ImageArray;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
