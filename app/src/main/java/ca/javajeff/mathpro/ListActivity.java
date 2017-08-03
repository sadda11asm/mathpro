package ca.javajeff.mathpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Саддам on 28.07.2017.
 */

public class ListActivity extends Activity implements LlistAdapter.ListAdapterOnClickHandler {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ProRef;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private YearAdapter mYearAdapter;
    private TextView mYearDisplay;
    private Spinner mSpinner;
    List<String> spinnerArray =  new ArrayList<String>();
    private ArrayList<String> Data = new ArrayList<String>();
    private ArrayList<String> Data2 = new ArrayList<String>();
    private ListView list;
    private SwipeRefreshLayout swipeLayout;
    private String keyofName;
    private String keyofType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year2);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        ProRef = mFirebaseDatabase.getReference();

        mYearDisplay = (TextView) findViewById(R.id.text2);
        mSpinner = (Spinner) findViewById(R.id.spinner2);
        list = (ListView) findViewById(R.id.list_view3);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display4);


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                refreshContent();
            }
        });


        Resources res = getResources();

        Bundle bundle = getIntent().getExtras();
//        String type = bundle.getString("type");
        String keyFromName = bundle.getString("name");
        String keyFromType = bundle.getString("type");
        keyofName = keyFromName;
        keyofType = keyFromType;
        Data2 = loadProblemData(keyFromName, keyFromType);


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator4);

    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Data2 = loadProblemData(keyofName, keyofType);
                swipeLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private ArrayList<String> showData(DataSnapshot dataSnapshot) {
        mYearDisplay.setText(keyofName);
        for (DataSnapshot ds: dataSnapshot.getChildren()) {
            String key = ds.getKey();
            Data.add(key);
            Log.i("dfsdf", key);
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (Data2 != null) {
            showProblemDataView();
        } else {
            showErrorMessage();
        }
        SpinnerData();
        return Data2;

    }
    private void showData2(DataSnapshot dataSnapshot, String selected) {
        if (dataSnapshot.getValue() != null) {
            String value = dataSnapshot.getValue().toString();
            String DataI = value;
            boolean[] used = new boolean[100100];
            boolean[] doubleUsed = new boolean[100100];
            for (int j=1;j < DataI.length()-1;j++) {
                if (DataI.charAt(j) == '$' && DataI.charAt(j+1) != '$' && DataI.charAt(j-1) != '$') {
                    used[j] = true;
                }
                if (DataI.charAt(j) == '$' && DataI.charAt(j+1) == '$') {
                    doubleUsed[j] = true;
                }
            }
            String DataNew = "";
            int k=0;
            int m =0;
            int pred = -2;
            int predplus = -5;
            for(int j = 0; j < DataI.length(); j++) {
                int notDefault = 0;
                if(used[j]) {
                    notDefault=1;
                    if (k==0) {
                        DataNew += "\\(";
                        k =1;
                    }
                    else {
                        DataNew +="\\)";
                        k=0;
                    }
                }
//                if (doubleUsed[j]) {
//                    notDefault=1;
//                    if (m==0) {
//                        DataNew +="\\(";
//                        m=1;
//                    }
//                    else {
//                        DataNew +="\\)";
//                        m=0;
//                    }
//                }
                String linebreak = "\n";
                if (j< (DataI.length()-1) && DataI.substring(j,j+2)==linebreak) {
                    notDefault=1;
                    DataNew+="";
                    pred = j;
                }
                if (j>0 && pred == j-1) {
                    notDefault=1;
                    DataNew+="";
                }
//                String plus = "\\plus";
//                if (j<(DataI.length()-5) && DataI.substring(j,j+5) == plus ) {
//                    notDefault=1;
//                    predplus=j;
//                    DataNew+=" + ";
//                }
//                if (predplus == j-1 || predplus == j-2 || predplus == j-3 || predplus == j-4) {
//                    notDefault=1;
//                    DataNew+="";
//                }
                if (notDefault==0) {
                    DataNew += DataI.charAt(j);
                }

            }
            value = DataNew;
            Data2.add(value);
            Log.i("dfsdfh", value);
        } else {
            Log.i("qwerty", dataSnapshot.getRef().toString());
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (Data2 != null) {
            showProblemDataView();
        } else {
            showErrorMessage();
        }
    }

    public ArrayList<String> firebaseData(String keyFromName, String keyFromType) {
//        String[] typesForBase = {"international", "junior", "national", "tst", "undergraduate"};
        DatabaseReference scaleOfOlympiad = ProRef.child(keyFromType).child(keyFromName);

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
        return Data2;
    }

    public ArrayList<String> firebaseData2(String keyFromType, String keyFromName, final String keyFromYear) {
//        String[] typesForBase = {"international", "junior", "national", "tst", "undergraduate"};
        mLoadingIndicator.setVisibility(View.VISIBLE);
        Data2.clear();
        DatabaseReference scaleOfOlympiad = ProRef.child(keyFromType).child(keyFromName).child(keyFromYear);
        Log.i("dee", scaleOfOlympiad.getRef().toString());
        for (int index = 0; index < 100; index ++) {
            DatabaseReference indexx = scaleOfOlympiad.child(String.valueOf(index)).child("uslovie");// types of olympiad

            indexx.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    showData2(dataSnapshot, keyFromYear);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return Data2;
    }
    private ArrayList<String> loadProblemData(String keyFromName, String keyFromType) {
        showProblemDataView();
//        String location = "Kazakhstan, Almaty";
//            new FetchProblemTask().execute(location);
        firebaseData(keyFromName, keyFromType);
        return Data2;
    }


    private void showProblemDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the problem data is visible */
        list.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        /* First, hide the currently visible data */
        list.setVisibility(View.INVISIBLE);
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
            mYearAdapter.setProblemData(null, null);
            loadProblemData(keyofName, keyofType);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private ArrayList<String> SpinnerData() {
        Log.i("hhh", String.valueOf(Data.size()));
        for (int index =Data.size()-1; index> -1;index--) {
            spinnerArray.add(Data.get(index));
            Log.i("hhh", Data.get(index));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
//        mSpinner.setDropDownWidth(370);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, int pos, long id) {
                final String selected = mSpinner.getSelectedItem().toString();
                firebaseData2(keyofType, keyofName, selected);
                new CountDownTimer(4000, 250) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        list.setVisibility(View.INVISIBLE);
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        list.setVisibility(View.VISIBLE);
                        LlistAdapter llistAdapter = new LlistAdapter(ListActivity.this,1, Data2, ListActivity.this, selected);
                        list.setAdapter(llistAdapter);
                        list.setItemsCanFocus(true);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v,int position, long arg3)
                            {
                                Log.i("fwfsf", "fssf");
                                Context context = view.getContext();
                                final Intent intent;
                                intent = new Intent(context, ProblemActivity.class);
                                intent.putExtra("problem", Data2.get(position));
                                intent.putExtra("year", selected);
                                context.startActivity(intent);
                            }
                        });
                    }
                }.start();
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
                // Do nothing.
            }
        });
        return Data2;
    }

    @Override
    public void onClick(String problemForDay) {

    }
}


