package edu.asu.bsse.dtdicker.assign5;

import android.content.Intent;
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
                RPCRequest rpc = new RPCRequest(this, getString(R.string.urlString), "remove", new Object[]{place.getPlace()});
                new AsyncRequest().execute(rpc);
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
        RPCRequest rpc = new RPCRequest(this, getString(R.string.urlString), "get", new Object[]{placeName});
        new AsyncRequest().execute(rpc);
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
}
