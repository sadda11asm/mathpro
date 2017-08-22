package ca.javajeff.mathpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by Саддам on 24.07.2017.
 */

public class NameActivity extends SwipeBackActivity implements NameAdapter.NameAdapterOnClickHandler {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference IntRef;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private TextView mTypeDisplay;
    private ProgressBar mLoadingIndicator;
    private NameAdapter mNameAdapter;

    private ArrayList<String> Data = new ArrayList<String>();
    private ArrayList<String> Data2;

    private String keyof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        IntRef = mFirebaseDatabase.getReference();


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_olympiad2);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display2);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mRecyclerView.setHasFixedSize(true);

        mNameAdapter = new NameAdapter(this);

        mTypeDisplay = (TextView) findViewById(R.id.textV);

        mRecyclerView.setAdapter(mNameAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator2);


        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        Bundle bundle = getIntent().getExtras();
        final String type = OlympiadAdapter.type;
        final String keyFromType = bundle.getString("key1");
        if (keyFromType!=null) {
            Log.i("keyy", OlympiadAdapter.key);
        }
        mTypeDisplay.setText(type);
        keyof = keyFromType;
        loadProblemData(OlympiadAdapter.key);
    }

    private void showData(DataSnapshot dataSnapshot) {
              for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    Data.add(key);
                    Log.i("dfsdf", key);
              }
        if (Data != null) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            showProblemDataView();
            mNameAdapter.setProblemData(Data, keyof);
        } else {
            showErrorMessage();
        }
    }

    public void firebaseData(final String keyFromType) {
//        String[] typesForBase = {"international", "junior", "national", "tst", "undergraduate"};
        mLoadingIndicator.setVisibility(View.VISIBLE);
        if (keyFromType != null) {
            DatabaseReference scaleOfOlympiad = IntRef.child(OlympiadAdapter.key);
//        for (int index = 0; index < 35; index ++) {
//            DatabaseReference indexx = scaleOfOlympiad.child(typesForBase[index]);// types of olympiad
////                    DatabaseReference indexxx = indexx.child(String.valueOf(0));//names of olympiad
////                    DatabaseReference indexxxx = indexxx.ch1ild(String.valueOf(1));//year of olympiad
//            if (0 == 0) {
//                final int finalIndex = index;

            scaleOfOlympiad.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    showData(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//            }
//        }
        } else {
            Toast.makeText(this,"Please wait... There is delay due to bad internet connection", Toast.LENGTH_LONG);
//          final String key=keyof;
            loadProblemData(OlympiadAdapter.key);
        }
    }

    private void loadProblemData(String keyFromType) {
        if (keyFromType == null) {
            keyFromType=keyof;
        }
        showProblemDataView();
        firebaseData(keyFromType);
    }


    private void showProblemDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the problem data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.settings, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mNameAdapter.setProblemData(null, null);
            loadProblemData(keyof);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String problemForDay) {
//
    }
}
