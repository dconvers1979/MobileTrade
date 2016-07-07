package co.com.firefly.daviviendatrade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,StockListingActivity.class);

        startActivity(intent);

        finish();
    }
}
