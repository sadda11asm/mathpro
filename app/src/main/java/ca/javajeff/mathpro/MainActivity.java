package ca.javajeff.mathpro;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.kexanie.library.MathView;

import static android.widget.Toast.LENGTH_SHORT;


    public class MainActivity extends AppCompatActivity implements OlympiadAdapter.OlympiadAdapterOnClickHandler {


        public String[] Data = new String[7];
        public String olympiad;
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference myRef;
        private RecyclerView mRecyclerView;
        private OlympiadAdapter mOlympiadAdapter;

        private TextView mErrorMessageDisplay;

        private ProgressBar mLoadingIndicator;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();


        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_olympiad);

        /* This TextView is used to display errors and will be hidden if there are no errors */
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
            mRecyclerView.setHasFixedSize(true);

        /*
         * The ProblemAdapter is responsible for linking our problem data with the Views that
         * will end up displaying our problem data.
         */

            mOlympiadAdapter = new OlympiadAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
            mRecyclerView.setAdapter(mOlympiadAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the problem data. */
            loadProblemData();
        }

        /**
         * This method will get the user's preferred location for problem, and then tell some
         * background method to get the problem data in the background.
         */
        private void loadProblemData() {
            showProblemDataView();

            String location = "Kazakhstan, Almaty";
            new FetchProblemTask().execute(location);
        }
        @Override
        public void onClick (String problemForDay) {
            //Context context = this;
            //Toast.makeText(context, problemForDay, Toast.LENGTH_SHORT).show();
        }


        /**
         * This method will make the View for the problem data visible and
         * hide the error message.
         * <p>
         * Since it is okay to redundantly set the visibility of a View, we don't
         * need to check whether each view is currently visible or invisible.
         */
        private void showProblemDataView() {
        /* First, make sure the error is invisible */
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the problem data is visible */
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        /**
         * This method will make the error message visible and hide the problem
         * View.
         * <p>
         * Since it is okay to redundantly set the visibility of a View, we don't
         * need to check whether each view is currently visible or invisible.
         */
        private void showErrorMessage() {
        /* First, hide the currently visible data */
            mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

        public class FetchProblemTask extends AsyncTask<String, Void, String[]> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
                if (params.length == 0) {
                    return null;
                }
                for (int index = 0; index < 6; index ++) {
                    DatabaseReference indexx = myRef.child(String.valueOf(index));// types of olympiad
                    DatabaseReference indexxx = indexx.child(String.valueOf(0));//names of olympiad
                    DatabaseReference indexxxx = indexxx.child(String.valueOf(1));//year of olympiad
                    if (0 == 0) {
                        final int finalIndex = index;

                        indexxxx.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                showData(dataSnapshot, finalIndex);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

//                String location = params[0];
//                URL problemRequestUrl = NetworkUtils.buildUrl(location);
//                Log.v("fsffs", problemRequestUrl.toString());
//                try {
//                    String jsonProblemResponse = NetworkUtils
//                            .getResponseFromHttpUrl(problemRequestUrl);
//
//                    String[] simpleJsonProblemData = OpenProblemJsonUtils
//                            .getSimpleProblemStringsFromJson(MainActivity.this, jsonProblemResponse);
//                    Log.v("ffdf", simpleJsonProblemData.toString());
//                    return simpleJsonProblemData;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
                return Data;
            }

            @Override
            protected void onPostExecute(String[] problemData) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (problemData != null) {
                    showProblemDataView();
                    mOlympiadAdapter.setProblemData(problemData);
                } else {
                    showErrorMessage();
                }
            }
            private void showData(DataSnapshot dataSnapshot, Integer i) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String zadacha = ds.child("uslovie").getValue().toString();
                    Data[i] += "                 " + zadacha;

                    String Data2 = Data[i];
                    boolean[] used = new boolean[100100];
                    for (int j=1;j < Data2.length()-1;j++) {
                        if (Data2.charAt(j) == '$' && Data2.charAt(j+1) != '$' && Data2.charAt(j-1) != '$') {
                            used[j] = true;
                        }
                    }
                    String DataNew = null;
                    int k=0;
                    for(int j = 0;j < Data2.length();j ++) {
                        if(used[j]) {
                            if (k==0) {
                                DataNew += "\\(";
                                k =1;
                            }
                            else {
                                DataNew +="\\)";
                                k=0;
                            }
                        }
                        else{
                            DataNew += Data2.charAt(j);
                        }
                    }
                    Data[i] = DataNew + "\r\n";
                    Log.i("dfsdf", zadacha);
                }
            }
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
                mOlympiadAdapter.setProblemData(null);
                loadProblemData();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    }
