package co.com.firefly.daviviendatrade.portfolio;

import android.content.Intent;
import android.view.View;

import co.com.firefly.daviviendatrade.SellEquityActivity;

/**
 * Created by toshiba on 11/07/2016.
 */
public class PortfolioOnClickListener implements View.OnClickListener {

    private View activity;
    private PortfolioEquity model;

    public PortfolioOnClickListener(View activity, PortfolioEquity model){
        this.activity = activity;
        this.model = model;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity.getContext(), SellEquityActivity.class);
        intent.putExtra(SellEquityActivity.EQUITY_TO_SELL, model);
        activity.getContext().startActivity(intent);
    }
}
