package ca.javajeff.mathpro;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.IntDef;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringDef;
import android.support.annotation.StyleRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.kexanie.library.MathView;

import static android.widget.Toast.LENGTH_SHORT;


    public class MainActivity extends AppCompatActivity implements OlympiadAdapter.OlympiadAdapterOnClickHandler {

        private SwipeRefreshLayout swipeLayout;
        public String[] Data = new String[5];
        public String[] Data2 = new String[5];
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference myRef;
        private RecyclerView mRecyclerView;
        private OlympiadAdapter mOlympiadAdapter;
        private DrawerLayout mDrawerLayout;
        private ActionBarDrawerToggle mToggle;

        private TextView mErrorMessageDisplay;
//        public TextView header;

        private ProgressBar mLoadingIndicator;
        private Toolbar mToolBar;
        private NavigationView navigation;

        public String phone;
        public String email;
        public String accountId;
        public String token = null;

        public TextView id;
        public ImageView idImage;
        public TextView nameOnHeader;
        public static String nameValue = new String();
        public static String idValue = new String();
        private TextView idShow;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();

//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setCustomView(R.layout.abs_layout);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_olympiad);
            navigation = (NavigationView) findViewById(R.id.navigation_view);
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

            mToolBar = (Toolbar) findViewById(R.id.nav_action);
            setSupportActionBar(mToolBar);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
            mDrawerLayout.addDrawerListener(mToggle);
            mToggle.syncState();

//            header = (TextView) findViewById(R.id.header_text);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(layoutManager);



            swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);


            mRecyclerView.setHasFixedSize(true);


            mOlympiadAdapter = new OlympiadAdapter(this);

            mRecyclerView.setAdapter(mOlympiadAdapter);

            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override public void onRefresh() {
                    refreshContent();
                }
            });


            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

            View header=navigation.getHeaderView(0);
            id = (TextView) header.findViewById(R.id.header_text);
            nameOnHeader = (TextView) header.findViewById(R.id.name_header);
            TextView myAccount = (TextView) header.findViewById(R.id.my_account);
            idShow = (TextView) header.findViewById(R.id.id_show);
            idImage = (ImageView) header.findViewById(R.id.header_image);

            idImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAccountInfo();
                }
            });
            myAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAccountInfo();
                }
            });
            id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAccountInfo();
                }
            });
            nameOnHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAccountInfo();
                }
            });
            try {
                token = AccountKit.getCurrentAccessToken().getAccountId();
                Log.i("tokeeen", token);
            } catch (Exception e) {
                token=null;
                Log.i("uff","null");
            }
//            try {
//                Toast.makeText(this, token, Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                Toast.makeText(this, nottoken, Toast.LENGTH_LONG).show();
//            }
            if (token!=null) {
                DatabaseReference idRef = myRef.child("Profiles").child(token);

                    idRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                String nameOn = dataSnapshot.child("Name").getValue().toString();
                                idValue =token;
                                nameValue = nameOn;
                                nameOnHeader.setText(nameOn);
                                id.setText(token);
                            } catch (Exception e) {
                                nameOnHeader.setText("Please add your name in My Account ");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            }
            if (token == null) {
                navigation.setVisibility(View.INVISIBLE);
                nameOnHeader.setText("Please log in to have own Name");
                nameOnHeader.setTextSize(10);
                id.setText("You do not have own ID");
                id.setTextSize(10);
                idValue=null;
                nameValue=null;
//                idShow.setVisibility(View.INVISIBLE);
            }
            final String finalToken = token;
//            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                @Override
//                public void onSuccess(Account account) {
//                    accountId = account.getId();
//                    idValue = accountId;
//                    PhoneNumber phoneNumber = account.getPhoneNumber();
//                    phone = phoneNumber.toString();
//
//                    email = account.getEmail();
//
//                    try {
//                        id.setText(accountId);
//                    } catch (Exception e) {
//                        id.setText("000000");
//                    }
//                }
//
//                @Override
//                public void onError(AccountKitError accountKitError) {
//                    navigation.setVisibility(View.INVISIBLE);
//                    phone = "You need to log in";
//                    email = "You need to log in";
//                    String toastMessage = accountKitError.getErrorType().getMessage();
//                    try {
//                       id.setText(finalToken);
//                    } catch (Exception e) {
//                        id.setText(finalNottoken);
//                    }
//                }
//            });

            navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected( MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_log:
                            AccountKit.logOut();
                            idValue=null;
                            nameValue=null;
                            new CountDownTimer(2000, 250) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                                    startActivity(intent);
                                }
                            }.start();
//                            intent.putExtra("logInfo", "If you need to log in again, please use the same type (phone or email)");
                        case R.id.nav_contests:
                            Intent intentt = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intentt);
