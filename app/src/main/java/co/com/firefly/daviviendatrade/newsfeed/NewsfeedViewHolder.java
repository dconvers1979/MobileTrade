package co.com.firefly.daviviendatrade.newsfeed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import co.com.firefly.daviviendatrade.R;

/**
 * Created by toshiba on 06/07/2016.
 */
public class NewsfeedViewHolder extends RecyclerView.ViewHolder{

    // each data item is just a string in this case
    public TextView mTextView;
    public NewsfeedViewHolder(View v) {
        super(v);
        mTextView = (TextView)v.findViewById(R.id.info_text_news);
    }


}
