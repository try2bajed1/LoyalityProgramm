package su.ias.malina.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import su.ias.malina.R;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.async.PostTask;
import su.ias.malina.data.DiscountData;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.09.2014
 * Time: 16:49
 */

public class SelectedDiscountActivity extends BaseActivity implements IListener {

    public static final String SELECTED_DISCOUNT = "sel_disc";
    private DiscountData discountData;
    private Button gotoUrlBtn;
    private Button participateBtn;
    private TextView rulesTV;
    private Button rulesBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        discountData = (DiscountData) getIntent().getSerializableExtra(SELECTED_DISCOUNT);

        setContentView(R.layout.activity_selected_discount);

        actionBar.setTitle("Акции");
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView img = (ImageView) findViewById(R.id.discount_img);
        ImageLoader.getInstance().displayImage(discountData.getSliderImgUrl(), img);

        TextView titleTV = (TextView) findViewById(R.id.discount_title);
        titleTV.setText(discountData.getTitle());

        rulesTV = (TextView) findViewById(R.id.rules_text);
        rulesTV.setText(discountData.getRules());


        TextView descrTV = (TextView) findViewById(R.id.discount_descr);
        if(discountData.getDescription().equals("null")){
            descrTV.setText(discountData.getContent());
        }else {
            descrTV.setText(discountData.getDescription());
        }

        participateBtn = (Button) findViewById(R.id.participate_btn);
        participateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonToPost = AppApiUtils.getJsonForParticipate(AppSingleton.get().getAccountData().getAccount_number(), String.valueOf(discountData.getId()));
//                Log.i("@", "jsonToPost " + jsonToPost);
                new PostTask(SelectedDiscountActivity.this).execute(AppApiUtils.ACTION_PARTICIPATE, jsonToPost);
                participateBtn.setEnabled(false);
            }
        });


        rulesBtn = (Button) findViewById(R.id.rules_btn);
        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rulesTV.getVisibility() == View.GONE) {
                    rulesTV.setVisibility(View.VISIBLE);
                } else {
                    rulesTV.setVisibility(View.GONE);
                }
            }
        });


        gotoUrlBtn = (Button) findViewById(R.id.goto_website_btn);
        gotoUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("@","url "+ discountData.getOfferUrl());
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(discountData.getOfferUrl())));
            }
        });


        if (discountData.getOfferType().equals("offline")) {
            rulesBtn.setVisibility(View.VISIBLE);
            gotoUrlBtn.setVisibility(View.GONE);
        } else {
            descrTV.setText(discountData.getRules());
            rulesBtn.setVisibility(View.GONE);
            gotoUrlBtn.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {
        if (actionName.equals(AppApiUtils.ACTION_PARTICIPATE)) {
            Toast.makeText(this,"Ваш запрос принят", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        Toast.makeText(this,"При отправлении данных произошла ошибка", Toast.LENGTH_SHORT).show();
        participateBtn.setEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
