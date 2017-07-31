package ca.javajeff.mathpro;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 30.07.2017.
 */

public class ProblemActivity extends Activity {

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private TextView mYearDisplay;
    private MathView mProblemDisplay;

    private String problem;
    private String year;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_problem);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_displayy);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicatorr);
        mYearDisplay = (TextView) findViewById(R.id.text_year);
        mProblemDisplay = (MathView) findViewById(R.id.text_problem);

        Bundle bundle = getIntent().getExtras();

        String problem = bundle.getString("problem");
        String year = bundle.getString("year");

        mYearDisplay.setText(year);
        mProblemDisplay.setText(problem);
    }
}
