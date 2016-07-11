package co.com.firefly.daviviendatrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import co.com.firefly.daviviendatrade.portfolio.PortfolioAdapter;
import co.com.firefly.daviviendatrade.portfolio.PortfolioEquity;

public class PortfolioDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewPortfolio;
    private RecyclerView.Adapter mAdapterPortfolio;
    private RecyclerView.LayoutManager mLayoutManagerPortfolio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_detail);

        mRecyclerViewPortfolio = (RecyclerView) findViewById(R.id.portfolio_equities);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewPortfolio.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerPortfolio = new LinearLayoutManager(this);
        mRecyclerViewPortfolio.setLayoutManager(mLayoutManagerPortfolio);

        PortfolioEquity equity1 = new PortfolioEquity();
        equity1.setEquityName("FIREFLY");
        equity1.setEquityQuantity(new Integer(20));
        equity1.setEquityBuyingValue(new Double(45000));
        equity1.setEquityCurrentValue(new Double(50000));
        PortfolioEquity equity2 = new PortfolioEquity();
        equity2.setEquityName("FIREFLY");
        equity2.setEquityQuantity(new Integer(40));
        equity2.setEquityBuyingValue(new Double(41000));
        equity2.setEquityCurrentValue(new Double(50000));
        PortfolioEquity equity3 = new PortfolioEquity();
        equity3.setEquityName("ECOPETROL");
        equity3.setEquityQuantity(new Integer(200));
        equity3.setEquityBuyingValue(new Double(3600));
        equity3.setEquityCurrentValue(new Double(1260));
        PortfolioEquity equity4 = new PortfolioEquity();
        equity4.setEquityName("ARGOS");
        equity4.setEquityQuantity(new Integer(300));
        equity4.setEquityBuyingValue(new Double(4500));
        equity4.setEquityCurrentValue(new Double(5000));

        PortfolioEquity equities[] = {equity1,equity2,equity3,equity4};//TODO hardcoded equities

        // specify an adapter (see also next example)
        mAdapterPortfolio = new PortfolioAdapter(equities);
        mRecyclerViewPortfolio.setAdapter(mAdapterPortfolio);
    }


}
