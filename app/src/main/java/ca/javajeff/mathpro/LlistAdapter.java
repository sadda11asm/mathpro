package ca.javajeff.mathpro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by ?????? on 28.07.2017.
 */

public class LlistAdapter extends ArrayAdapter<String> {
    int pos;
    String year;
    int size;
    Context context;
    ArrayList<String> titleArray;
    private final LlistAdapter.ListAdapterOnClickHandler mClickHandler;

    public LlistAdapter(Context c, int resource, ArrayList<String> titles, LlistAdapter.ListAdapterOnClickHandler clickHandler, String selected) {
        super(c, resource, titles);
        mClickHandler = clickHandler;
        this.context = c;
        this.size=resource;
        this.titleArray = titles;
        this.year=selected;
        Log.i("deeee", String.valueOf(titleArray));
    }

    public interface ListAdapterOnClickHandler {

        void onClick(String problemForDay);
    }

    public class MyViewHolder {
        MathView myText;
        private int adapterPosition;

        MyViewHolder(View v){

            myText=(MathView) v.findViewById(R.id.textlist);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyViewHolder holder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_row, parent, false);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        }
        else {
            holder = (MyViewHolder) row.getTag();
        }
        try {
            holder.myText.config(
                    "MathJax.Hub.Config({\n"+
                            "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                            "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                            "         SVG: { linebreaks: { automatic: true } }\n"+
                            "});");
            holder.myText.setText(titleArray.get(position));
        } catch (Exception e) {
            holder.myText.setText("Some problems with Internet connection");
        }
        pos=position;
        holder.myText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("est", "est");
                int adapterPosition = position;
                String item = getItem(position);
                final Intent intent;
                intent = new Intent(context, ProblemActivity.class);
                intent.putExtra("problem", item);
                intent.putExtra("year", year);
                String problemForDay = titleArray.get(adapterPosition);
                mClickHandler.onClick(problemForDay);
                context.startActivity(intent);
            }
        });
        return row;
    }
}