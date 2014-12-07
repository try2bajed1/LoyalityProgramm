package su.ias.malina.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import su.ias.malina.R;
import su.ias.malina.adapters.PartnersPagerAdapter;
import su.ias.malina.app.AppSingleton;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: n.senchurin
 * Date: 04.03.14
 * Time: 13:32
 */
public class PartnerDescriptionActivity extends ActionBarActivity {

    public static final String OPEN_AT_INDEX = "open_at";

    private ActionBar mActionbar;
    private ViewPager partnersVP;
    private TextView titleTV;
    private ArrayList<Integer> partnersArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selected_partner);
        mActionbar = getSupportActionBar();

        mActionbar.setCustomView(R.layout.actionbar_back_custom_view);
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        View actionBarView = mActionbar.getCustomView();

        titleTV = (TextView) actionBarView.findViewById(R.id.title);
        titleTV.setText("Партнеры");

        actionBarView.findViewById(R.id.filter_btn).setVisibility(View.GONE);
        actionBarView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        partnersArr = AppSingleton.get().getDBAdapter().getParntersIndexesForMap("msk");

        partnersVP = (ViewPager) findViewById(R.id.partners_view_pager);
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                String titleStr = AppSingleton.get().getDBAdapter().getPartnerNameById("msk",partnersArr.get(position));
                titleTV.setText(titleStr);
            }
        };

        partnersVP.setOnPageChangeListener(pageChangeListener);
        PartnersPagerAdapter fragmentPagerAdapter = new PartnersPagerAdapter(getSupportFragmentManager(), partnersArr);
        partnersVP.setAdapter(fragmentPagerAdapter);

        //set current page due to position in list in
        int pageNum = getIntent().getIntExtra(OPEN_AT_INDEX, 0);
        partnersVP.setCurrentItem(pageNum);

    }
}
