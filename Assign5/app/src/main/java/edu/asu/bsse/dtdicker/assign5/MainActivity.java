package edu.asu.bsse.dtdicker.assign5;

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

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecycleView rView;
    private int urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView tmpRecyclerView = (RecyclerView) findViewById(R.id.main_recycle);
        tmpRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager tmpRecycleViewLayoutManager = new LinearLayoutManager((this));
        tmpRecyclerView.setLayoutManager(tmpRecycleViewLayoutManager);
        rView = new RecycleView();
        tmpRecyclerView.setAdapter(rView);

        /*try {
            //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            //StrictMode.setThreadPolicy(policy);
            //HTTPHandle http = new HTTPHandle(new URL("http://192.168.0.8:8423"), this);
            //String res = http.call("{ \"jsonrpc\":\"2.0\", \"method\":\"getnames\", \"params\":"+"[]"+",\"id\":3}");
            //Log.d("DEBUG", res);
            RPCRequest rpc = new RPCRequest(this, "test", "test2", new Object[]{});
            AsyncRequest asnc = (AsyncRequest) new AsyncRequest().execute(rpc);
            Log.d("DEBUG", "end of Create");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DEBUG", "Exception");
        }*/


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Log.d("ADD", "onOptionsItemSelected: ADD");
                PlaceDescription tmp = new PlaceDescription("help me");
                Intent viewIntent = new Intent(MainActivity.this, AddActivity.class);
                viewIntent.putExtra("place", tmp);
                this.startActivity(viewIntent);
                return true;
            case R.id.action_remove:
                Log.d("RM", "onOptionsItemSelected: REMOVE");
                return true;
            case R.id.action_edit:
                Log.d("EDIT", "onOptionsItemSelected: EDIT");
                return true;
            case R.id.action_done:
                Log.d("DONE", "onOptionsItemSelected: DONE");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void asyncUpdate(ArrayList<String> list) {
        Log.d("DEBUG", "Async update of UI");
        rView.setList(list);
    }

    public void onTap(View view) {
        TextView t = (TextView) view;
        Log.d("tap click", "onTap: list size: " + rView.getItemCount());
        Log.d("tap click", "onTap: Tapped: " + t.getText());
        Intent viewIntent = new Intent(MainActivity.this, ViewActivity.class);
        viewIntent.putExtra("placeName", t.getText());
        this.startActivity(viewIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RPCRequest rpc = new RPCRequest(this, getString(R.string.urlString), "getNames", new Object[]{});
        new AsyncRequest().execute(rpc);
    }
}
