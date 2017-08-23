package ca.javajeff.mathpro;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.ArrayList;

/**
 * Created by Саддам on 20.08.2017.
 */

public class RatingActivity extends SwipeBackActivity implements RatingAdapter.RatingAdapterOnClickHandler {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private ArrayList<Integer> scores = new ArrayList<Integer>();
    private ArrayList<String> names = new ArrayList<String>();
    private int[] score;
    private String[] name;
    private RatingAdapter mRatingAdapter;
    private RecyclerView mRecyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_rating);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRatingAdapter = new RatingAdapter(this);

        mRecyclerView.setAdapter(mRatingAdapter);

        loadRatingData();

    }

    public void loadRatingData() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        myRef.child("Profiles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    try {
                        scores.add(Integer.parseInt(ds.child("Score For").getValue().toString()) + (Integer.parseInt(ds.child("Score Against").getValue().toString())));
                    } catch (Exception e) {
                        scores.add(0);
                    }
                    try {
                        names.add(ds.child("Name").getValue().toString());
                    } catch (Exception e) {
                        names.add("Anonymous user");
                    }
                }
                score = new int[scores.size()];
                name = new String[names.size()];
                for (int i=0; i<scores.size();i++) {
                    score[i]=scores.get(i);
                    name[i]=names.get(i);
                }
                for (int i=0; i<score.length-1;i++) {
                    for (int j=i+1;j<score.length;j++) {
                        if (score[i]<score[j]) {
                            int c=score[i];
                            score[i]= score[j];
                            score[j]= c;
                            String b= name[i];
                            name[i]= name[j];
                            name[j]= b;
                        }
                    }
                }

                mRatingAdapter.setProblemData(name, score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(String problemForDay) {

    }
}
