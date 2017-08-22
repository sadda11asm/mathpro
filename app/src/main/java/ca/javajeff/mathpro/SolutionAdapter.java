package ca.javajeff.mathpro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.internal.LockOnGetVariable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 15.08.2017.
 */

public class SolutionAdapter extends ArrayAdapter<String> {
    int pos;
    int size;
    Context context;
    ArrayList<String> namesOnSolution;
    ArrayList<String> titleArray;
    ArrayList<String> idArray;
//    private final LlistAdapter.ListAdapterOnClickHandler mClickHandler;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference MyRef;
    private String number;
    private String year;
    private String name;
    private String type;
    private String idOf;

    public SolutionAdapter(Context c, int resource, ArrayList<String> titles, ArrayList<String> names, ArrayList<String> ids, String type, String name, String year, String number) {
        super(c, resource, titles);
//        mClickHandler = clickHandler;
        this.namesOnSolution=names;
        this.context = c;
        this.size=resource;
        this.titleArray = titles;
        this.idArray=ids;
        this.type=type;
        this.number=number;
        this.year=year;
        this.name=name;
        Log.i("deeee", String.valueOf(titleArray));
    }

    public interface SolutionAdapterOnClickHandler {

        void onClick(String problemForDay);
    }

    public class MyViewHolder {
        MathView myText;
        TextView myName;
        ImageView likeOn;
        ImageView likeOff;
        ImageView dislikeOn;
        ImageView dislikeOff;
        TextView plus;
        TextView minus;
        TextView number;


