package su.ias.malina.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import su.ias.malina.R;
import su.ias.malina.activities.fragments.UserCardsFragment;
import su.ias.malina.adapters.UserCardsPagerAdapter;
import su.ias.malina.app.AppSingleton;
import su.ias.malina.async.PostTask;
import su.ias.malina.components.PagerSlidingTabStrip;
import su.ias.malina.data.UserCardItem;
import su.ias.malina.interfaces.IListener;
import su.ias.malina.utils.AppApiUtils;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 21.03.2014
 * Time: 18:48
 */


public class UserCardsActivity extends BaseAppCompatActivity implements IListener {

    private static final String FLAG_CARDS_HELP_WAS_SHOWN = "cadrs_guide_was_shown";
    private ActionBar.TabListener tabListener;
    private ViewPager.SimpleOnPageChangeListener pageChangeListener;
    private ViewPager mPager;
    private Button beelineBtn;
    private UserCardsPagerAdapter fragmentPagerAdapter;

    private ArrayList<UserCardItem> activeCardsArr = new ArrayList<UserCardItem>();
    private ArrayList<UserCardItem> blockedCardsArr = new ArrayList<UserCardItem>();

    public static final String GET_CARDS = "get_cards";
    private ShowcaseView showcaseView;
    private int counter;

    private LinearLayout connectionErrorContainer;
    private Button tryAgainBtn ;
    private PagerSlidingTabStrip tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_viewpager_drawer);
        initNavButtons();


        connectionErrorContainer = (LinearLayout) findViewById(R.id.connection_error);
        tryAgainBtn = (Button) findViewById(R.id.try_again_btn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionErrorContainer.setVisibility(View.GONE);
                new PostTask(UserCardsActivity.this).execute(GET_CARDS, getJsonForPost());
            }
        });



        // вставляем кнопку с баллами билайна
        LinearLayout container = (LinearLayout)findViewById(R.id.aditional_container);
        container.setVisibility(View.VISIBLE);
        View child = getLayoutInflater().inflate(R.layout.layout_beeline_points_button, null);
        container.addView(child);



        beelineBtn = (Button) findViewById(R.id.beeline_btn);
        beelineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(activeCardsArr.size() == 0 ) {
                     Toast.makeText(UserCardsActivity.this, "Спискок карт пуст", Toast.LENGTH_SHORT).show();
                     return;
                 } else {
                     startActivity(new Intent(UserCardsActivity.this,BeelinePointsActivity.class)
                                       .putExtra(BeelinePointsActivity.EXTRA_CARDS_ARRAY, getCardsSerialNumbersArr()));
                 }
            }
        });


        initDrawer();

//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                actionBar.setSelectedNavigationItem(position);
                mPager.setCurrentItem(position);
                updatePagerFragment();
            }
        };


        mPager = (ViewPager) findViewById(R.id.pager);
        fragmentPagerAdapter = new UserCardsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(fragmentPagerAdapter);
//        mPager.setOnPageChangeListener(pageChangeListener);


        tabs = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);
        tabs.setViewPager(mPager);
        tabs.setOnPageChangeListener(pageChangeListener);


      /*  tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                ((TextView) tab.getCustomView().findViewById(R.id.tab_label)).setTextColor(getResources().getColor(R.color.tab_inactive_text_color));
            }


            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                ((TextView) tab.getCustomView().findViewById(R.id.tab_label)).setTextColor(Color.WHITE);
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) { }
        };


        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_custom_view).setTabListener(tabListener), true);
        View tabView = actionBar.getTabAt(0).getCustomView();
        ((TextView)  tabView.findViewById(R.id.tab_label)).setText("Активные");

        tabView.findViewById(R.id.tab_icon).setVisibility(View.GONE);


        actionBar.addTab(actionBar.newTab().setCustomView(R.layout.tab_custom_view).setTabListener(tabListener), false);
        tabView = actionBar.getTabAt(1).getCustomView();
        ((TextView)  tabView.findViewById(R.id.tab_label)).setText("Заблокированные");
        tabView.findViewById(R.id.tab_icon).setVisibility(View.GONE);*/

    }

    @Override
    public void setNavButtons() {

        mapNavLL.setEnabled(true);
        ((ImageView) mapNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.map));
        ((TextView)  mapNavLL.getChildAt(1)).setTextColor(getResources().getColor(R.color.navigation_btn_unactive_text_color));

        discountsNavLL.setEnabled(true);
        ((ImageView) discountsNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.action));
        ((TextView)  discountsNavLL.getChildAt(1)).setTextColor(getResources().getColor(R.color.navigation_btn_unactive_text_color));

        cardsNavLL.setEnabled(false);
        ((ImageView) cardsNavLL.getChildAt(0)).setImageDrawable(getResources().getDrawable(R.drawable.cards_active));
        ((TextView)  cardsNavLL.getChildAt(1)).setTextColor(Color.WHITE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        new PostTask(this).execute(GET_CARDS, getJsonForPost());
        setProgressBarIndeterminateVisibility(true);
    }

    public void initGuideForUserCards() {

        counter = 0;

        View.OnClickListener userCardsGuideClickHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (counter) {
                    case 0:
                        showcaseView.setContentTitle("Подключите номер Билайн для начисления баллов");
                        showcaseView.setShowcase(new ViewTarget(beelineBtn), true);
                        counter++;
                    break;
                    case 1:
                        showcaseView.setContentTitle("Нажмите на карту для ее отображения на весь экран");
                        showcaseView.setShowcase(new ViewTarget(mPager), true);
                        counter++;
                    break;
                    case 2:
                        showcaseView.hide();
                        AppSingleton.get().prefs.edit().putBoolean(FLAG_CARDS_HELP_WAS_SHOWN, true).commit();
                    break;
                }
            }
        };


        showcaseView = new ShowcaseView.Builder(this)
                .setOnClickListener(userCardsGuideClickHandler)
                .setStyle(R.style.CustomShowcaseTheme)
                .build();

        showcaseView.setContentTitle("Регистрируйте новые или добавляйте существующие карты");
