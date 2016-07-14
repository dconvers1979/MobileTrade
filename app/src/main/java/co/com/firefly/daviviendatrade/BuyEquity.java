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
import android.widget.Toast;

import java.text.NumberFormat;

import co.com.firefly.daviviendatrade.firebase.model.Equity;

public class BuyEquity extends AppCompatActivity {

    private Equity equity;
    private TextView equityName;
    private TextView equityValue;
    private TextView userBalance;
    private EditText buyQuantity;
    private EditText buyPrice;
    private TextView buyTotal;
    private Double total = new Double(0);
    private Double price = new Double(0);
    private Double quantity = new Double(0);
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private ImageButton buyAction;

    public static final String EQUITY_TO_BUY = "equityToBuy";

    private Double dummyUserBalance = new Double(256032000.05); // TODO dummy value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_equity);

        equityName = (TextView) findViewById(R.id.buy_equity_name);
        equityValue = (TextView) findViewById(R.id.buy_equity_value);
        userBalance = (TextView) findViewById(R.id.user_balance);
        buyQuantity = (EditText) findViewById(R.id.buy_quantity);
        buyPrice = (EditText) findViewById(R.id.buy_market_price);
        buyTotal = (TextView) findViewById(R.id.buy_total);
        buyAction = (ImageButton) findViewById(R.id.buy_action);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.equity = (Equity) extras.getSerializable(EQUITY_TO_BUY);
            String moneyString = formatter.format(new Double(this.equity.getValue().trim()));
            equityName.setText(this.equity.getEquity());
            equityValue.setText(moneyString);
            this.buyPrice.setText(this.equity.getValue());
        }else{
            finish();
        }


        String moneyString = formatter.format(dummyUserBalance);//TODO dummy value

        userBalance.setText(moneyString);//TODO dummy value

        buyPrice.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                calculateTotal();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        buyQuantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                calculateTotal();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        buyAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BuyEquity.this)
                        .setTitle(BuyEquity.this.getResources().getString(R.string.buy_confirm_title))
                        .setMessage(BuyEquity.this.getResources().getString(R.string.buy_confirm))
                        .setIcon(R.mipmap.ic_notification)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(BuyEquity.this,OrderStatusActivity.class);

                                startActivity(intent);
                            }})
                        .setNegativeButton(BuyEquity.this.getResources().getString(R.string.no), null).show();

            }
        });
    }

    public void calculateTotal(){
        total = new Double(0);

        if(this.buyPrice.getText()!=null && this.buyQuantity.getText()!=null &&
                !this.buyPrice.getText().toString().equals("") && !this.buyQuantity.getText().toString().equals("")){
            price = new Double(this.buyPrice.getText().toString());
            quantity = new Double(this.buyQuantity.getText().toString());
            total = price * quantity;
            buyTotal.setText(formatter.format(this.total));
        }

    }
}
