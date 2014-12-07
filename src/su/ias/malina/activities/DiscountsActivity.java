package su.ias.malina.activities;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.activities.fragments.DiscountsFragment;
import su.ias.malina.adapters.DiscountsPagerAdapter;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.components.PagerSlidingTabStrip;
import su.ias.malina.data.DiscountData;
import su.ias.malina.dialogs.CategoriesDialog;
import su.ias.malina.interfaces.IFilterDialogListener;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 20.03.14
 * Time: 12:36
 */
public class DiscountsActivity extends BaseAppCompatActivity implements IListener, IFilterDialogListener {


    private ViewPager mPager;

    public static final String TYPE_ONLINE = "online";
    public static final String TYPE_OFFLINE = "offline";

    private ArrayList<DiscountData> onlineArr  = new ArrayList<DiscountData>();
    private ArrayList<DiscountData> offlineArr = new ArrayList<DiscountData>();

    private Dialog categoriesDialog;

    private LinearLayout connectionErrorContainer;
    private Button tryAgainBtn ;

    private PagerSlidingTabStrip tabs;
    private SmoothProgressBar smoothProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_viewpager_drawer );

        initDrawer();
        initNavButtons();

        connectionErrorContainer = (LinearLayout) findViewById(R.id.connection_error);
        tryAgainBtn = (Button) findViewById(R.id.try_again_btn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionErrorContainer.setVisibility(View.GONE);
                startGetDataTask();
            }
        });


        mPager = (ViewPager) findViewById(R.id.pager);

        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

//                actionBar.setSelectedNavigationItem(position);
                updatePagerFragment();
            }
        };


        FragmentManager fm = getSupportFragmentManager();
        DiscountsPagerAdapter fragmentPagerAdapter = new DiscountsPagerAdapter(fm);
        mPager.setAdapter(fragmentPagerAdapter);
//        mPager.setOnPageChangeListener(pageChangeListener);


        tabs = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);
        tabs.setViewPager(mPager);
        tabs.setOnPageChangeListener(pageChangeListener);

        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.smooth_pb);


        categoriesDialog = getFilterDialog();

        startGetDataTask();

    }


    @Override
    public void setNavButtons() {
        mapNavLL.setEnabled(true);
        ((ImageView) mapNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.map));
        ((TextView)  mapNavLL.getChildAt(1)).setTextColor(getResources().getColor(R.color.navigation_btn_unactive_text_color));

        discountsNavLL.setEnabled(false);
        ((ImageView) discountsNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.action_active));
        ((TextView)  discountsNavLL.getChildAt(1)).setTextColor(Color.WHITE);

        cardsNavLL.setEnabled(true);
        ((ImageView) cardsNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.cards));
        ((TextView)  cardsNavLL.getChildAt(1)).setTextColor(getResources().getColor(R.color.navigation_btn_unactive_text_color));
    }


    private void startGetDataTask() {
        smoothProgressBar.setVisibility(View.VISIBLE);
        new GetDiscountsData(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TYPE_ONLINE,  getOffersJsonOnline());
        new GetDiscountsData(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TYPE_OFFLINE, getOffersJsonOffline());
    }



    private void updatePagerFragment() {
        Fragment currentPage = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());
        if (mPager.getCurrentItem() == 0 && currentPage != null) {
            ((DiscountsFragment)currentPage).setDiscountsAdapter(onlineArr);
        } else if (mPager.getCurrentItem() == 1 && currentPage != null) {
            ((DiscountsFragment)currentPage).setDiscountsAdapter(offlineArr);
        }
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

