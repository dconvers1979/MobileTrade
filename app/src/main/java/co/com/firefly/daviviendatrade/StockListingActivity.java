package co.com.firefly.daviviendatrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

import co.com.firefly.daviviendatrade.firebase.model.Equity;
import co.com.firefly.daviviendatrade.firebase.model.User;
import co.com.firefly.daviviendatrade.firebase.viewholder.EquityViewHolder;


public class StockListingActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Equity, EquityViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public StockListingActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_listing);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Equity, EquityViewHolder>(Equity.class, R.layout.item_post,
                EquityViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final EquityViewHolder viewHolder, final Equity model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Launch PostDetailActivity TODO ir a detalle del equity
                        //Intent intent = new Intent(StockListingActivity.this, PostDetailActivity.class);
                        //intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        //startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.getStars().containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("equity").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-equity").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);

    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Equity p = mutableData.getValue(Equity.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.getStars().containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.getStars().remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.getStars().put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Toast.makeText(StockListingActivity.this, "postTransaction:onComplete:" + databaseError,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    // [END post_stars_transaction]

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

    public Query getQuery(DatabaseReference databaseReference){
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("equity")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    public void newEquity(View view){
        final String equity = "ECOP";
        final String value = "1400";

        final String userId = getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Toast.makeText(StockListingActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, equity, value);
                        }

                        // Finish this Activity, back to the stream
                        //finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(StockListingActivity.this,
                                "getUser:onCancelled" + databaseError.toException(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        // [END single_value_read]

    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String equity, String value) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("equity").push().getKey();
        Equity post = new Equity(userId, equity, value);
        Map<String, Object> equityValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/equity/" + key, equityValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]


}
