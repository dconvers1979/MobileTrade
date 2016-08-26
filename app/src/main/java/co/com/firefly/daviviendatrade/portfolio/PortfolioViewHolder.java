package co.com.firefly.daviviendatrade.portfolio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

import co.com.firefly.daviviendatrade.R;

/**
 * Created by toshiba on 11/07/2016.
 */
public class PortfolioViewHolder extends RecyclerView.ViewHolder{

    // each data item is just a string in this case
    public TextView equityName;
    public TextView equityBuyingPrice;
    public TextView equityQuantity;
    public TextView equityCurrentPrice;
    public TextView equityCurrentTotal;
    public PortfolioEquity equity;


    public PortfolioViewHolder(View v) {
        super(v);
        equityName = (TextView)v.findViewById(R.id.equity_name_portfolio);
        equityBuyingPrice = (TextView)v.findViewById(R.id.equity_buying_price_portfolio);
        equityQuantity = (TextView)v.findViewById(R.id.equity_quantity_portfolio);
        equityCurrentPrice = (TextView)v.findViewById(R.id.equity_current_price_portfolio);
        equityCurrentTotal = (TextView)v.findViewById(R.id.equity_current_total_portfolio);
    }

    public void bindToEquity(PortfolioEquity equity) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        this.equity = equity;

        equityName.setText(equity.getEquityName());
        equityBuyingPrice.setText(format.format(equity.getEquityBuyingValue()));
        equityQuantity.setText(""+equity.getEquityQuantity());
        equityCurrentPrice.setText(format.format(equity.getEquityCurrentValue()));
        equityCurrentTotal.setText(format.format((equity.getEquityQuantity().doubleValue()*equity.getEquityCurrentValue().doubleValue())));

    }



}