        MyViewHolder(View v){
            likeOn =(ImageView) v.findViewById(R.id.like_on);
            dislikeOn =(ImageView) v.findViewById(R.id.dislike_on);
            likeOff =(ImageView) v.findViewById(R.id.like_off);
            dislikeOff =(ImageView) v.findViewById(R.id.dislike_off);
            myText=(MathView) v.findViewById(R.id.solution);
            myName=(TextView) v.findViewById(R.id.name_solver);
            plus = (TextView) v.findViewById(R.id.plus);
            minus = (TextView) v.findViewById(R.id.minus);
            number = (TextView) v.findViewById(R.id.number);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        try {
//            liked = ProblemActivity.liked.get(position);
//        } catch(Exception e) {
//            liked=0;
//        }
//        try {
//            disliked = ProblemActivity.disliked.get(position);
//        } catch(Exception e) {
//            disliked=0;
//        }

        View row = convertView;
        final MyViewHolder holder;
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
        holder.number.setText(String.valueOf(position+1));
//        Log.i("vsevse", type+name+year+number+position);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        MyRef = mFirebaseDatabase.getReference();
        final int[] overallScoreFor = {0};
        final int[] overallScoreAgainst = {0};
        final int[] liked = new int[1];
        final int[] disliked = new int[1];
        final DatabaseReference numberOfSolution = MyRef.child("Solutions").child(type).child(name).child(year).child(number).child("solution").child(String.valueOf(position+1));
        final DatabaseReference profiles = MyRef.child("Profiles");
        final int[] permanentScoreFor = new int[1];
        final int[] permanentScoreMinus = new int[1];

        class loading extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... params) {
                numberOfSolution.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            permanentScoreFor[0] = Integer.parseInt(dataSnapshot.child("Score For").getValue().toString());
                        } catch (Exception e) {
                            permanentScoreFor[0]=0;
                        }
                        holder.plus.setText(String.valueOf(permanentScoreFor[0]));
                        try {
                            permanentScoreMinus[0] = Integer.parseInt(dataSnapshot.child("Score Against").getValue().toString());
                        } catch (Exception e) {
                            permanentScoreMinus[0] = 0;
                        }
                            holder.minus.setText(String.valueOf(permanentScoreMinus[0]));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                numberOfSolution.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int est=0;
                        int disest=0;
                        int disvalue=0;
                        int value = 0;
                        for (DataSnapshot ds:dataSnapshot.child("liked").getChildren()) {
                            Log.i("keyyy", ds.getKey());
                            Log.i("idValue", MainActivity.idValue);
                            Log.i("value", ds.getValue().toString());
                            if (MainActivity.idValue.equals(ds.getKey())) {
                                est=1;
                                value = Integer.parseInt(ds.getValue().toString());
                            } else {
                                est=0;
                            }
                            Log.i("qwert", String.valueOf(est+value));
                        }
                        if (est==1 && value == 1) {
                            liked[0]=1;
                        } else {
                            liked[0]=0;
                        }
                        for (DataSnapshot ds:dataSnapshot.child("disliked").getChildren()) {
                            if (MainActivity.idValue.equals(ds.getKey())) {
                                disest=1;
                                disvalue= Integer.parseInt(ds.getValue().toString());
                            } else {
                                disest=0;
                            }
                        }
                        if (disest==1 && disvalue ==1) {
                            disliked[0]=1;
                        } else {
                            disliked[0]=0;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                profiles.child(idArray.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            overallScoreFor[0] = Integer.parseInt(dataSnapshot.child("Score For").getValue().toString());
                        } catch(Exception e) {
                            overallScoreFor[0] = 0;
                        }
                        try {
                            overallScoreAgainst[0] = Integer.parseInt(dataSnapshot.child("Score Against").getValue().toString());
                        } catch(Exception e) {
                            overallScoreAgainst[0] = 0;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return idOf;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String idOf) {
                super.onPostExecute(idOf);
                Log.i("zashel", "1");
                Log.i("zashell", String.valueOf(liked[0]));
                if (namesOnSolution != null) {
                    holder.myName.setText(namesOnSolution.get(position));
                } else {
                    holder.myName.setText("Anonymous user");
                }

                if (liked[0]==0) {
                    holder.likeOn.setVisibility(View.INVISIBLE);
                    holder.likeOff.setVisibility(View.VISIBLE);
                }
                if (liked[0]==1) {
                    holder.likeOn.setVisibility(View.VISIBLE);
                    holder.likeOff.setVisibility(View.INVISIBLE);
                }
                if (disliked[0]==0) {
                    holder.dislikeOn.setVisibility(View.INVISIBLE);
                    holder.dislikeOff.setVisibility(View.VISIBLE);
                }
                if (disliked[0]==1) {
                    holder.dislikeOn.setVisibility(View.VISIBLE);
                    holder.dislikeOff.setVisibility(View.INVISIBLE);
                }
            }
        }
        new loading().execute(type);

            holder.likeOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (liked[0]==0) {
                        holder.likeOff.setVisibility(View.INVISIBLE);
                        holder.likeOn.setVisibility(View.VISIBLE);
                        numberOfSolution.child("liked").child(MainActivity.idValue).setValue("1");
                        numberOfSolution.child("Score For").setValue(String.valueOf(permanentScoreFor[0]+1));
                        profiles.child(idArray.get(position)).child("Score For").setValue(String.valueOf(overallScoreFor[0]+1));
                        new CountDownTimer(1000, 250) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                new loading().execute(number);
                            }

                            @Override
                            public void onFinish() {
                            }
                        }.start();
                        Log.i("asdfg", String.valueOf(liked[0]));
                    }
                }
            });


            holder.likeOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (liked[0]==1) {
                        holder.likeOn.setVisibility(View.INVISIBLE);
                        holder.likeOff.setVisibility(View.VISIBLE);
                        numberOfSolution.child("liked").child(MainActivity.idValue).setValue("0");
                        numberOfSolution.child("Score For").setValue(String.valueOf(permanentScoreFor[0] - 1));
                        profiles.child(idArray.get(position)).child("Score For").setValue(String.valueOf(overallScoreFor[0]-1));
                        new loading().execute(number);
                        Log.i("asdfg", String.valueOf(liked[0]));
                    }
                }
            });


            holder.dislikeOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disliked[0]==0) {
                        holder.dislikeOff.setVisibility(View.INVISIBLE);
                        holder.dislikeOn.setVisibility(View.VISIBLE);
                        numberOfSolution.child("disliked").child(MainActivity.idValue).setValue("1");
                        disliked[0] = 1;
                        numberOfSolution.child("Score Against").setValue(String.valueOf(permanentScoreMinus[0] - 1));
                        profiles.child(idArray.get(position)).child("Score Against").setValue(String.valueOf(overallScoreAgainst[0]-1));
                        new loading().execute(number);
                    }
                }
            });


            holder.dislikeOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disliked[0]==1) {
                        holder.dislikeOff.setVisibility(View.VISIBLE);
                        holder.dislikeOn.setVisibility(View.INVISIBLE);
                        numberOfSolution.child("disliked").child(MainActivity.idValue).setValue("0");
                        numberOfSolution.child("Score Against").setValue(String.valueOf(permanentScoreMinus[0] + 1));
                        profiles.child(idArray.get(position)).child("Score Against").setValue(String.valueOf(overallScoreAgainst[0]+1));
                        new loading().execute(number);
                    }
                }
            });

        return row;
    }
}
