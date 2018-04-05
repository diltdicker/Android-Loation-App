package edu.asu.bsse.dtdicker.assign7;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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

public class RecycleView extends RecyclerView.Adapter<RecycleView.ViewHold>{

    private LayoutInflater layInflate;
    //private String[] list = {"test1", "test2", "test5", "test6"};
    private ArrayList<String> list;

    public RecycleView() {
        list = new ArrayList<String>();
    }

    public RecycleView(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        ViewHold vh = new ViewHold(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        holder.plDataTextView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(String var) {
        list.add(var);
        notifyItemInserted(list.size()-1);
    }

    public void removeItem(int i){
        Log.d("rm", "size: " + list.size());
        list.remove(i);
        Log.d("rm", "size2: " + list.size());
        notifyItemRemoved(i);
        Log.d("rm", "size3: " + list.size());
    }

    public static class ViewHold extends RecyclerView.ViewHolder {

        public TextView plDataTextView;

        public ViewHold(TextView itemView) {
            super(itemView);
            plDataTextView = (TextView) itemView;
        }
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
