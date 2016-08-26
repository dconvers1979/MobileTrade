package co.com.firefly.daviviendatrade.firebase.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import co.com.firefly.daviviendatrade.R;
import co.com.firefly.daviviendatrade.firebase.model.Equity;

/**
 * Created by toshiba on 29/06/2016.
 */
public class EquityViewHolder extends RecyclerView.ViewHolder {

    public TextView equityView;
    public TextView equityValueView;
    public ImageView starView;
    public TextView numStarsView;
    public View view;
    public Equity equity;

    public EquityViewHolder(View itemView) {
        super(itemView);

        view = itemView;
        equityView = (TextView) itemView.findViewById(R.id.equity_name);
        equityValueView = (TextView) itemView.findViewById(R.id.equity_value);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
    }

    public void bindToEquity(Equity equity, View.OnClickListener starClickListener) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        this.equity = equity;

        equityView.setText(equity.getEquity());
        numStarsView.setText(String.valueOf(equity.getStarCount()));

        starView.setOnClickListener(starClickListener);
        try{
            equityValueView.setText(format.format(Double.valueOf(equity.getValue())));
        }catch(Exception e){
            equityValueView.setText(equity.getValue());
        }

    }
}