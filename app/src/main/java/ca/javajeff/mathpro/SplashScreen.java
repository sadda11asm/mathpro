package ca.javajeff.mathpro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Саддам on 21.08.2017.
 */

public class SplashScreen extends Activity {
    String[] Data2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        MainActivity.mFirebaseDatabase = FirebaseDatabase.getInstance();
        MainActivity.myRef = MainActivity.mFirebaseDatabase.getReference();

//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setCustomView(R.layout.abs_layout);

        MainActivity.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_olympiad);
        MainActivity.mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        MainActivity.mRecyclerView.setLayoutManager(layoutManager);




        MainActivity.mRecyclerView.setHasFixedSize(true);


        MainActivity.mOlympiadAdapter = new OlympiadAdapter((OlympiadAdapter.OlympiadAdapterOnClickHandler)this);

        MainActivity.mRecyclerView.setAdapter( MainActivity.mOlympiadAdapter);


        MainActivity.mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        MainActivity.loadProblemData();
        Data2=MainActivity.Data2;
        boolean vse=true;
        for (int i=0;i<5;i++) {
            if (MainActivity.Data2[i] == null) {
                vse = false;
            }
        }
        if (vse) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG);
            connected = false;
        }
    }
}