//        Log.w("@","discounts "+ jsonFromApi);

        //todo:закешировать массивы акций в бд
        if (actionName.equals(TYPE_OFFLINE)) {
            offlineArr.clear();
        } else {
            onlineArr.clear();
        }

        if (mPager.getVisibility() == View.GONE) {
            mPager.setVisibility(View.VISIBLE);
        }

        try {

            JSONObject discountsJson = new JSONObject(jsonFromApi);
            JSONArray discountsArr = discountsJson.getJSONArray("offers");
            for (int i = 0; i < discountsArr.length(); i++) {

                JSONObject singleDiscount = discountsArr.getJSONObject(i);

                String participateUrl = singleDiscount.has("participate_url") ? singleDiscount.getString("participate_url") :"";
                String logoUrl = singleDiscount.has("logo_url") ? singleDiscount.getString("logo_url") :"";
                String rules = singleDiscount.has("rules") ? singleDiscount.getString("rules") :"";
                String description = singleDiscount.has("description") ? singleDiscount.getString("description") : "";
                String content = singleDiscount.has("content") ? singleDiscount.getString("content") : "";
                String offerUrl = singleDiscount.has("offer_url") ? singleDiscount.getString("offer_url") :"";
                String offerType = singleDiscount.has("offer_type") ? singleDiscount.getString("offer_type") : "";
                String sliderImgUrl = singleDiscount.has("slider_image") ? singleDiscount.getString("slider_image") : "";
                String startDate = singleDiscount.has("start_date") ? singleDiscount.getString("start_date") : "";
                String endDate = singleDiscount.has("end_date") ? singleDiscount.getString("end_date") : "";
                String title = singleDiscount.has("title") ? singleDiscount.getString("title") : "";
                String sliderDescr = singleDiscount.has("slider_desc") ? singleDiscount.getString("slider_desc") :"";
                String sliderTitle = singleDiscount.has("slider_title") ? singleDiscount.getString("slider_title") :"";
                int id = singleDiscount.has("id") ? singleDiscount.getInt("id") : -1;


                DiscountData discountData = new DiscountData();
                discountData.setParticipateUrl(participateUrl);
                discountData.setLogoUrl(logoUrl);
                discountData.setRules(rules);
                discountData.setDescription(description);
                discountData.setContent(content);
                discountData.setOfferUrl(offerUrl);
                discountData.setOfferType(offerType);
                discountData.setSliderImgUrl(sliderImgUrl);
                discountData.setStartDate(startDate);
                discountData.setEndDate(endDate);
                discountData.setTitle(title);
                discountData.setSliderDescr(sliderDescr);
                discountData.setSliderTitle(sliderTitle);
                discountData.setId(id);

                if (actionName.equals(TYPE_OFFLINE)) {
                    offlineArr.add(discountData);
                } else{
                    onlineArr.add(discountData);
                }

                if(onlineArr.size() >0 && offlineArr.size()>0) {
                    smoothProgressBar.setVisibility(View.INVISIBLE);
                }
            }

        } catch (JSONException e) {
            Log.e("@","json ex "+ e.toString());
        }

        updatePagerFragment();
    }



    @Override
    public void responseErrorHandler(String actionName, String errorStr) {

        smoothProgressBar.setVisibility(View.INVISIBLE);

        mPager.setVisibility(View.GONE);
        connectionErrorContainer.setVisibility(View.VISIBLE);
    }



    private String getOffersJsonOnline() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, AppApiUtils.ACTION_GET_OFFERS);
            JSONObject filterParams = new JSONObject();
            filterParams.put("categories_ids", new JSONArray(Arrays.toString(AppSingleton.get().getDBAdapter().getSelectedCategories())));
            filterParams.put("partner_type", TYPE_ONLINE);
            jsonObject.put(AppApiUtils.FILTER_PARAMS, filterParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }



    private String getOffersJsonOffline() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(AppApiUtils.ACTION, AppApiUtils.ACTION_GET_OFFERS);
            JSONObject filterParams = new JSONObject();
            filterParams.put("partner_type", TYPE_OFFLINE);
            jsonObject.put(AppApiUtils.FILTER_PARAMS, filterParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Если событие обработано переключателем, то выходим
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item))
            return true;


        switch (item.getItemId()) {

            case android.R.id.home:

            return true;

            case R.id.action_filter:
//                Log.i("@",offlineArr.toString()+onlineArr.toString());
                categoriesDialog.show();
            return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_discounts, menu);

        return true;
    }



    @Override
    protected void onPause() {
        super.onPause();

        if(categoriesDialog !=null)
           categoriesDialog.dismiss();
    }


    @Override
    public Dialog getFilterDialog() {
        return new CategoriesDialog(this, this);
    }



    @Override
    public void applyFilter() {
        new GetDiscountsData(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TYPE_ONLINE,  getOffersJsonOnline());
    }


    private class GetDiscountsData extends AsyncTask<String, Void, Boolean> {

        private String errorCode;
        private String errorMessage;
        private IListener listener;
        private String discountType;
        private String dataFromServer;

        private static final String OK = "ok";
        private static final String ERROR = "error";


        public GetDiscountsData(IListener listener) {
            this.listener = listener;
        }


        @Override
        protected Boolean doInBackground(String... params) {

            boolean result;

            discountType = params[0];
            String jsonToPostStr = params[1];
            DefaultHttpClient client = new DefaultHttpClient();
            client.getParams().setBooleanParameter("http.protocol.expect-continue", false);

            if (AppSingleton.cookieStore != null) {
                client.setCookieStore(AppSingleton.cookieStore);
            }

            HttpPost httppost = new HttpPost(AppApiUtils.SERVER_URL);

            try {
                httppost.setEntity(new StringEntity(jsonToPostStr, "UTF-8"));
                HttpResponse response = client.execute(httppost);

                BufferedReader ins = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder sb = new StringBuilder("");
                dataFromServer = "";
                while ((dataFromServer = ins.readLine()) != null) {
                    sb.append(dataFromServer);
                }
                dataFromServer = sb.toString();

                JSONObject jsonObject = new JSONObject(dataFromServer);
                String status = jsonObject.getString("status");
                result = status.equals(OK); // else ERROR

            } catch (UnsupportedEncodingException e) {
                result = false;
                Log.i("@",e.toString());
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                result = false;
                Log.i("@",e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                result = false;
                Log.i("@",e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                result = false;
                Log.i("@",e.toString());
                e.printStackTrace();
            }

            return result;
        }



        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                listener.responseCompleteHandler(discountType,dataFromServer);
            } else {
                Log.e("@", "error "+ dataFromServer);
                listener.responseErrorHandler(discountType, dataFromServer);
            }
        }


        @Override
        protected void onCancelled() {

        }
    }



}
