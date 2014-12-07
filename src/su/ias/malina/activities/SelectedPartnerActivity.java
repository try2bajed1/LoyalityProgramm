package su.ias.malina.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.async.PostTask;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.09.2014
 * Time: 16:49
 */

public class SelectedPartnerActivity extends ActionBarActivity implements IListener {


    public static final String SELECTED_PARTNER_ID = "sel_partner_id";
    public static final String GET_PARTNER_INFO = "get_partner_info";
    private int partnerId;
    private ImageView partnerImg;
    private String partnerUrl = "";
    private Button gotoBtn;
    private TextView descrTV;


    public static final String MODE_EXTRA = "mode_extra";
    public static final String IS_ONLINE = "is_online";
    public static final int ONLY_SOFA_MODE = 1;
    public static final int ALL_MODE = 2;
    private int mode;
    private boolean isOnline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        partnerId =  getIntent().getIntExtra(SELECTED_PARTNER_ID, -1);
        mode = getIntent().getIntExtra(MODE_EXTRA, ALL_MODE);
        isOnline = getIntent().getBooleanExtra(IS_ONLINE,true);

        setContentView(R.layout.activity_selected_online_partner);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mode == ONLY_SOFA_MODE ? "На диване" : "Партнеры");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.logo_filter);

        partnerImg = (ImageView) findViewById(R.id.partner_img);
        partnerImg.setVisibility(View.GONE);
        descrTV = (TextView) findViewById(R.id.discount_descr);

        initBtn();

        new PostTask(this).execute(GET_PARTNER_INFO, getJsonForPost());
    }




    private void initBtn() {

        gotoBtn = (Button) findViewById(R.id.goto_website_btn);
        if (mode == ONLY_SOFA_MODE) {
            gotoBtn.setText("Купить сейчас");
        } else {
            if(isOnline) {
                gotoBtn.setText("Перейти на сайт партнера");
            } else {
                gotoBtn.setText("Точки на карте");
            }
        }


        gotoBtn.setVisibility(View.GONE);
        gotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(partnerUrl)));
                } else {
                    //Toast.makeText(SelectedPartnerActivity.this, "map ", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(SelectedPartnerActivity.this, SinglePartnerPointsActivity.class)
                                        .putExtra(SinglePartnerPointsActivity.PARTNER_ID_EXTRA,partnerId));
                }
            }
        });

    }


    private String getJsonForPost() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, GET_PARTNER_INFO);
            jsonObject.put("partner_id", partnerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
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




    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

//        Log.i("@", "jsonFromApi "+jsonFromApi);
        //{"status": "ok", "partner": {"code": "062", "has_transactions": false, "is_card_emitter": false, "partner_url": "http://ad.admitad.com/goto/ecc96ea13e9b082de664c7e11adc3f/?cardnumber=6393000088427721", "name": "ASMC - \u0430\u0440\u043c\u0435\u0439\u0441\u043a\u0438\u0439 \u043c\u0430\u0433\u0430\u0437\u0438\u043d", "has_points": false, "region": "msk", "logo_url": "http://lpvstatic.ru/media/partners/brands/logo_85.JPG", "description": "\u0412\u0441\u0435 \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e\u0435 \u0434\u043b\u044f \u043f\u0440\u0438\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0439 \u0432\u0441\u0435\u0445 \u0432\u0438\u0434\u043e\u0432"}}

        try {
            JSONObject jsonObject = new JSONObject(jsonFromApi);
            partnerUrl = jsonObject.getJSONObject("partner").getString("partner_url");

            gotoBtn.setVisibility(View.VISIBLE);

            String imgUrl = jsonObject.getJSONObject("partner").getString("logo_url");
            partnerImg.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(imgUrl, partnerImg);

            String description = jsonObject.getJSONObject("partner").getString("description");
            descrTV.setText(description);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        Toast.makeText(this, "Oшибка при передаче данных", Toast.LENGTH_SHORT).show();
        finish();
    }



}
