package co.com.firefly.daviviendatrade.firebase.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import co.com.firefly.daviviendatrade.R;
import co.com.firefly.daviviendatrade.firebase.model.EquityNews;

public class NewsfeedViewHolder extends RecyclerView.ViewHolder{

    // each data item is just a string in this case
    public TextView mTextView;
    public TextView mTitleView;
    public TextView mDateView;
    public TextView mHourView;

    public NewsfeedViewHolder(View v) {
        super(v);
        mTextView = (TextView)v.findViewById(R.id.info_text_news);
        mTitleView = (TextView)v.findViewById(R.id.info_title_news);
        mDateView = (TextView)v.findViewById(R.id.info_date_news);
        mHourView = (TextView)v.findViewById(R.id.info_hour_news);
    }

    public void bindToNews(EquityNews equityNews) {

        this.mTitleView.setText(equityNews.getTitle());
        this.mTextView.setText(equityNews.getBody());
        this.mDateView.setText(equityNews.getDate());
        this.mHourView.setText(equityNews.getHour());

    }

}
