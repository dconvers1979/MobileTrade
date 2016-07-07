package co.com.firefly.daviviendatrade;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import co.com.firefly.daviviendatrade.firebase.model.Equity;
import co.com.firefly.daviviendatrade.newsfeed.NewsfeedAdapter;

public class EquityDetail extends AppCompatActivity {

    public static final String EQUITY_NAME = "equityNameDetail";

    private String[] mMonth = new String[] {
            "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
            "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
    };

    private TextView equityNameDetail;
    private Equity equity;
    private LinearLayout chartView;
    private GraphicalView mChartView;

    private Button buyEquity;

    private RecyclerView mRecyclerViewNews;
    private RecyclerView.Adapter mAdapterNews;
    private RecyclerView.LayoutManager mLayoutManagerNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equity_detail);

        equityNameDetail = (TextView) findViewById(R.id.equityNameDetail);
        chartView = (LinearLayout) findViewById(R.id.equityChart);

        buyEquity = (Button) findViewById(R.id.detail_equity_buy);

        buyEquity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EquityDetail.this,BuyEquity.class);

                intent.putExtra(BuyEquity.EQUITY_TO_BUY,equity);

                startActivity(intent);

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.equity = (Equity) extras.getSerializable(EQUITY_NAME);
            equityNameDetail.setText(this.equity.getEquity()+" - "+this.equity.getValue());
        }else{
            finish();
        }

        boolean raising = true;

        if (this.equity.getSpread()!=null && this.equity.getSpread().contains("-")){
            raising=false;
        }

        mRecyclerViewNews = (RecyclerView) findViewById(R.id.equityNewsFeed);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerViewNews.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerNews = new LinearLayoutManager(this);
        mRecyclerViewNews.setLayoutManager(mLayoutManagerNews);

        String news[] = {getResources().getString(R.string.dummy_news_1),
                getResources().getString(R.string.dummy_news_2),
                getResources().getString(R.string.dummy_news_3),
                getResources().getString(R.string.dummy_news_4),
                getResources().getString(R.string.dummy_news_5)};//TODO hardcoded news

        // specify an adapter (see also next example)
        mAdapterNews = new NewsfeedAdapter(news);
        mRecyclerViewNews.setAdapter(mAdapterNews);

        openChart(raising);

    }

    private void openChart(boolean raising){

        int baseColor = 0;

        if(raising){
            baseColor = Color.GREEN;
        }else{
            baseColor = Color.RED;
        }

        equityNameDetail.setTextColor(baseColor);

        int[] x = { 1,2,3,4,5,6,7,8 };
        int[] income = { 2000,2500,2700,3000,2800,3500,3700,3800};

        if(!raising){
            income = new int[]{3800,3700,3500,2800,2700,2500,3000,2000};
        }

        // Creating an  XYSeries for Income
        XYSeries incomeSeries = new XYSeries("Price");
        // Creating an  XYSeries for Expense
        // Adding data to Income and Expense Series
        for(int i=0;i<x.length;i++){
            incomeSeries.add(x[i], income[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(incomeSeries);

        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(baseColor);
        incomeRenderer.setPointStyle(PointStyle.CIRCLE);
        incomeRenderer.setAnnotationsColor(Color.WHITE);
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setLineWidth(3);
        incomeRenderer.setStroke(BasicStroke.SOLID);
        incomeRenderer.setChartValuesTextSize(30);

        incomeRenderer.setDisplayChartValues(true);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Precios de la accion");
        multiRenderer.setXTitle("2016");
        multiRenderer.setYTitle("COP");
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setMargins(new int[]{0,0,0,0});
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setLabelsColor(baseColor);
        multiRenderer.setXLabelsColor(baseColor);
        multiRenderer.setYLabelsColor(0,baseColor);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setClickEnabled(false);
        multiRenderer.setExternalZoomEnabled(false);
        multiRenderer.setLegendHeight(0);
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setLegendTextSize(30);
        multiRenderer.setAxisTitleTextSize(30);
        multiRenderer.setChartTitleTextSize(30);
        multiRenderer.setInScroll(false);
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        multiRenderer.setZoomEnabled(false,false);


        multiRenderer.setZoomButtonsVisible(false);
        for(int i=0;i<x.length;i++){
            multiRenderer.addXTextLabel(i+1, mMonth[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(incomeRenderer);

        mChartView = ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);

        chartView.addView(mChartView, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

    }



}
