package ca.javajeff.mathpro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 15.08.2017.
 */

public class SolutionAdapter extends ArrayAdapter<String> {
    int pos;
    String year;
    int size;
    Context context;
    ArrayList<String> namesOnSolution;
    ArrayList<String> titleArray;
//    private final LlistAdapter.ListAdapterOnClickHandler mClickHandler;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference MyRef;

    public SolutionAdapter(Context c, int resource, ArrayList<String> titles, ArrayList<String> names) {
        super(c, resource, titles);
//        mClickHandler = clickHandler;
        this.namesOnSolution=names;
        this.context = c;
        this.size=resource;
        this.titleArray = titles;
        Log.i("deeee", String.valueOf(titleArray));
    }

    public interface SolutionAdapterOnClickHandler {

        void onClick(String problemForDay);
    }

    public class MyViewHolder {
        MathView myText;
        TextView myName;
        private int adapterPosition;

        MyViewHolder(View v){

            myText=(MathView) v.findViewById(R.id.solution);
            myName=(TextView) v.findViewById(R.id.name_solver);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final MyViewHolder holder;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        MyRef = mFirebaseDatabase.getReference();
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.solution_list_item, parent, false);
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

        if (namesOnSolution != null) {
            holder.myName.setText(namesOnSolution.get(position));
        } else {
            holder.myName.setText("Anonymous user");
        }

        pos=position;
        return row;
    }
}
