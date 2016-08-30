package co.com.firefly.daviviendatrade;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;

import co.com.firefly.daviviendatrade.portfolio.PortfolioAdapter;
import co.com.firefly.daviviendatrade.portfolio.PortfolioEquity;
import co.com.firefly.daviviendatrade.portfolio.PortfolioViewHolder;

public class PortfolioDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewPortfolio;
    private RecyclerView.Adapter mAdapterPortfolio;
    private RecyclerView.LayoutManager mLayoutManagerPortfolio;
    private Paint p = new Paint();

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
        equity1.setEquityName("PFBCOLOM");
        equity1.setEquityQuantity(20);
        equity1.setEquityBuyingValue(Double.valueOf(45000));
        equity1.setEquityCurrentValue(Double.valueOf(50000));
        PortfolioEquity equity2 = new PortfolioEquity();
        equity2.setEquityName("PFBCOLOM");
        equity2.setEquityQuantity(40);
        equity2.setEquityBuyingValue(Double.valueOf(41000));
        equity2.setEquityCurrentValue(Double.valueOf(50000));
        PortfolioEquity equity3 = new PortfolioEquity();
        equity3.setEquityName("BVC");
        equity3.setEquityQuantity(200);
        equity3.setEquityBuyingValue(Double.valueOf(3600));
        equity3.setEquityCurrentValue(Double.valueOf(1260));
        PortfolioEquity equity4 = new PortfolioEquity();
        equity4.setEquityName("ETB");
        equity4.setEquityQuantity(300);
        equity4.setEquityBuyingValue(Double.valueOf(4500));
        equity4.setEquityCurrentValue(Double.valueOf(5000));

        PortfolioEquity equities[] = {equity1,equity2,equity3,equity4};//TODO hardcoded equities

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();

                if (swipeDir == ItemTouchHelper.LEFT){
                    PortfolioViewHolder holder = (PortfolioViewHolder)viewHolder;

                    Intent intent = new Intent(PortfolioDetailActivity.this , SellEquityActivity.class);

                    intent.putExtra(SellEquityActivity.EQUITY_TO_SELL,holder.equity);

                    startActivity(intent);

                    mAdapterPortfolio.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX < 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sell);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(mRecyclerViewPortfolio);

        // specify an adapter (see also next example)
        mAdapterPortfolio = new PortfolioAdapter(equities);
        mRecyclerViewPortfolio.setAdapter(mAdapterPortfolio);

        ImageButton returnButton = (ImageButton) findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortfolioDetailActivity.this, StockListingActivity.class);
                startActivity(intent);
            }
        });
    }


}
