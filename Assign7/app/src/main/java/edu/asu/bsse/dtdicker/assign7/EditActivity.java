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

public class EditActivity extends AppCompatActivity {

    private PlaceDescription place;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent previous = getIntent();
        Log.d("DEBUG", "test");
        place = (PlaceDescription) previous.getSerializableExtra("place");
        placeName = place.getPlace();
        Log.d("DEBUG", place.getPlace());
        updateDetails();
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
                //RPCRequest rpc = new RPCRequest(this, getString(R.string.urlString), "remove", new Object[]{place.getPlace()});
                //new AsyncRequest().execute(rpc);
                grabEdits();
                //RPCRequest rpc2 = new RPCRequest(this, getString(R.string.urlString), "add", new Object[]{place.toJSON()});
                //new AsyncRequest().execute(rpc2);
                updateDB(place);
                Intent intent = new Intent();
                intent.putExtra("placeName", place.getName());
                setResult(0, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateDetails() {
        TextView tView = new TextView(this);

        TextView mod = (TextView)findViewById(R.id.place);
        mod.setText(place.getPlace());
        mod = (TextView)findViewById(R.id.add_title_edit);
        mod.setText(place.getAddTitle());
        mod = (TextView)findViewById(R.id.add_street_edit);
        mod.setText(place.getAddStreet());
        mod = (TextView)findViewById(R.id.elevation_edit);
        mod.setText(place.getElevation());
        mod = (TextView)findViewById(R.id.latitude_edit);
        mod.setText(place.getLatitude());
        mod = (TextView)findViewById(R.id.longitude_edit);
        mod.setText(place.getLongitude());
        mod = (TextView)findViewById(R.id.name_edit);
        mod.setText(place.getName());
        mod = (TextView)findViewById(R.id.image_edit);
        mod.setText(place.getImage());
        mod = (TextView)findViewById(R.id.description_edit);
        mod.setText(place.getDescription());
        mod = (TextView)findViewById(R.id.category_edit);
        mod.setText(place.getCategory());
        Log.d("DEBUG", "update Details");
    }

    public void grabEdits() {
        TextView mod = new TextView(this);
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

    public void updateDB(PlaceDescription pl) {
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
            cur = placeDB.rawQuery("update places set title = ?, street = ?, elevation = ?, latitude = ?, longitude = ?, name = ?, image = ?, description = ?, category = ? where placeid = ?;",
                    new String[] {pl.getAddTitle(), pl.getAddStreet(), pl.getElevation(), pl.getLatitude(), pl.getLongitude(), pl.getName(), pl.getImage(), pl.getDescription(), pl.getCategory(),
                            String.valueOf(id)});
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
