package edu.asu.bsse.dtdicker.assign5;

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

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AsyncRequest extends AsyncTask<RPCRequest, Integer, RPCRequest> {

    private static int id = 0;

    @Override
    protected RPCRequest doInBackground(RPCRequest... rpcRequests)  {
        Log.d("DEBUG", "in BKG Execute");
        try {
            //"http://192.168.0.8:8423"
            //"{ \"jsonrpc\": \"2.0\", \"method\": \"getNames\", \"params\": [ ], \"id\": 3}"
            HTTPHandle http = new HTTPHandle(new URL(rpcRequests[0].urlString), rpcRequests[0].parent);
            id++;
            String param = "";
            if (rpcRequests[0].params.length > 0 && !rpcRequests[0].method.equals("add")) {
                for (int i = 0; i < rpcRequests[0].params.length; i ++) {
                    param += "\"" + (String) rpcRequests[0].params[i] + "\"" ;
                }
            } else if (rpcRequests[0].method.equals("add")) {
                for (int i = 0; i < rpcRequests[0].params.length; i ++) {
                    param += (String) rpcRequests[0].params[i];
                }
            }
            String res = http.call("{ \"jsonrpc\": \"2.0\", \"method\": \"" + rpcRequests[0].method + "\", \"params\": [" + param + "], \"id\": "+ id +" }");

            rpcRequests[0].resultAsJson = res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rpcRequests[0];
    }

    @Override
    public void onPreExecute(){
        Log.d("DEBUG", "pre Execute");
    }

    @Override
    protected void onPostExecute(RPCRequest req) {

        if (req.method.equals("getNames")) {
            ArrayList<String> list = new ArrayList<String>();
            try {
                JSONObject jsonObj = new JSONObject(req.resultAsJson);
                JSONArray placeList = jsonObj.getJSONArray("result");
                for (int i = 0; i < placeList.length(); i++) {
                    list.add(placeList.getString(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((MainActivity) req.parent).asyncUpdate(list);
            //req.parent.asyncUpdate(list);
        }
        if (req.method.equals("get")) {
            PlaceDescription place = new PlaceDescription("New Place");
            try {
                JSONObject jsonObj = new JSONObject(req.resultAsJson);
                place = new PlaceDescription(jsonObj.getJSONObject("result"), jsonObj.getJSONObject("result").getString("name"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((ViewActivity) req.parent).updateDetails(place);
            //req.parent.updateDetails(null);
        }
        Log.d("DEBUG", "post Execute");
        Log.d("DEBUG", req.method);
        Log.d("DEBUg", req.resultAsJson);
        //req.parent.asyncUpdate(null);
    }
}
