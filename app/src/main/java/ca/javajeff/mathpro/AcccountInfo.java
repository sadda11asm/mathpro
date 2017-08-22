package ca.javajeff.mathpro;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

/**
 * Created by Саддам on 14.08.2017.
 */

public class AcccountInfo extends SwipeBackActivity {
    public TextView ownId;
    public TextView ownName;
    public TextView ownCountry;
    public TextView ownScore;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    public String theName;
    public String country;
    public String score;

    public String scoreFor;
    public String scoreAgainst;
    public String ID;
    public EditText enterName;
    public EditText enterCountry;
    private Button addCountry;
    private Button addName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        addName = (Button) findViewById(R.id.add_name);
        addCountry = (Button) findViewById(R.id.add_country);
        enterName = (EditText) findViewById(R.id.enter_name);
        enterCountry = (EditText) findViewById(R.id.enter_country);
        ownId = (TextView) findViewById(R.id.own_id);
        ownName = (TextView) findViewById(R.id.own_name);
        ownCountry = (TextView) findViewById(R.id.own_country);
        ownScore = (TextView) findViewById(R.id.own_score);

        addCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCountry();
                enterCountry.setText("");
            }
        });

        addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addName();
                enterName.setText("");
            }
        });

        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("id");
        if (ID != null) {
            ownId.setText(ID);
        } else {
            ownId.setText("You must login to have own ID");
            ownId.setTextSize(13);
        }
        setData();

    }
    public void addCountry() {
        DatabaseReference profiles = myRef.child("Profiles");
        DatabaseReference idRef = profiles.child(ID);
        String currentCountry = enterCountry.getText().toString();
        idRef.child("Country").setValue(currentCountry);
        ownCountry.setText(currentCountry);
        ownCountry.setVisibility(View.VISIBLE);
        enterCountry.setVisibility(View.INVISIBLE);
    }

    public void addName() {
        DatabaseReference profiles = myRef.child("Profiles");
        DatabaseReference idRef = profiles.child(ID);
        String currentName = enterName.getText().toString();
        idRef.child("Name").setValue(currentName);
        ownName.setText(currentName);
        ownName.setVisibility(View.VISIBLE);
        enterName.setVisibility(View.INVISIBLE);
    }
    private void setData() {
        DatabaseReference profiles = myRef.child("Profiles");
        if (ID!=null) {
            DatabaseReference idRef = profiles.child(ID);
            DatabaseReference nameRef = idRef.child("Name");
            DatabaseReference countryRef = idRef.child("Country");
            DatabaseReference scoreRef = idRef.child("Score");
            idRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        theName = dataSnapshot.child("Name").getValue().toString();
                    } catch (Exception e) {
                        theName = null;
                    }
                    try {
                        country = dataSnapshot.child("Country").getValue().toString();
                    } catch (Exception e) {
                        country = null;
                    }
                    try {
                        scoreFor = dataSnapshot.child("Score For").getValue().toString();
                        scoreAgainst = dataSnapshot.child("Score Against").getValue().toString();
                        score = String.valueOf (Integer.parseInt(scoreFor) + Integer.parseInt(scoreAgainst));
                    } catch (Exception e) {
                        score = null;
                    }

                    if (theName != null) {
                        ownName.setText(theName);
                        ownName.setVisibility(View.VISIBLE);
                        enterName.setVisibility(View.INVISIBLE);
                        addName.setVisibility(View.INVISIBLE);
                    }  else {
                        enterName.setVisibility(View.VISIBLE);
                        addName.setVisibility(View.VISIBLE);
                        ownName.setVisibility(View.INVISIBLE);
                    }

                    if (score != null) {
                        ownScore.setText(score);
                    }  else {
                        ownScore.setText("0");
                    }
                    if (country != null) {
                        ownCountry.setText(country);
                        ownCountry.setVisibility(View.VISIBLE);
                        enterCountry.setVisibility(View.INVISIBLE);
                        addCountry.setVisibility(View.INVISIBLE);
                    }  else {
                        enterCountry.setVisibility(View.VISIBLE);
                        addCountry.setVisibility(View.VISIBLE);
                        ownCountry.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            ownName.setText("You need to log in");
            ownName.setTextSize(13);
            ownCountry.setText("You need to log in");
            ownCountry.setTextSize(13);
            ownScore.setText("You must log in to have own Score");
            ownScore.setTextSize(13);
            ownName.setVisibility(View.VISIBLE);
            enterName.setVisibility(View.INVISIBLE);
            addName.setVisibility(View.INVISIBLE);
            ownCountry.setVisibility(View.VISIBLE);
            enterCountry.setVisibility(View.INVISIBLE);
            addCountry.setVisibility(View.INVISIBLE);
        }

    }
}
