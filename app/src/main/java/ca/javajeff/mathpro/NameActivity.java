package ca.javajeff.mathpro;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Саддам on 24.07.2017.
 */

public class InternationalActivity extends Activity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference IntRef;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        IntRef = mFirebaseDatabase.getReference();
    }


}
