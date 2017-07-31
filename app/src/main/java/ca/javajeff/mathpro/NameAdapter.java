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

/**
 * Created by Саддам on 25.07.2017.
 */

public class NameAdapter  extends RecyclerView.Adapter<NameAdapter.NameAdapterViewHolder> {
    private ArrayList<String> mProblemData = new ArrayList<String>();

    private final NameAdapterOnClickHandler mClickHandler;
    private String type;


    public interface NameAdapterOnClickHandler {

        void onClick(String problemForDay);
    }

    public NameAdapter(NameAdapterOnClickHandler ClickHandler) {
        mClickHandler = ClickHandler;
    }

    public class NameAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {





        TextView SecondFormula;

        public NameAdapterViewHolder(View view, Context context) {
            super(view);


            SecondFormula = (TextView) view.findViewById(R.id.formula_two2);
            view.setOnClickListener(this);
            // TODO (7) Call setOnClickListener on the view passed into the constructor (use 'this' as the OnClickListener)
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Context context = v.getContext();
            final Intent intent;
            intent = new Intent(context, ListActivity.class);
            intent.putExtra("name", mProblemData.get(adapterPosition));
            intent.putExtra("type", type);
            String problemForDay = mProblemData.get(adapterPosition);
            mClickHandler.onClick(problemForDay);
            context.startActivity(intent);
        }

        // TODO (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method
    }


    @Override
    public NameAdapter.NameAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.name_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new NameAdapter.NameAdapterViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(NameAdapter.NameAdapterViewHolder nameAdapterViewHolder, int position) {
        String problemForThisDay = mProblemData.get(position);
        //olympiadAdapterViewHolder.formula.setText(problemorThisDay);
        Log.i("fsdvs", problemForThisDay);
        nameAdapterViewHolder.SecondFormula.setText(problemForThisDay);



    }

    @Override
    public int getItemCount() {
        if (null == mProblemData) return 0;
        return mProblemData.size();
    }

    public void setProblemData(ArrayList<String> problemData, String parentType) {
        mProblemData = problemData;
        type = parentType;
        notifyDataSetChanged();
    }

}
