package coolpharaoh.tee.speicher.tea.timer.views;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import coolpharaoh.tee.speicher.tea.timer.R;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;

public class Payment extends AppCompatActivity /* implements BillingProcessor.IBillingHandler */{

    private enum ListItems {
        Zero, One, Two, Three, Five, Ten
    }

    //private BillingProcessor bp;
    private String[] prices;
    private int currentPrice;

    private TextView mToolbarCustomTitle;
    private Button buttonNavigateLeft, buttonNavigateRight, buttonPurchase;
    private TextView textViewPurchaseAmount, textViewPurchaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.billing_heading);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prices = getResources().getStringArray(R.array.billing_prices);
        currentPrice = 1;

        //instanciate billing library
        //bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh7+jAZigZwBUU0m+4P8fuiDq7Z8PCbgvdLQW0kmrWspPsmY9TyaHgRWwwerZ5a7aAQfaeUEBF6AGQmdu9V0flJ+wQPPexZzEmX0wBnu2osVqLC7VxHnWCh/d+XMiaSDu0kRf0vI+FOQZgzeqWIDxKnbmqay2T0kfjnnshTzV1LUGYmp7bWK481f4Mrf9L9D7ZE9BsOkbUtQZ0VRYdBQotIl2aOmZh2q8ko7ixtB968v1ZDc5VmSmaPuWuJvk3ohkAL9aAgPG30r9M4qLCl+1xM3amEEAPhzFqIPJL0YqMh+D5k3YMf5TtQsbQY8WDrInYfVHn2n9oBxCjGSL1WPNjQIDAQAB", this);

        //Get Views
        textViewPurchaseInfo = findViewById(R.id.textViewPurchaseInfo);
        textViewPurchaseAmount = findViewById(R.id.textViewPurchaseAmount);
        buttonNavigateLeft = findViewById(R.id.buttonNavigateLeft);
        buttonNavigateRight = findViewById(R.id.buttonNavigateRight);
        buttonPurchase = findViewById(R.id.buttonPurchase);

        //set Prices
        textViewPurchaseAmount.setText(prices[currentPrice]);

        buttonNavigateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPrice--;
                textViewPurchaseAmount.setText(prices[currentPrice]);
                if(currentPrice<=0){
                    buttonNavigateLeft.setEnabled(false);
                }else{
                    buttonNavigateRight.setEnabled(true);
                }
            }
        });

        buttonNavigateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPrice++;
                textViewPurchaseAmount.setText(prices[currentPrice]);
                if(currentPrice>=5){
                    buttonNavigateRight.setEnabled(false);
                }else{
                    buttonNavigateLeft.setEnabled(true);
                }
            }
        });

        buttonPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                //bp.purchase(Payment.this, "android.test.purchased");
                ListItems item = ListItems.values()[currentPrice];
                switch (item){
                    case Zero:
                        textViewPurchaseInfo.setText(R.string.billing_zero);
                        purchaseDone();
                        break;
                    case One:
                        bp.purchase(Payment.this, "supportone");
                        break;
                    case Two:
                        bp.purchase(Payment.this, "supporttwo");
                        break;
                    case Three:
                        bp.purchase(Payment.this, "supportthree");
                        break;
                    case Five:
                        bp.purchase(Payment.this, "supportfive");
                        break;
                    case Ten:
                        bp.purchase(Payment.this, "supportten");
                        break;
                }
                */
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        /**
        * Called when requested PRODUCT ID was successfully purchased
        */
      /*  textViewPurchaseInfo.setText(R.string.billing_support_me);
        purchaseDone();
        bp.consumePurchase(productId);
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /**
        * Called when purchase history was restored and the list of all owned PRODUCT ID's
        * was loaded from Google Play
        */
    /*}

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        /**
        * Called when some error occurred. See Constants class for more details
        *
        * Note - this includes handling the case where the user canceled the buy dialog:
        * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
        */
    /*}

    @Override
    public void onBillingInitialized() {
        /**
        * Called when BillingProcessor was initialized and it's ready to purchase
        */
    /*}

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
         }
     }

     @Override
     public void onDestroy() {
         if (bp != null) {
            bp.release();
         }
         super.onDestroy();
     }

     private void purchaseDone(){
         buttonNavigateLeft.setVisibility(View.INVISIBLE);
         textViewPurchaseAmount.setVisibility(View.INVISIBLE);
         buttonNavigateRight.setVisibility(View.INVISIBLE);
         buttonPurchase.setVisibility(View.INVISIBLE);
     }
     */
 }