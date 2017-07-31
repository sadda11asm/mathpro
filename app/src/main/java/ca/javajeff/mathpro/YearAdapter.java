package ca.javajeff.mathpro;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 26.07.2017.
 */

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.YearAdapterViewHolder> {
    private ArrayList<String> mProblemData = new ArrayList<String>();

    private final YearAdapter.YearAdapterOnClickHandler mClickHandler;
    private String year;


    public interface YearAdapterOnClickHandler {

        void onClick(String problemForDay);
    }

    public YearAdapter(YearAdapter.YearAdapterOnClickHandler ClickHandler) {
        mClickHandler = ClickHandler;
    }

    public class YearAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {





        MathView SecondFormula;

        public YearAdapterViewHolder(View view, Context context) {
            super(view);


            SecondFormula = (MathView) view.findViewById(R.id.formula_two3);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Context context = v.getContext();
            final Intent intent;
            intent = new Intent(context, ProblemActivity.class);
            intent.putExtra("problem", mProblemData.get(adapterPosition));
            intent.putExtra("year", year);
            String problemForDay = mProblemData.get(adapterPosition);
            mClickHandler.onClick(problemForDay);
            context.startActivity(intent);
        }
    }

    @Override
    public YearAdapter.YearAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.year_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new YearAdapter.YearAdapterViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(YearAdapter.YearAdapterViewHolder yearAdapterViewHolder, int position) {
        String problemForThisDay = mProblemData.get(position);
        //olympiadAdapterViewHolder.formula.setText(problemorThisDay);
        Log.i("fsdvs", problemForThisDay);
        try {
            yearAdapterViewHolder.SecondFormula.setText(problemForThisDay);
        } catch (Exception e) {
            yearAdapterViewHolder.SecondFormula.setText("Please, try again later");
        }



    }

    @Override
    public int getItemCount() {
        if (null == mProblemData) return 0;
        return mProblemData.size();
    }

    public void setProblemData(ArrayList<String> problemData, String mYear) {
        mProblemData = problemData;
        year=mYear;
        notifyDataSetChanged();
    }

}
