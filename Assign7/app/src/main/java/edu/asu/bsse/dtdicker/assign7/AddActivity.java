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

public class AddActivity extends AppCompatActivity {

    private PlaceDescription place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent previous = getIntent();
        Log.d("DEBUG", "test");
        place = new PlaceDescription("New Place");
        Log.d("DEBUG", place.getPlace());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_view, menu);
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
                return true;
            case R.id.action_edit:
                Log.d("EDIT", "onOptionsItemSelected: EDIT");
                return true;
            case R.id.action_done:
                Log.d("DONE", "onOptionsItemSelected: DONE");
                grabEdits();
                if (place.getName().equals("")) {
                    place.setName("New Place");
                }
                //RPCRequest rpc = new RPCRequest(this, getString(R.string.urlString), "add", new Object[]{place.toJSON()});
                //new AsyncRequest().execute(rpc);
                addToDB(place);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void grabEdits() {
        TextView mod = (TextView)findViewById(R.id.place_edit);
        String name = mod.getText().toString();
        place = new PlaceDescription(name);
        mod = (TextView)findViewById(R.id.add_title_edit);
        Log.d("TEST", mod.getText().toString());
        place.setAddTitle(mod.getText().toString());
        mod = (TextView)findViewById(R.id.add_street_edit);
        place.setAddStreet(mod.getText().toString());
        mod = (TextView)findViewById(R.id.elevation_edit);
        place.setElevation(mod.getText().toString());
        mod = (TextView)findViewById(R.id.latitude_edit);
        place.setLatitude(mod.getText().toString());
        mod = (TextView)findViewById(R.id.longitude_edit);
        place.setLongitude(mod.getText().toString());
        mod = (TextView)findViewById(R.id.name_edit);
        place.setName(mod.getText().toString());
        mod = (TextView)findViewById(R.id.image_edit);
        place.setImage(mod.getText().toString());
        mod = (TextView)findViewById(R.id.description_edit);
        place.setDescription(mod.getText().toString());
        mod = (TextView)findViewById(R.id.category_edit);
        place.setCategory(mod.getText().toString());
    }

    public void addToDB(PlaceDescription pl) {
        PlaceDB db;
        SQLiteDatabase placeDB = null;
        Cursor cur = null;
        int id = -1;
        try {
            db = new PlaceDB((Context)this);
            //db.customCheckDB();
            placeDB = db.openDB();
            cur = placeDB.rawQuery("select placeid from places order by placeid desc;", null);
            Log.d("add names", String.valueOf(cur.getColumnCount()));
            cur.moveToFirst();
            if (!cur.isAfterLast()) {
                id = cur.getInt(0);
            }
            id ++;
            cur = placeDB.rawQuery("insert into places (title, street, elevation, latitude, longitude," +
                    " name, image, description, category, placeid) values ('" + pl.getAddTitle() + "'," +
                    " '" + pl.getAddStreet() + "', '" + pl.getElevation() + "', '" + pl.getLatitude() + "', '" + pl.getLongitude() + "'," +
                    " '" + pl.getName() + "', '" + pl.getImage() + "', '" + pl.getDescription() + "', '" + pl.getCategory() + "', '" + id + "');", null);
            Log.d("add names", String.valueOf(cur.getColumnCount()));
            cur.moveToFirst();
            while (!cur.isAfterLast()) {

                Log.d(this.getClass().getSimpleName(), cur.getString(0));
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
