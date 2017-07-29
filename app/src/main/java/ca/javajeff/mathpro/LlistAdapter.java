package ca.javajeff.mathpro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 28.07.2017.
 */

class LlistAdapter extends ArrayAdapter<String> {
    int size;
    Context context;
    ArrayList<String> titleArray;
    LlistAdapter(Context c, int resource, ArrayList<String> titles) {
        super(c, resource, titles);
        this.context = c;
        this.size=resource;
        this.titleArray = titles;
        Log.i("deeee", String.valueOf(titleArray));
    }
    class MyViewHolder {
        MathView myText;
        MyViewHolder(View v){
            myText=(MathView) v.findViewById(R.id.textlist);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyViewHolder holder = null;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_row, parent, false);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        }
        else {
            holder = (MyViewHolder) row.getTag();
        }
        holder.myText.setText(titleArray.get(position));
        return row;
    }
}