//        showcaseView.setButtonText(getString(R.string.next));
//        ActionItemTarget target = new ActionItemTarget(this, R.id.action_add_card);
//        showcaseView.setShowcase(target, true);

        showcaseView.setShowcase(new ViewTarget(findViewById(R.id.for_menu)), true);
    }



    private String[] getCardsSerialNumbersArr() {

        String[] serialNumArr = new String[activeCardsArr.size()];

        for (int i = 0; i < activeCardsArr.size(); i++) {
            serialNumArr[i] = getSplitterCardCode(activeCardsArr.get(i).getCode());
        }

        return serialNumArr;
    }




    private String getSplitterCardCode(String codeWithoutSplits) {
        if(codeWithoutSplits.length() != 16)
            return codeWithoutSplits;

        String splittedStr  = codeWithoutSplits.substring(0, 4) +" "
                            + codeWithoutSplits.substring(4, 8) +" "
                            + codeWithoutSplits.substring(8, 12)+ " "
                            + codeWithoutSplits.substring(12,16);
        return splittedStr;
    }



    private void updatePagerFragment() {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + mPager.getCurrentItem());

        if (mPager.getCurrentItem() == 0 && page != null) {
            ((UserCardsFragment)page).setCardsAdapter(activeCardsArr);
            if(activeCardsArr.size()==0)
                Toast.makeText(this,"Список пуст", Toast.LENGTH_SHORT).show();
        } else if (mPager.getCurrentItem() == 1 && page != null) {
            ((UserCardsFragment)page).setCardsAdapter(blockedCardsArr);
            if(blockedCardsArr.size()==0)
                Toast.makeText(this,"Список пуст", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
    }




    private String getJsonForPost() {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(AppApiUtils.ACTION, GET_CARDS);
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


            case R.id.action_add_card:
                startActivity(new Intent(UserCardsActivity.this, CardAdditionActivity.class));

            return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cards, menu);

        // если не показывали гид на странице выбора
        if(!AppSingleton.get().prefs.getBoolean(FLAG_CARDS_HELP_WAS_SHOWN, false)) {
            initGuideForUserCards();
        }

        return true;
    }



    @Override
    public void responseCompleteHandler(String actionName, String jsonFromApi) {

        setProgressBarIndeterminateVisibility(false);

        activeCardsArr.clear();
        blockedCardsArr.clear();

        try {

            JSONObject jsonObject = new JSONObject(jsonFromApi);
            JSONArray cardsArr = jsonObject.getJSONArray("cards");

            for (int i = 0; i < cardsArr.length(); i++) {
                JSONObject cardJson = cardsArr.getJSONObject(i);

                String code = cardJson.getString("card");
                String splittedCode = getSplitterCardCode(code);
                boolean  isActive = cardJson.getString("status").equals("active");
                String type = cardJson.getString("type");
                boolean  irsEnabled = cardJson.getString("is_irs_enabled").equals("true") ;
                String beelinePhoneNum = cardJson.has("registered_beeline_phone") ? cardJson.getString("registered_beeline_phone") :"";
                String barCodeUrl = cardJson.getString("card_image");

                UserCardItem userCardItem = new UserCardItem(splittedCode, isActive, type, irsEnabled, beelinePhoneNum, barCodeUrl);

                if(isActive){
                    activeCardsArr.add(userCardItem);
                } else {
                    blockedCardsArr.add(userCardItem);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        updatePagerFragment();
    }





    @Override
    public void responseErrorHandler(String actionName, String errorStr) {
        setProgressBarIndeterminateVisibility(false);
        mPager.setVisibility(View.GONE);
        connectionErrorContainer.setVisibility(View.VISIBLE);
    }







}
