package co.com.firefly.daviviendatrade;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import co.com.firefly.daviviendatrade.firebase.model.Equity;
import co.com.firefly.daviviendatrade.firebase.model.User;
import co.com.firefly.daviviendatrade.firebase.viewholder.EquityViewHolder;


public class StockListingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Equity, EquityViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private FirebaseRecyclerAdapter<Equity, EquityViewHolder> mAdapterFav;
    private RecyclerView mRecyclerFav;
    private LinearLayoutManager mManagerFav;

    private ImageButton equitySearch;
    private AutoCompleteTextView searchEquityText;

    private Paint p = new Paint();

    public StockListingActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_listing);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        equitySearch = (ImageButton) findViewById(R.id.equitySearch);
        searchEquityText = (AutoCompleteTextView) findViewById(R.id.searchEquityText);

        String[] dummyEquities = getResources().getStringArray(R.array.dummy_equities);
        ArrayAdapter<String> equitySearchAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dummyEquities);
        searchEquityText.setAdapter(equitySearchAdapter);

        equitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchEquityText!=null && searchEquityText.getText()!=null && !searchEquityText.getText().toString().equals("")){

                    searchEquity(searchEquityText.getText().toString());

                } else {

                    searchEquityText.setError(getResources().getString(R.string.prompt_searchError));
                }

            }
        });

        mRecycler = (RecyclerView) findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        /*RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        mRecycler.setItemAnimator(itemAnimator);*/

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase, false);
        mAdapter = new FirebaseRecyclerAdapter<Equity, EquityViewHolder>(Equity.class, R.layout.item_post,
                EquityViewHolder.class, postsQuery) {

            @Override
            protected void populateViewHolder(final EquityViewHolder viewHolder, final Equity model, final int position) {
                final DatabaseReference postRef = getRef(position);

                //final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StockListingActivity.this, EquityDetail.class);
                        intent.putExtra(EquityDetail.EQUITY_NAME, model);
                        startActivity(intent);
                    }
                });

                viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);

                if (model.getSpread()!=null && model.getSpread().contains("-")){
                    viewHolder.equityValueView.setTextColor(getResources().getColor(R.color.red));
                    viewHolder.spreadView.setTextColor(getResources().getColor(R.color.red));
                    viewHolder.equityView.setTextColor(getResources().getColor(R.color.red));
                } else {
                    viewHolder.equityValueView.setTextColor(getResources().getColor(R.color.green));
                    viewHolder.spreadView.setTextColor(getResources().getColor(R.color.green));
                    viewHolder.equityView.setTextColor(getResources().getColor(R.color.green));
                }

                viewHolder.bindToEquity(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {

                        mDatabase.child("equity").child(model.getEquity()).child(getUid()).setValue(true);

                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);

        mRecyclerFav = (RecyclerView) findViewById(R.id.favorites_list);
        mRecyclerFav.setHasFixedSize(true);

        mManagerFav = new LinearLayoutManager(this);
        mManagerFav.setReverseLayout(true);
        mManagerFav.setStackFromEnd(true);
        mRecyclerFav.setLayoutManager(mManagerFav);

        Query postsQueryFav = getQuery(mDatabase, true);

        mAdapterFav = new FirebaseRecyclerAdapter<Equity, EquityViewHolder>(Equity.class, R.layout.item_post,
                EquityViewHolder.class, postsQueryFav) {

            @Override
            protected void populateViewHolder(final EquityViewHolder viewHolder, final Equity model, final int position) {
                final DatabaseReference postRef = getRef(position);

                //final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StockListingActivity.this, EquityDetail.class);
                        intent.putExtra(EquityDetail.EQUITY_NAME, model);
                        startActivity(intent);
                    }
                });

                viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);

                if (model.getSpread()!=null && model.getSpread().contains("-")){
                    viewHolder.equityValueView.setTextColor(getResources().getColor(R.color.red));
                    viewHolder.spreadView.setTextColor(getResources().getColor(R.color.red));
                    viewHolder.equityView.setTextColor(getResources().getColor(R.color.red));
                } else {
                    viewHolder.equityValueView.setTextColor(getResources().getColor(R.color.green));
                    viewHolder.spreadView.setTextColor(getResources().getColor(R.color.green));
                    viewHolder.equityView.setTextColor(getResources().getColor(R.color.green));
                }

                viewHolder.bindToEquity(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        mDatabase.child("equity").child(model.getEquity()).child(getUid()).setValue(false);

                    }
                });
            }
        };
        mRecyclerFav.setAdapter(mAdapterFav);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //int position = viewHolder.getAdapterPosition();

                if (swipeDir == ItemTouchHelper.LEFT){

                    EquityViewHolder equityViewHolder = (EquityViewHolder) viewHolder;

                    final DatabaseReference equityRef = mDatabase.child("equity").child(equityViewHolder.equity.getEquity());

                    equityRef.child(getUid()).removeValue();

                } else {

                    EquityViewHolder holder = (EquityViewHolder)viewHolder;

                    Intent intent = new Intent(StockListingActivity.this , BuyEquity.class);

                    intent.putExtra(BuyEquity.EQUITY_TO_BUY,holder.equity);

                    startActivity(intent);

                    mAdapter.notifyDataSetChanged();
                    mAdapterFav.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_buy);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelperFav = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelperFav.attachToRecyclerView(mRecyclerFav);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(mRecycler);

        //Menu

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userLoggedEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_logged_email);

        if(FirebaseAuth.getInstance().getCurrentUser()!= null && FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null){
            userLoggedEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference, boolean stared){
        Query recentPostsQuery;

        if(stared){
            recentPostsQuery = databaseReference.child("equity").orderByChild(getUid()).startAt(true).endAt(true);
        }else{
            recentPostsQuery = databaseReference.child("equity").orderByChild(getUid()).startAt(false).endAt(false);
        }

        return recentPostsQuery;
    }

    public void searchEquity(final String equity){

        final String userId = getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            Toast.makeText(StockListingActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            writeNewEquity(equity);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        /*Toast.makeText(StockListingActivity.this,
                                "getUser:onCancelled" + databaseError.toException(),
                                Toast.LENGTH_SHORT).show();*/
                    }
                });

    }

    public void writeNewEquity(String equity) {

        final DatabaseReference equityRef = mDatabase.child("equity").child(equity);

        equityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    equityRef.child(getUid()).setValue(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } /*else {
            super.onBackPressed();
        }*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_balance) {

        } else if (id == R.id.menu_portfolio) {
            Intent intent = new Intent(this, PortfolioDetailActivity.class);

            startActivity(intent);
        } else if (id == R.id.menu_help) {

        } else if (id == R.id.menu_config) {

        } else if (id == R.id.menu_security) {

        } else if (id == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
