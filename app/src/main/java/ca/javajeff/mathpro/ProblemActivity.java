package ca.javajeff.mathpro;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 30.07.2017.
 */

public class ProblemActivity extends Activity {

    private GestureDetectorCompat gesture;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private TextView mYearDisplay;
    private MathView mProblemDisplay;
    private EditText enterSolution;
    private MathView mSolutionDisplay;
    private Button submitSolution;
    private String predSolution;
    private int numberOf = 0;
    private String problem;
    private String year;
    private ArrayList<String> previousSolutions = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ProRef;
    private boolean proshel;
    private SwipeRefreshLayout swipeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_problem);

//        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        ProRef = mFirebaseDatabase.getReference();

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_displayy);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicatorr);
        mYearDisplay = (TextView) findViewById(R.id.text_year);
        mProblemDisplay = (MathView) findViewById(R.id.text_problem);
//        mSolutionDisplay = (MathView) findViewById(R.id.text_solution);
//        enterSolution = (EditText) findViewById(R.id.solution_enter);
//        submitSolution = (Button) findViewById(R.id.solution_submit);
//
//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ref);

//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override public void onRefresh() {
//                refreshContent();
//            }
//        });

        Bundle bundle = getIntent().getExtras();

        String problem = bundle.getString("problem");
        String year = bundle.getString("year");
        String name = bundle.getString("name");
        String type = bundle.getString("type");
        String number = bundle.getString("number");
        DatabaseReference ProSolution = ProRef.child("Solutions");
        final DatabaseReference SolutionForProblem = ProSolution.child(type).child(name).child(year).child(number);
        mYearDisplay.setText(year);
        mProblemDisplay.setText(problem);
//        SolutionForProblem.child("solution").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                showData(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        SolutionForProblem.child("number").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                showData2(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        submitSolution.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (proshel) {
//                    String solution = enterSolution.getText().toString();
//                    numberOf++;
//                    previousSolutions.add(String.valueOf(numberOf) + ". " + solution);
//                    SolutionForProblem.child("number").setValue(numberOf);
//                    for (int i = 0; i < previousSolutions.size(); i++) {
//                        SolutionForProblem.child("solution").child(String.valueOf(i+1)).setValue(previousSolutions.get(i));
//                    }
//                    SolutionForProblem.child("number").setValue(numberOf);
////                  String array2 = previousSolutions.toString();
////                  mSolutionDisplay.setText("");
////                  mSolutionDisplay.setText(array2);
//                    enterSolution.setText("");
//                }
//            }
//
//
//        });


    }

    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void showData(DataSnapshot dataSnapshot) {
        mSolutionDisplay.setText("");
        previousSolutions.clear();
        for (DataSnapshot ds: dataSnapshot.getChildren()) {
            previousSolutions.add(ds.getValue().toString() + "                                       ");
        }

        String array = previousSolutions.toString();
        mSolutionDisplay.setText(array);
        proshel=true;
    }
    private void showData2(DataSnapshot dataSnapshot) {
        try {
            numberOf = Integer.parseInt(dataSnapshot.getValue().toString());
        } catch (Exception e) {
            numberOf = 0;
        }
    }
}
