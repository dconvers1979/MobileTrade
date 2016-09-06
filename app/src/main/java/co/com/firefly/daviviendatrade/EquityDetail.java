package co.com.firefly.daviviendatrade;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import co.com.firefly.daviviendatrade.firebase.model.Equity;
import co.com.firefly.daviviendatrade.firebase.model.EquityNews;
import co.com.firefly.daviviendatrade.firebase.viewholder.NewsfeedViewHolder;

public class EquityDetail extends AppCompatActivity {

    public static final String EQUITY_NAME = "equityNameDetail";

    private String[] mMonth = new String[] {
            "Jan", "Feb" , "Mar", "Apr", "May", "Jun",
            "Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
    };


    private Equity equity;
    private LinearLayout chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        equity = null;

        TextView equityNameDetail;
        ImageButton buyEquity;
        final LinearLayoutManager mManager;
        ScrollView scroll;
        final RecyclerView mRecycler;
        final FirebaseRecyclerAdapter<EquityNews, NewsfeedViewHolder> mAdapter;
        DatabaseReference mDatabase;

        setContentView(R.layout.activity_equity_detail);

        equityNameDetail = (TextView) findViewById(R.id.equityNameDetail);
        chartView = (LinearLayout) findViewById(R.id.equityChart);

        buyEquity = (ImageButton) findViewById(R.id.detail_equity_buy);

        scroll = (ScrollView) findViewById(R.id.scroll_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.equity = (Equity) extras.getSerializable(EQUITY_NAME);
            equityNameDetail.setText(this.equity.getEquity()+" - "+this.equity.getValue());
        }else{
            finish();
        }

        buyEquity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EquityDetail.this,BuyEquity.class);

                intent.putExtra(BuyEquity.EQUITY_TO_BUY,equity);

                startActivity(intent);

            }
        });

        boolean raising = true;

        if (this.equity.getSpread()!=null && this.equity.getSpread().contains("-")){
            raising=false;
        }

        mRecycler = (RecyclerView) findViewById(R.id.equityNewsFeed);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);


        Query newsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<EquityNews, NewsfeedViewHolder>(EquityNews.class, R.layout.item_news,
                NewsfeedViewHolder.class, newsQuery) {

            @Override
            protected void populateViewHolder(final NewsfeedViewHolder viewHolder, final EquityNews model, final int position) {
                //final DatabaseReference postRef = getRef(position);
                
                viewHolder.bindToNews(model);

            }
        };

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                int lastVisiblePosition =
                        mManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mRecycler.scrollToPosition(positionStart);
                }
            }
        });

        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        scroll.scrollTo(0,0);

        ImageButton returnButton = (ImageButton) findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EquityDetail.this, StockListingActivity.class);
                startActivity(intent);
            }
        });

        openChart(raising);

    }

    /*@Override
    public void onBackPressed() {
        this.finish();
        mAdapter=null;
        mRecycler =null;
        super.onBackPressed();
    }*/

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }*/

    public Query getQuery(DatabaseReference databaseReference){
        Query recentNewsQuery;

        recentNewsQuery = databaseReference.getRoot().child("news").child(this.equity.getEquity()).orderByChild("date");

        return recentNewsQuery;
    }

    private void openChart(boolean raising){

        int baseColor;
        GraphicalView mChartView;

        if(raising){
            baseColor = Color.GREEN;
        }else{
            baseColor = Color.RED;
        }

        //equityNameDetail.setTextColor(baseColor);

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