//                        case R.id.nav_settings:
//                            final Intent intettt = new Intent(MainActivity.this,SettiingsActivity.class);
                    }
                    return false;
                }
            });

            loadProblemData();
        }


        private void refreshContent(){
             new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOlympiadAdapter.setProblemData(null, null);
                    loadProblemData();
                    swipeLayout.setRefreshing(false);
                }
            }, 2000);
        }
        private void showData(DataSnapshot dataSnapshot, Integer i) {
//              for (DataSnapshot ds: dataSnapshot.getChildren()) {
            String[] types= {"International olympiads", "Olympiads for Junior students", "National olympiads", "Team Selection Tests", "Undergraduate olympiads", "Latest contests"};
            String key = dataSnapshot.getKey();
            Data[i] = types[i];
            Data2[i] = key;
//                    String zadacha = ds.child("uslovie").getValue().toString();
//                    Data[i] += "                 " + zadacha;
//
//                    String Data2 = Data[i];
//                    boolean[] used = new boolean[100100];
//                    for (int j=1;j < Data2.length()-1;j++) {
//                        if (Data2.charAt(j) == '$' && Data2.charAt(j+1) != '$' && Data2.charAt(j-1) != '$') {
//                            used[j] = true;
//                        }
//                    }
//                    String DataNew = null;
//                    int k=0;
//                    for(int j = 0;j < Data2.length();j ++) {
//                        if(used[j]) {
//                            if (k==0) {
//                                DataNew += "\\(";
//                                k =1;
//                            }
//                            else {
//                                DataNew +="\\)";
//                                k=0;
//                            }
//                        }
//                        else{
//                            DataNew += Data2.charAt(j);
//                        }
//                    }
//                    Data[i] = DataNew + "\r\n";
            Log.i("dfsdf", Data[i]);
//                }
            new CountDownTimer(4000, 250) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if (Data != null) {
                        showProblemDataView();
                        mOlympiadAdapter.setProblemData(Data, Data2 );
                    } else {
                        showErrorMessage();
                    }
                }
            }.start();
        }

        public void firebaseData() {
            String[] typesForBase = {"international", "junior", "national", "tst", "undergraduate"};
            mLoadingIndicator.setVisibility(View.VISIBLE);
            for (int index = 0; index < 5; index ++) {
                DatabaseReference indexx = myRef.child(typesForBase[index]);// types of olympiad
//                    DatabaseReference indexxx = indexx.child(String.valueOf(0));//names of olympiad
//                    DatabaseReference indexxxx = indexxx.ch1ild(String.valueOf(1));//year of olympiad
                if (0 == 0) {
                    final int finalIndex = index;

                    indexx.addValueEventListener(new ValueEventListener() {
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
        }

        /**
         * This method will get the user's preferred location for problem, and then tell some
         * background method to get the problem data in the background.
         */
        private void loadProblemData() {
            showProblemDataView();
            String location = "Kazakhstan, Almaty";
//            new FetchProblemTask().execute(location);
            firebaseData();
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


        public void openAccountInfo() {
            Intent intent = new Intent(this, AcccountInfo.class);
            intent.putExtra("id", token);
            startActivity(intent);
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

//        public class FetchProblemTask extends AsyncTask<String, Void, String[]> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                mLoadingIndicator.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected String[] doInBackground(String... params) {
//
//            /* If there's no zip code, there's nothing to look up. */
//                if (params.length == 0) {
//                    return null;
//                }
//
////                String location = params[0];
////                URL problemRequestUrl = NetworkUtils.buildUrl(location);
////                Log.v("fsffs", problemRequestUrl.toString());
////                try {
////                    String jsonProblemResponse = NetworkUtils
////                            .getResponseFromHttpUrl(problemRequestUrl);
////
////                    String[] simpleJsonProblemData = OpenProblemJsonUtils
////                            .getSimpleProblemStringsFromJson(MainActivity.this, jsonProblemResponse);
////                    Log.v("ffdf", simpleJsonProblemData.toString());
////                    return simpleJsonProblemData;
////
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    return null;
////                }
//                return Data;
//            }
//            @Override
//            protected void onPostExecute(String[] problemData) {
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
//                if (problemData != null) {
//                    showProblemDataView();
//                    mOlympiadAdapter.setProblemData(problemData);
//                } else {
//                    showErrorMessage();
//                }
//            }
//        }

//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
//            MenuInflater inflater = getMenuInflater();
//        /* Use the inflater's inflate method to inflate our menu layout to this menu */
//            inflater.inflate(R.menu.settings, menu);
//        /* Return true so that the menu is displayed in the Toolbar */
//            return true;
//        }
//
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (mToggle.onOptionsItemSelected(item)) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
