package edu.asu.bsse.dtdicker.assign7;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by diltdicker on 3/18/18.
 */
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

public class ViewActivity extends AppCompatActivity {


    private PlaceDescription place;
    private String placeName;
    private boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lock = false;
        setContentView(R.layout.activity_view);
        Intent previous = getIntent();
        Log.d("DEBUG", "test");
        placeName = previous.getStringExtra("placeName");
        //place = (PlaceDescription) previous.getSerializableExtra("place");
        //Log.d("DEBUG", place.getPlace());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Log.d("ADD", "onOptionsItemSelected: ADD");
                return true;
            case R.id.action_remove:
                Log.d("RM", "onOptionsItemSelected: REMOVE");
                //RPCRequest rpc = new RPCRequest(this, getString(R.string.urlString), "remove", new Object[]{place.getPlace()});
                //new AsyncRequest().execute(rpc);
                deleteDB();
                finish();
                return true;
            case R.id.action_edit:
                if (lock) {
                    // do nothing
                } else {
                    Log.d("EDIT", "onOptionsItemSelected: EDIT");
                    Intent viewIntent = new Intent(ViewActivity.this, EditActivity.class);
                    viewIntent.putExtra("place", place);
                    this.startActivityForResult(viewIntent, 111);
                }
                return true;
            case R.id.action_done:
                Log.d("DONE", "onOptionsItemSelected: DONE");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateDetails(PlaceDescription place) {
        this.place = place;
        TextView tView = new TextView(this);

        TextView mod = (TextView)findViewById(R.id.place);
        mod.setText(place.getPlace());
        mod = (TextView)findViewById(R.id.add_title_value);
        mod.setText(place.getAddTitle());
        mod = (TextView)findViewById(R.id.add_street_value);
        mod.setText(place.getAddStreet());
        mod = (TextView)findViewById(R.id.elevation_value);
        mod.setText(place.getElevation());
        mod = (TextView)findViewById(R.id.latitude_value);
        mod.setText(place.getLatitude());
        mod = (TextView)findViewById(R.id.longitude_value);
        mod.setText(place.getLongitude());
        mod = (TextView)findViewById(R.id.name_value);
        mod.setText(place.getPlace());
        mod = (TextView)findViewById(R.id.image_value);
        mod.setText(place.getImage());
        mod = (TextView)findViewById(R.id.description_value);
        mod.setText(place.getDescription());
        mod = (TextView)findViewById(R.id.category_value);
        mod.setText(place.getCategory());
        Log.d("DEBUG", "update Details");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //RPCRequest rpc = new RPCRequest(this, getString(R.string.urlString), "get", new Object[]{placeName});
        //new AsyncRequest().execute(rpc);
        place = viewDB();
        updateDetails(place);
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && data != null) {
            placeName = data.getStringExtra("placeName");
        }
    }

    public PlaceDescription viewDB () {
        PlaceDescription pl = new PlaceDescription(placeName);
        PlaceDB db;
        SQLiteDatabase placeDB = null;
        Cursor cur = null;
        try {
            db = new PlaceDB((Context)this);
            //db.customCheckDB();
            placeDB = db.openDB();
            cur = placeDB.rawQuery("select title, street, elevation, latitude, longitude, name, image, description, category from places where name=?;", new String[]{placeName});
            Log.d("view names", String.valueOf(cur.getColumnCount()));
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                pl.setAddTitle(cur.getString(0));
                pl.setAddStreet(cur.getString(1));
                pl.setElevation(String.valueOf(cur.getDouble(2)));
                pl.setLatitude(String.valueOf(cur.getDouble(3)));
                pl.setLongitude(String.valueOf(cur.getDouble(4)));
                pl.setName(cur.getString(5));
                pl.setPlace(cur.getString(5));
                pl.setImage(cur.getString(6));
                pl.setDescription(cur.getString(7));
                pl.setCategory(cur.getString(8));
                Log.d(this.getClass().getSimpleName(), cur.getString(5));
                cur.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            cur.close();
            placeDB.close();
        }
        return pl;
    }

    public void deleteDB () {
        PlaceDB db;
        SQLiteDatabase placeDB = null;
        Cursor cur = null;
        try {
            db = new PlaceDB((Context)this);
            //db.customCheckDB();
            placeDB = db.openDB();
            cur = placeDB.rawQuery("delete from places where name=?;", new String[]{placeName});
            Log.d("delete names", String.valueOf(cur.getColumnCount()));
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                cur.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            cur.close();
            placeDB.close();
        }
    }
}
