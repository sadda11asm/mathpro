package ca.javajeff.mathpro;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Саддам on 24.07.2017.
 */

public class NameActivity extends Activity implements NameAdapter.NameAdapterOnClickHandler {
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

//        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        IntRef = mFirebaseDatabase.getReference();


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_olympiad2);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display2);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mNameAdapter = new NameAdapter(this);

        mTypeDisplay = (TextView) findViewById(R.id.textV);

        mRecyclerView.setAdapter(mNameAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator2);

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("type");
        String keyFromType = bundle.getString("key1");

        keyof = keyFromType;
        loadProblemData(keyFromType);
        mTypeDisplay.setText(type);
    }

    private void showData(DataSnapshot dataSnapshot) {
              for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    Data.add(key);
                    Log.i("dfsdf", key);
              }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (Data != null) {
            showProblemDataView();
            mNameAdapter.setProblemData(Data, keyof);
        } else {
            showErrorMessage();
        }
    }

    public void firebaseData(String keyFromType) {
//        String[] typesForBase = {"international", "junior", "national", "tst", "undergraduate"};
        mLoadingIndicator.setVisibility(View.VISIBLE);
        DatabaseReference scaleOfOlympiad = IntRef.child(keyFromType);
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
    }

    private void loadProblemData(String keyFromType) {
        showProblemDataView();
        String location = "Kazakhstan, Almaty";
//            new FetchProblemTask().execute(location);
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
