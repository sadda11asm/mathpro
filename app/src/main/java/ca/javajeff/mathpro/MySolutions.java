package ca.javajeff.mathpro;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

/**
 * Created by Саддам on 20.08.2017.
 */

public class MySolutions extends SwipeBackActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.my_solutions);

        setDragEdge(SwipeBackLayout.DragEdge.LEFT);

    }
}
