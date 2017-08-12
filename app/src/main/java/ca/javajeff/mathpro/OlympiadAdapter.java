package ca.javajeff.mathpro;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * Created by Саддам on 21.07.2017.
 */

public class OlympiadAdapter  extends RecyclerView.Adapter<OlympiadAdapter.OlympiadAdapterViewHolder> {
    private String[] mProblemData;
    private String[] mProblemData2;
    private ImageView intern;

    private final OlympiadAdapterOnClickHandler mClickHandler;


    public interface OlympiadAdapterOnClickHandler {

        void onClick(String problemForDay);
    }

    public OlympiadAdapter(OlympiadAdapterOnClickHandler ClickHandler) {
        mClickHandler = ClickHandler;
    }

    /**
     * Cache of the children views for a olympiad list item.
     */
    public class OlympiadAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {





        TextView SecondFormula;

        public OlympiadAdapterViewHolder(View view, Context context) {
            super(view);

            intern = (ImageView) view.findViewById(R.id.imageView3);
            SecondFormula = (TextView) view.findViewById(R.id.formula_two);
            view.setOnClickListener(this);
            // TODO (7) Call setOnClickListener on the view passed into the constructor (use 'this' as the OnClickListener)
        }
        @Override
        public void onClick(View v) {
            try {
                int adapterPosition = getAdapterPosition();
                Context context = v.getContext();
                final Intent intent;
                intent = new Intent(context, NameActivity.class);
                switch (adapterPosition) {
                    case 0:
                        intent.putExtra("type", mProblemData[0]);
                        intent.putExtra("key1", mProblemData2[0]);
                        break;
                    case 1:
                        intent.putExtra("type", mProblemData[1]);
                        intent.putExtra("key1", mProblemData2[1]);
                        break;
                    case 2:
                        intent.putExtra("type", mProblemData[2]);
                        intent.putExtra("key1", mProblemData2[2]);
                        break;
                    case 3:
                        intent.putExtra("type", mProblemData[3]);
                        intent.putExtra("key1", mProblemData2[3]);
                        break;
                    default:
                        intent.putExtra("type", mProblemData[4]);
                        intent.putExtra("key1", mProblemData2[4]);
                        break;
                }
                String problemForDay = mProblemData[adapterPosition];
                mClickHandler.onClick(problemForDay);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        // TODO (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new OlympiadAdapterViewHolder that holds the View for each list item
     */
    @Override
    public OlympiadAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.olympiad_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new OlympiadAdapterViewHolder(view, context);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the problem
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param olympiadAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(OlympiadAdapterViewHolder olympiadAdapterViewHolder, int position) {
        String problemForThisDay = mProblemData[position];
        //olympiadAdapterViewHolder.formula.setText(problemorThisDay);
        olympiadAdapterViewHolder.SecondFormula.setText(problemForThisDay);
        intern.setImageResource(R.drawable.intern);

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our olympiad
     */
    @Override
    public int getItemCount() {
        if (null == mProblemData) return 0;
        return mProblemData.length;
    }

    /**
     * This method is used to set the problem olympiad on a OlympiadAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new OlympiadAdapter to display it.
     *
     * @param problemData The new problem data to be displayed.
     */
    public void setProblemData(String[] problemData, String[] problemData2) {
        mProblemData = problemData;
        mProblemData2 = problemData2;
        notifyDataSetChanged();
    }
}
