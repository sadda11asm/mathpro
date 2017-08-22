package ca.javajeff.mathpro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Саддам on 20.08.2017.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingAdapterViewHolder>  {

    private String[] mNameData;
    private int[] mRatingData;
    private final RatingAdapterOnClickHandler mClickHandler;


    public interface RatingAdapterOnClickHandler {

        void onClick(String problemForDay);
    }

    public RatingAdapter(RatingAdapterOnClickHandler ClickHandler) {
        mClickHandler = ClickHandler;
    }

    public static class RatingAdapterViewHolder extends RecyclerView.ViewHolder{

        public static TextView mTextView;

        static TextView rating;

        public RatingAdapterViewHolder(View view, Context context) {
            super(view);

            mTextView= (TextView) view.findViewById(R.id.name);
            rating = (TextView) view.findViewById(R.id.score);
        }
    }

    @Override
    public RatingAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rating_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RatingAdapter.RatingAdapterViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RatingAdapterViewHolder holder, int position) {
        String nameFor = mNameData[position];
        int score = mRatingData[position];
        RatingAdapterViewHolder.mTextView.setText(nameFor);
        RatingAdapterViewHolder.rating.setText(String.valueOf(score));
    }

    @Override
    public int getItemCount() {
        if (null == mNameData) return 0;
        return mNameData.length;
    }

    public void setProblemData(String[] problemData, int[] problemData2) {
        mNameData = problemData;
        mRatingData = problemData2;
        notifyDataSetChanged();
    }
}
