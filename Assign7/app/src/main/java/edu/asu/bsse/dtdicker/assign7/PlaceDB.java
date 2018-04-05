package edu.asu.bsse.dtdicker.assign7;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//---------------------------------------------------------------------------
//    Copyright (C) 2018 Dillon Dickerson
//	  email: diltdicker@gmail.com
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.
//---------------------------------------------------------------------------

public class PlaceDB extends SQLiteOpenHelper {

    private static final boolean debug = true;
    private static final int DBVersion = 3;
    private static String dbName = "places";
    private String dbPATH;
    private SQLiteDatabase placesDB;
    private final Context context;

    public PlaceDB(Context context) {
        super(context, dbName, null, DBVersion);
        this.context = context;
        dbPATH = context.getFilesDir().getPath()+"/";
        Log.d(this.getClass().getSimpleName(), "DB path: " + dbPATH);
    }

    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    private boolean checkDB() {
        SQLiteDatabase checkDB = null;
        boolean crsTabExists = false;
        try{
            String path = dbPATH + dbName + ".database";
            debug("CourseDB --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    debug("CourseDB --> checkDB","opened db at: "+checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='places';", null);
                    if(tabChk == null){
                        debug("CourseDB --> checkDB","check for course table result set is null");
                    }else{
                        tabChk.moveToNext();
                        debug("CourseDB --> checkDB","check for course table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : (String) tabChk.getString(0))));
                        crsTabExists = !tabChk.isAfterLast();
                    }
                    if(crsTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM places", null);
                        c.moveToFirst();
                        while(!c.isAfterLast()) {
                            String crsName = c.getString(5);
                            int crsid = c.getInt(9);
                            debug("CourseDB --> checkDB","Place table has PlaceName: "+
                                    crsName+"\tCourseID: "+crsid);
                            c.moveToNext();
                        }
                        crsTabExists = true;
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("CourseDB->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return crsTabExists;
    }

    public boolean customCheckDB() {
        SQLiteDatabase db = null;
        String path = dbPATH + dbName + ".database";
        File file = new File(path);
        if (file.exists()) {
            Log.d("file", "file exists");
            file.setWritable(true);
        }
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor chk = db.rawQuery("select * from places", null);
        while (chk.moveToNext()) {
            Log.d("collumn count", String.valueOf(chk.getColumnCount()));
            Log.d("first", chk.getString(0));
        }
        chk.close();
        db.close();
        return true;
    }

    public void copyDB() throws IOException {
        try {
            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                debug("CourseDB --> copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.places);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPATH);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPATH  +  dbName +".database";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("CourseDB --> copyDB", "IOException: "+e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPATH + dbName + ".database";
        if(checkDB()) {
            placesDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            debug("CourseDB --> openDB", "opened db at path: " + placesDB.getPath());
        }else{
            try {
                this.copyDB();
                placesDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),"unable to copy and open db: "+ex.getMessage());
            }
        }
        return placesDB;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public synchronized void close() {
        super.close();
    }

    private void debug(String hdr, String msg){
        if(debug){
            android.util.Log.d(hdr,msg);
        }
    }
}
