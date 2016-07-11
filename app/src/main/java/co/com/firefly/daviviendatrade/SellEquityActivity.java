package co.com.firefly.daviviendatrade;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;

import co.com.firefly.daviviendatrade.portfolio.PortfolioEquity;

public class SellEquityActivity extends AppCompatActivity {

    private PortfolioEquity equity;
    private TextView equityName;
    private TextView userBalance;
    private EditText sellQuantity;
    private EditText sellPrice;
    private TextView sellTotal;
    private Double total = new Double(0);
    private Double price = new Double(0);
    private Double quantity = new Double(0);
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private ImageButton sellAction;

    public static final String EQUITY_TO_SELL = "equityToSell";

    private Double dummyUserBalance = new Double(256032000.05); // TODO dummy value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_equity);

        equityName = (TextView) findViewById(R.id.sell_equity_name);
        userBalance = (TextView) findViewById(R.id.user_balance);
        sellQuantity = (EditText) findViewById(R.id.sell_quantity);
        sellPrice = (EditText) findViewById(R.id.sell_market_price);
        sellTotal = (TextView) findViewById(R.id.sell_total);
        sellAction = (ImageButton) findViewById(R.id.sell_action);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.equity = (PortfolioEquity) extras.getSerializable(EQUITY_TO_SELL);
            String moneyString = formatter.format(this.equity.getEquityCurrentValue());
            equityName.setText(this.equity.getEquityName()+" - "+moneyString);
            this.sellPrice.setText(""+this.equity.getEquityCurrentValue());
        }else{
            finish();
        }


        String moneyString = formatter.format(dummyUserBalance);//TODO dummy value

        userBalance.setText(moneyString);//TODO dummy value

        sellPrice.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                calculateTotal();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        sellQuantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                calculateTotal();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        sellAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SellEquityActivity.this)
                        .setTitle(SellEquityActivity.this.getResources().getString(R.string.sell_confirm_title))
                        .setMessage(SellEquityActivity.this.getResources().getString(R.string.sell_confirm))
                        .setIcon(R.mipmap.ic_notification)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(SellEquityActivity.this,OrderStatusActivity.class);

                                startActivity(intent);
                            }})
                        .setNegativeButton(SellEquityActivity.this.getResources().getString(R.string.no), null).show();

            }
        });
    }

    public void calculateTotal(){
        total = new Double(0);

        if(this.sellPrice.getText()!=null && this.sellQuantity.getText()!=null &&
                !this.sellPrice.getText().toString().equals("") && !this.sellQuantity.getText().toString().equals("")){
            price = new Double(this.sellPrice.getText().toString());
            quantity = new Double(this.sellQuantity.getText().toString());
            total = price * quantity;
            sellTotal.setText(formatter.format(this.total));
        }

    }
}
