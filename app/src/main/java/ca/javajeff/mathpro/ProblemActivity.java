package ca.javajeff.mathpro;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 30.07.2017.
 */

public class ProblemActivity extends SwipeBackActivity {

    private GestureDetectorCompat gesture;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private TextView mYearDisplay;
    private MathView mProblemDisplay;
    private EditText enterSolution;
    private Button submitSolution;
    private String predSolution;
    private int numberOf = 0;
    private String problem;
    private ArrayList<String> previousNames = new ArrayList<>();
    private ArrayList<String> previousSolutions = new ArrayList<>();
    private ArrayList<String> previousIds = new ArrayList<>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ProRef;
    private boolean proshel;
    private SwipeRefreshLayout swipeLayout;
    private ListView mListView;
    private ScrollView mScrollView;
    private String namee;
    private String typee;
    private String yearr;
    private String numberr;
    static public ArrayList<Integer> liked=new ArrayList<Integer>();
    static public ArrayList<Integer> disliked=new ArrayList<Integer>();
    static public ArrayList<String> scoreFor=new ArrayList<String>();
    static public ArrayList<String> scoreAgainst=new ArrayList<String>();
    static public ArrayList<String> overallScoreFor=new ArrayList<String>();
    static public ArrayList<String> overallScoreAgainst=new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_problem);

        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        ProRef = mFirebaseDatabase.getReference();
//        gesture = new GestureDetectorCompat(this, LearnGesture())
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_displayy);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicatorr);
        mYearDisplay = (TextView) findViewById(R.id.text_year);
        mProblemDisplay = (MathView) findViewById(R.id.text_problem);
        enterSolution = (EditText) findViewById(R.id.solution_enter);
        submitSolution = (Button) findViewById(R.id.solution_submit);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mListView = (ListView) findViewById(R.id.list_solution);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        setListViewHeightBasedOnChildren(mListView);

//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ref);

//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override public void onRefresh() {
//                refreshContent();
//            }
//        });

        Bundle bundle = getIntent().getExtras();

        final String problem = bundle.getString("problem");
        final String year = bundle.getString("year");
        final String name = bundle.getString("name");
        final String type = bundle.getString("type");
        final String number = bundle.getString("number");

        yearr = year;
        numberr=number;
        typee=type;
        namee=name;
        DatabaseReference ProSolution = ProRef.child("Solutions");
        final DatabaseReference profiles = ProRef.child("Profiles");
        final DatabaseReference SolutionForProblem = ProSolution.child(type).child(name).child(year).child(number);

        if(MainActivity.idValue==null) {
            Toast.makeText(this, "You must register or log in to upvote and downvote others' solutions", Toast.LENGTH_LONG).show();
        }

        class loadingg extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... params) {
                SolutionForProblem.child("number").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showData2(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                SolutionForProblem.child("solution").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        previousIds.clear();
                        previousNames.clear();
                        previousSolutions.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            try {
                                previousSolutions.add(ds.child("solution").getValue().toString());
                            } catch(Exception e) {
                                previousSolutions.add("loading...");
                            }
                            try {
                                previousNames.add(ds.child("name").getValue().toString());
                            } catch(Exception e) {
                                previousNames.add("loading...");
                            }
                            try {
                                previousIds.add(ds.child("id").getValue().toString());
                            } catch(Exception e) {
                                previousIds.add("0");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return number;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String idOf) {
                Log.i("vPostExecute", "1");
                super.onPostExecute(idOf);
                SolutionAdapter solutionAdapter = new SolutionAdapter(ProblemActivity.this, previousSolutions.size(), previousSolutions, previousNames, previousIds, type, name, year, number);
                mListView.setAdapter(solutionAdapter);
                mListView.setItemsCanFocus(true);
//                justifyListViewHeightBasedOnChildren(mListView);
            }
        }

        mYearDisplay.setText(year);
        mProblemDisplay.setText(problem);
        if (MainActivity.idValue != null) {
            enterSolution.setHint("Please enter your own solution");
        } else {
            enterSolution.setHint("Please enter your name in My Account to enter own solutions with your name or just log in");
            submitSolution.setVisibility(View.INVISIBLE);
        }
        enterSolution.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    submitSolution.setEnabled(false);
                } else {
                    submitSolution.setEnabled(true);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                submitSolution.setEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        enterSolution.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
        new loadingg().execute(String.valueOf(numberOf));
//        justifyListViewHeightBasedOnChildren(mListView);
        Log.i("zdesIds", previousIds.toString());



        submitSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String solution = enterSolution.getText().toString();
                numberOf++;
                SolutionForProblem.child("number").setValue(String.valueOf(numberOf));
                SolutionForProblem.child("solution").child(String.valueOf(numberOf)).child("name").setValue(MainActivity.nameValue);
                SolutionForProblem.child("solution").child(String.valueOf(numberOf)).child("id").setValue(MainActivity.idValue);
                SolutionForProblem.child("solution").child(String.valueOf(numberOf)).child("solution").setValue(solution);
                SolutionForProblem.child("solution").child(String.valueOf(numberOf)).child("Score For").setValue("0");
                SolutionForProblem.child("solution").child(String.valueOf(numberOf)).child("Score Against").setValue("0");
                new loadingg().execute(number);
                enterSolution.setText("");
                }
        });


    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
    private void refreshContent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 2000);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    private void showData2(DataSnapshot dataSnapshot) {
        try {
            numberOf = Integer.parseInt(dataSnapshot.getValue().toString());
        } catch (Exception e) {
            numberOf = 0;
        }
    }
}
