package co.com.firefly.daviviendatrade.portfolio;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.com.firefly.daviviendatrade.R;

/**
 * Created by toshiba on 11/07/2016.
 */
public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioViewHolder> {

    private PortfolioEquity[] mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PortfolioAdapter(PortfolioEquity[] myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PortfolioViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_portfolio, parent, false);
        // set the view's size, margins, paddings and layout parameters

        PortfolioViewHolder vh = new PortfolioViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PortfolioViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bindToEquity(this.mDataset[position]);

        holder.itemView.setOnClickListener(new PortfolioOnClickListener(holder.itemView,this.mDataset[position]));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.mDataset.length;
    }
}
